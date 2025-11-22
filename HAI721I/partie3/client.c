#include <stdlib.h>
#include <stdio.h>
#include <sys/socket.h>
#include <unistd.h>
#include <arpa/inet.h>
#include <string.h>
#include <errno.h>
#include <pthread.h>

#define NB_SITES_MAX 10

//struct message avec un champ char[256] et un entier
struct M {
    char texte[256];
    int HV[NB_SITES_MAX];
    int i;
};

// structure liste chainée pour stocker les messages reçus
struct MessageNode {
    struct M message;
    struct MessageNode* next;
};

// Structure pour regrouper les paramètres nécessaires aux threads
struct ThreadParams {
    int sockAcceptation;
    int* HV;
    fd_set* ensemble;
    fd_set* ensemble_initial;
    int* max;
    int nbVoisins;
    int nbPi;
    int clientPort;
    int isSource;
    int intervalle;
    struct sockaddr_in* adressesVoisins;
    struct MessageNode* premierNoeud;
    pthread_mutex_t* mutex_ensemble; // Mutex pour protéger ensemble_initial
    int index; // Ajout du champ index
};

void afficherHV(const char* prefixe, int* HV, int nbPi) {
    printf("%s [", prefixe);
    for (int i = 0; i < nbPi; i++) {
        printf("%d", HV[i]);
        if (i < nbPi - 1) {
            printf(", ");
        }
    }
    printf("]\n");
}

void* thread_input(void* arg) {
    printf("Thread input démarré...\n");
    struct ThreadParams* params = (struct ThreadParams*)arg;
    int sockAcceptation = params->sockAcceptation;
    struct sockaddr_in clientAddr;
    socklen_t addrLen = sizeof(clientAddr);
    fd_set lecture;

    // Ecoute
    listen(sockAcceptation, params->nbVoisins);

    while (1) {
        // 1. REINITIALISER le set de lecture
        pthread_mutex_lock(params->mutex_ensemble);
        lecture = *(params->ensemble_initial); 
        pthread_mutex_unlock(params->mutex_ensemble);

        // 2. SELECT
        int activite = select(*(params->max) + 1, &lecture, NULL, NULL, NULL);
        if (activite < 0 && errno != EINTR) { perror("Select"); continue; }

        // 3. GESTION ACCEPTATION (Nouveaux voisins)
        if (FD_ISSET(sockAcceptation, &lecture)) {
            int newSock = accept(sockAcceptation, (struct sockaddr*)&clientAddr, &addrLen);
            if (newSock != -1) {
                printf("Connexion : %s:%d\n", inet_ntoa(clientAddr.sin_addr), ntohs(clientAddr.sin_port));
                pthread_mutex_lock(params->mutex_ensemble);
                FD_SET(newSock, params->ensemble_initial);
                if (*(params->max) < newSock ) *(params->max) = newSock;
                pthread_mutex_unlock(params->mutex_ensemble);
            }
        }

        // 4. GESTION MESSAGES (Voisins existants)
        for(int i=3; i<=*(params->max); i++) {
            if(i != sockAcceptation && FD_ISSET(i, &lecture)) {
                
                struct M messageRecu;
                int bytesReceived = recv(i, &messageRecu, sizeof(struct M), 0);
                
                if (bytesReceived <= 0) {
                    close(i);
                    pthread_mutex_lock(params->mutex_ensemble);
                    FD_CLR(i, params->ensemble_initial);
                    pthread_mutex_unlock(params->mutex_ensemble);
                } else {
                    int dejaRecu = 0;
                    struct MessageNode* current = params->premierNoeud;
                    while (current != NULL) {
                        // Un message est un doublon si MEME emetteur ET MEME horloge
                        if (current->message.i == messageRecu.i && 
                            memcmp(current->message.HV, messageRecu.HV, params->nbPi * sizeof(int)) == 0) {
                            dejaRecu = 1;
                            break;
                        }
                        current = current->next;
                    }

                    if (!dejaRecu) {
                        printf("--> Réception brute de %d (HV notée)\n", messageRecu.i);
                        
                        // Stockage dans la liste (Buffer)
                        struct MessageNode* newNode = malloc(sizeof(struct MessageNode));
                        newNode->message = messageRecu;
                        newNode->next = params->premierNoeud;
                        params->premierNoeud = newNode;

                        pthread_mutex_lock(params->mutex_ensemble);
                        for(int j=3; j<=*(params->max); j++) {
                            if(FD_ISSET(j, params->ensemble_initial) && j != sockAcceptation && j != i) {
                                send(j, &messageRecu, sizeof(struct M), 0);
                            }
                        }
                        pthread_mutex_unlock(params->mutex_ensemble);

                        // --- ETAPE C : Tentative de Délivrance Causale ---
                        // On parcourt la liste pour voir si des messages peuvent être "lus" (délivrés)
                        // Condition de délivrance (Causalité) :
                        // 1. W[emetteur] == Local[emetteur] + 1
                        // 2. W[k] <= Local[k] (pour tout autre k)
                        
                        int progres = 1;
                        while(progres) {
                            progres = 0;
                            struct MessageNode** scan = &params->premierNoeud;
                            while(*scan != NULL) {
                                struct M* m = &((*scan)->message);
                                int emetteur = m->i; // Index de l'émetteur du message
                                
                                // Vérification de la condition causale
                                int livrable = 1;
                                if (m->HV[emetteur-1] != params->HV[emetteur-1] + 1) {
                                    livrable = 0; // Pas le prochain attendu de cet émetteur
                                } else {
                                    for (int k = 0; k < params->nbPi; k++) {
                                        if (k != emetteur-1 && m->HV[k] > params->HV[k]) {
                                            livrable = 0; // Il a vu des messages que je n'ai pas encore vus
                                            break;
                                        }
                                    }
                                }

                                if (livrable) {
                                    // --- DELIVRANCE OFFICIELLE ---
                                    printf("+++ DELIVRANCE du message de %d : %s\n", emetteur, m->texte);
                                    afficherHV("Etat avant MAJ", params->HV, params->nbPi);
                                    
                                    // Mise à jour de l'horloge locale
                                    for(int k=0; k<params->nbPi; k++) {
                                        if (m->HV[k] > params->HV[k]) params->HV[k] = m->HV[k];
                                    }
                                    params->HV[params->index-1]++; // On incrémente notre propre horloge pour l'acte de délivrance? (Optionnel selon variante)
                                    
                                    afficherHV("Etat après MAJ", params->HV, params->nbPi);

                                    // Retirer le message de la liste (ou le marquer comme traité)
                                    // Ici, on le retire simplement pour simplifier
                                    struct MessageNode* toFree = *scan;
                                    *scan = (*scan)->next;
                                    free(toFree);
                                    
                                    progres = 1; // On a avancé, on recommence le scan car cela a pu débloquer d'autres messages
                                } else {
                                    scan = &((*scan)->next);
                                }
                            }
                        }
                    } else {
                        // Message déjà vu, on l'ignore (on ne retransmet pas pour éviter les boucles infinies, 
                        // car nos voisins l'ont probablement déjà eu ou vont l'avoir via d'autres chemins)
                    }
                }
            }
        }
    }
    return NULL;
}

void* thread_output(void* arg) {
    printf("Thread output démarré...\n");
    struct ThreadParams* params = (struct ThreadParams*)arg;
    struct sockaddr_in* adressesVoisins = params->adressesVoisins;
    for(int i = 0; i < params->nbVoisins; i++){
        // se connecter à chaque voisin plus petit et envoyer un message
        if(adressesVoisins[i].sin_port == 0) {
            printf("Erreur : Port du voisin %d est invalide (0).\n", i);
            continue;
        }
        if(adressesVoisins[i].sin_addr.s_addr == 0) {
            printf("Erreur : Voisin %d a une adresse invalide.\n", i);
            continue;
        }
        if(ntohs(adressesVoisins[i].sin_port) < params->clientPort){
            int sockVoisin = socket(AF_INET,SOCK_STREAM,0);
            int connected = 0;
            int tentatives = 0;
            while (!connected && tentatives < 10) { // On essaie pendant 10 secondes max
                if (connect(sockVoisin, (struct sockaddr*)&adressesVoisins[i], sizeof(struct sockaddr_in)) == -1) {
                    printf("Tentative de connexion au voisin %d échouée (pas encore prêt ?). Nouvelle tentative dans 1s...\n", i);
                    sleep(1); // Attendre 1 seconde avant de retenter
                    tentatives++;
                } else {
                    connected = 1;
                }
            }

            if (!connected) {
                perror("Abandon après 10 tentatives. Connect crash");
                exit(EXIT_FAILURE);
            }
            //mettre à jour le fd_set
            pthread_mutex_lock(params->mutex_ensemble); // Protéger l'accès à ensemble_initial
            FD_SET(sockVoisin, params->ensemble_initial);
            if (*(params->max) < sockVoisin) {
                *(params->max) = sockVoisin;
            }
            pthread_mutex_unlock(params->mutex_ensemble); // Déverrouiller après modification
            printf("Connexion établie avec le voisin : %s:%d\n",inet_ntoa(adressesVoisins[i].sin_addr),ntohs(adressesVoisins[i].sin_port));
        }   
    }
    // on laisse allumé le thread pour la source, pour envoyer le premier message de diffusion
    if(params->isSource == 1){ // si c'est la source
        while(1){
            struct M message;
            snprintf(message.texte, sizeof(message.texte), "Message de diffusion initial");
            message.i = params->index;
            //envoyer le message à tous les voisins
            pthread_mutex_lock(params->mutex_ensemble);
            for(int j=3; j<=*(params->max); j++) {
                if(FD_ISSET(j, params->ensemble_initial) && j != params->sockAcceptation) {
                    // incrémenter l'horloge vectorielle (le int* HV dans les args) locale avant d'envoyer
                    params->HV[params->index-1]++;
                    //estampiller l'horloge vectorielle dans le message
                    memcpy(message.HV, params->HV, params->nbPi * sizeof(int));
                    send(j, &message, sizeof(struct M), 0);

                    char debugMsg[100];
                    sprintf(debugMsg, "--> Envoi initial vers %d (mon horloge)", j);
                    afficherHV(debugMsg, params->HV, params->nbPi);
                }
            }
            pthread_mutex_unlock(params->mutex_ensemble);
            sleep(params->intervalle);
        }
    }
    return NULL;
}

int main(int argc, char* argv[]){
    int* HV; // liste des horloges vectorielles de tous les sites

    if (argc!=9){
        printf("Signature correcte: %s <index> <port> <ip> <portTCP> <nbVoisins> <intervalle> <isSource> <nbPi>\n",argv[0]);
        exit(EXIT_FAILURE);
    }
    
    int index = atoi(argv[1]);
    int targetPort = atoi(argv[2]);
    char* targetIp = argv[3];

    //Initialisation des paramètres nécessaires pour les threads
    int clientPort = 5000 + (index % 50000); // Plage de ports entre 5000 et 6000
    int intervalle = atoi(argv[6]);
    int isSource = atoi(argv[7]);
    int nbPi = atoi(argv[8]);

    //initialiser les horloges vectorielles avec nbPi éléments à 0
    HV = calloc(nbPi, sizeof(int));

    //Signaler existence au serveur------------

    struct sockaddr_in servAddr;
    servAddr.sin_family = AF_INET;
    if (-1==inet_pton(AF_INET,targetIp,&servAddr.sin_addr)){perror("Inet pton a crash");exit(EXIT_FAILURE);}
    servAddr.sin_port = htons(targetPort);

    int sockClient = socket(AF_INET, SOCK_DGRAM, 0);
    if (sockClient == -1) {
        perror("socket");
        exit(EXIT_FAILURE);
    }

    struct sockaddr_in addrClient;
    addrClient.sin_family = AF_INET;
    addrClient.sin_addr.s_addr = INADDR_ANY; // listen on all interfaces
    addrClient.sin_port = htons(clientPort);

    if (bind(sockClient,(struct sockaddr*) &addrClient,sizeof(addrClient))==-1){
        perror("bind client crash\n");
        exit(EXIT_FAILURE);
    }

    printf("Socket créée, je suis le site %d\n", index);

    sendto(sockClient,&index,sizeof(int),0,(struct sockaddr*) &servAddr,sizeof(struct sockaddr_in));
    printf("J'EXISTE (et le serveur le sait) !!!!!!!!!!!!\n");

    //Recevoir la liste des adresses des voisins
    printf("En attente de la liste des adresses des voisins...\n");
    int nbVoisins = atoi(argv[5]);
    struct sockaddr_in adressesVoisins[nbVoisins];
    socklen_t addr_len = sizeof(struct sockaddr_in);
    socklen_t buffer_len = sizeof(struct sockaddr_in) * nbVoisins;

    int bytesReceived = recvfrom(sockClient, &adressesVoisins, buffer_len, 0, (struct sockaddr*) &servAddr, &addr_len);
    if (bytesReceived == -1) {
        perror("Erreur lors de la réception des adresses des voisins");
        exit(EXIT_FAILURE);
    }

    if (bytesReceived != buffer_len) {
        printf("Erreur : Taille des données reçues (%d) différente de la taille attendue (%u).\n", bytesReceived, (unsigned int)buffer_len);
        // Tu devrais quitter ou gérer cette erreur ici
        close(sockClient);
        exit(EXIT_FAILURE);
    }

    printf("Liste des adresses des voisins reçue :\n");
    for (int i = 0; i < nbVoisins; i++) {
        printf("Voisin %d : IP = %s, Port = %d\n", i, inet_ntoa(adressesVoisins[i].sin_addr), ntohs(adressesVoisins[i].sin_port));
    }

    close(sockClient); //fermer la connexion UDP serveur

    //Connexions TCP avec mes voisins

    int sockAcceptation = socket(AF_INET,SOCK_STREAM,0);

    int opt = 1;
    if (setsockopt(sockAcceptation, SOL_SOCKET, SO_REUSEADDR, &opt, sizeof(opt)) < 0) {
    perror("setsockopt");
    }

    struct sockaddr_in addrAcceptation;
    addrAcceptation.sin_family = AF_INET;
    addrAcceptation.sin_addr.s_addr = INADDR_ANY;
    addrAcceptation.sin_port = htons(clientPort);

    if (bind(sockAcceptation,(struct sockaddr*) &addrAcceptation,sizeof(addrAcceptation))==-1){
        perror("bind acceptation crash\n");
        exit(EXIT_FAILURE);
    }

    //initialiser une liste des sockets
    int *socketsVoisins = malloc(sizeof(int)*nbVoisins);

    int voisinsPlusGrands[nbVoisins];
    int voisinsPlusPetits[nbVoisins];
    for(int i=0; i<nbVoisins; i++) {
        voisinsPlusGrands[i] = -1; // -1 comme valeur "invalide"
        voisinsPlusPetits[i] = -1;
    }

    int nbVoisinsPlusGrands = 0;
    int nbVoisinsPlusPetits = 0;

    for (int i=0;i<nbVoisins;i++){
        if (adressesVoisins[i].sin_port == 0) {
            printf("Erreur : Port du voisin %d est invalide (0).\n", i);
            continue;
        }
        if (adressesVoisins[i].sin_addr.s_addr == 0) {
            printf("Erreur : Voisin %d a une adresse invalide.\n", i);
            continue;
        }
        if (ntohs(adressesVoisins[i].sin_port) > clientPort) { 
            voisinsPlusGrands[nbVoisinsPlusGrands] = i; // Stocke l'index i
            nbVoisinsPlusGrands++; // Incrémente le VRAI compteur
        } else {
            voisinsPlusPetits[nbVoisinsPlusPetits] = i; // Stocke l'index i
            nbVoisinsPlusPetits++; // Incrémente le VRAI compteur
        }
    }

    fd_set ensemble;
    fd_set ensemble_initial;

    FD_ZERO (&ensemble_initial);
    FD_SET (sockAcceptation, &ensemble_initial);
    ensemble = ensemble_initial;
    int max = sockAcceptation;

    // Initialisation des mutex
    pthread_mutex_t mutex_ensemble = PTHREAD_MUTEX_INITIALIZER;

    // Initialisation de la structure ThreadParams
    struct ThreadParams params;
    params.sockAcceptation = sockAcceptation;
    params.ensemble = &ensemble;
    params.ensemble_initial = &ensemble_initial;
    params.max = &max;
    params.nbVoisins = nbVoisins;
    params.nbPi = nbPi;
    params.HV = HV;
    params.clientPort = clientPort;
    params.isSource = isSource;
    params.intervalle = intervalle;
    params.adressesVoisins = adressesVoisins;
    params.premierNoeud = NULL; // Initialisation de la liste chaînée
    params.mutex_ensemble = &mutex_ensemble;
    params.index = index; // Initialisation de l'index

    // Renommer les identifiants de threads pour éviter les conflits
    pthread_t thread_input_id;
    pthread_t thread_output_id;

    printf("Démarrage des threads de gestion des entrées et sorties...\n");

    // Créer les threads
    pthread_create(&thread_input_id, NULL, thread_input, (void*)&params);
    pthread_create(&thread_output_id, NULL, thread_output, (void*)&params);

    pthread_join(thread_input_id, NULL);
    pthread_join(thread_output_id, NULL);

    free(socketsVoisins);
    free(HV);
    close(sockAcceptation);

    return 0;
}