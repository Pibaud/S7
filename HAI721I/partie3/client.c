#include <stdlib.h>
#include <stdio.h>
#include <sys/socket.h>
#include <unistd.h>
#include <arpa/inet.h>
#include <string.h>
#include <pthread.h>

//struct message avec un champ char[256] et un entier
struct M {
    char texte[256];
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
    fd_set* ensemble;
    fd_set* ensemble_initial;
    int* max;
    int nbVoisins;
    int clientPort;
    int isSource;
    int intervalle;
    struct sockaddr_in* adressesVoisins;
    struct MessageNode* premierNoeud;
    pthread_mutex_t* mutex_ensemble; // Mutex pour protéger ensemble_initial
    int index; // Ajout du champ index
};

void* thread_input(void* arg) {
    printf("Thread input démarré...\n");
    struct ThreadParams* params = (struct ThreadParams*)arg;
    int sockAcceptation = params->sockAcceptation;
    struct sockaddr_in clientAddr;
    socklen_t addrLen = sizeof(clientAddr);
    listen(sockAcceptation, params->nbVoisins);

    while (1) {
        // Protéger l'accès à ensemble_initial avec un mutex
        pthread_mutex_lock(params->mutex_ensemble);
        select(*(params->max) + 1, params->ensemble, NULL, NULL, NULL);
        pthread_mutex_unlock(params->mutex_ensemble);

        if (FD_ISSET(sockAcceptation, params->ensemble)) {
            //boucler dans le set pour voir quelle socket est prête
            for(int i=3; i<=*(params->max); i++) {
                if(i == sockAcceptation) { // c'est la socket d'acceptation, on accepte la connexion
                    int newSock = accept(sockAcceptation, (struct sockaddr*)&clientAddr, &addrLen);
                    if (newSock == -1) {
                        perror("Erreur lors de l'acceptation d'une connexion");
                        continue;
                    }
                    printf("Nouvelle connexion acceptée de : %s:%d\n", inet_ntoa(clientAddr.sin_addr), ntohs(clientAddr.sin_port));
                    //ajouter le newSock au fd_set
                    pthread_mutex_lock(params->mutex_ensemble);
                    FD_SET(newSock, params->ensemble_initial);
                    if (*(params->max) < newSock ) *(params->max) = newSock;
                    pthread_mutex_unlock(params->mutex_ensemble);
                }
                else{ // c'est une socket de client, on gère la réception des messages
                    struct M messageRecu;
                    int bytesReceived = recv(i, &messageRecu, sizeof(struct M), 0);
                    if (bytesReceived <= 0) {
                            // erreur ou connexion fermée
                        printf("Connexion fermée ou erreur sur le socket %d\n", i);
                        close(i);
                        FD_CLR(i, params->ensemble_initial);
                    } else {
                        //véifier si le message a déjà été reçu dans la liste chainée
                        printf("Message reçu : %s, i = %d\n", messageRecu.texte, messageRecu.i);
                        // vérifier dans la liste chainée
                        struct MessageNode* current = params->premierNoeud;
                        int dejaRecu = 0;
                        while (current != NULL) {
                            if (current->message.i == messageRecu.i) {
                                dejaRecu = 1;
                                break;
                            }
                            current = current->next;
                        }
                        if (dejaRecu == 0) {// nouveau message, l'ajouter à la liste chainée
                            printf("Nouveau message, ajout à la liste chainée\n");
                            struct MessageNode* newNode = malloc(sizeof(struct MessageNode));
                            if (newNode == NULL) {
                                perror("malloc");
                                exit(EXIT_FAILURE);
                            }
                            newNode->message = messageRecu;
                            newNode->next = params->premierNoeud;
                            params->premierNoeud = newNode;
                            // diffuser le message aux voisins
                            
                            //itérer dans les fd_set des voisins, envoyer qu'aux voisins qui ne sont pas l'émetteur
                            for(int j=3; j<=*(params->max); j++) {
                                if(FD_ISSET(j, params->ensemble_initial) && j != sockAcceptation && j != i) {
                                    send(j, &messageRecu, sizeof(struct M), 0);
                                    printf("Message retransmis au socket %d : %s, i = %d\n", j, messageRecu.texte, messageRecu.i);
                                }
                            }
                        }
                    }
                }
            }
        }
        params->ensemble = params->ensemble_initial;
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
            if (connect(sockVoisin, (struct sockaddr*)&adressesVoisins[i], sizeof(struct sockaddr_in)) == -1) {
                perror("connect crash\n");
                exit(EXIT_FAILURE);
            }
            //mettre à jour le fd_set
            pthread_mutex_lock(params->mutex_ensemble); // Protéger l'accès à ensemble_initial
            FD_SET(sockVoisin, params->ensemble_initial);
            pthread_mutex_unlock(params->mutex_ensemble); // Déverrouiller après modification
            printf("Connexion établie avec le voisin : %s:%d\n",
           inet_ntoa(adressesVoisins[i].sin_addr),
           ntohs(adressesVoisins[i].sin_port));
        }   
    }
    // on laisse allumé le thread pour la source, pour envoyer le premier message de diffusion
    if(params->isSource == 1){ // si c'est la source
        while(1){
            struct M message;
            snprintf(message.texte, sizeof(message.texte), "Message de diffusion initiale");
            message.i = params->index;
            //envoyer le message à tous les voisins
            pthread_mutex_lock(params->mutex_ensemble);
            for(int j=3; j<=*(params->max); j++) {
                if(FD_ISSET(j, params->ensemble_initial) && j != params->sockAcceptation) {
                    send(j, &message, sizeof(struct M), 0);
                    printf("Message initial envoyé au socket %d : %s, i = %d\n", j, message.texte, message.i);
                }
            }
            pthread_mutex_unlock(params->mutex_ensemble);
            sleep(params->intervalle);
        }
    }
    return NULL;
}

int main(int argc, char* argv[]){
    if (argc!=8){
        printf("Signature correcte: %s <index> <port> <ip> <portTCP> <nbVoisins> <intervalle> <isSource>\n",argv[0]);
        exit(EXIT_FAILURE);
    }
    
    int index = atoi(argv[1]);
    int targetPort = atoi(argv[2]);
    char* targetIp = argv[3];

    //Initialisation des paramètres nécessaires pour les threads
    int clientPort = 5000 + (index % 50000); // Plage de ports entre 5000 et 6000
    int intervalle = atoi(argv[6]);
    int isSource = atoi(argv[7]);

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

    close(sockAcceptation);
}