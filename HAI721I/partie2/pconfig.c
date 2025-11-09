#include <stdlib.h>
#include <stdio.h>
#include <sys/socket.h>
#include <unistd.h>
#include <arpa/inet.h>
#include <string.h>

int main(int argc, char* argv[]){

if (argc!=3){
     printf("Signature correcte: %s <portServeur> <fichier texte>\n",argv[0]);
    exit(EXIT_FAILURE);
}

FILE *fichier;
fichier = fopen(argv[2], "r");

//lire le fichier, en déduire le nombre de Pi et leur voisinnage respectif
char *p = "p";
char line[256];
int nbPi;
int nbAretes;
int *arites;
int noeudA, noeudB;

//première boucle pour connaitre les arites et nbPi
if (fichier != NULL) {
    while (fgets(line, sizeof(line), fichier)) {
        if (sscanf(line, "p edge %d %d", &nbPi, &nbAretes) == 2) {
            printf("Specs du graphe :\n");
            printf("Nombre de sommets : %d\n", nbPi);
            printf("Nombre d'arêtes : %d\n", nbAretes);

            arites = calloc(nbPi, sizeof(int)); //initialise mais surtout, les met à zéro
        }
        if (sscanf(line, "e %d %d", &noeudA, &noeudB) == 2) {
            arites[noeudA-1] += 1; // -1 car dans cette convention, les noeuds commencent à 1
            arites[noeudB-1] += 1;
        }
    }
    fclose(fichier);
}
else {
    fprintf(stderr, "Unable to open fichier!\n");
}

int** voisinages;
voisinages = malloc(nbPi * sizeof(int*)); 

for (int i = 0; i < nbPi; i++) {
    int arite = arites[i];
    voisinages[i] = calloc(arite * sizeof(int), sizeof(int)); //initialise mais surtout, les met à zéro
}

fichier = fopen(argv[2], "r");

if (fichier != NULL) {
    while (fgets(line, sizeof(line), fichier)) {
        if (sscanf(line, "e %d %d", &noeudA, &noeudB) == 2) {
            int i = 0;
            //vérifier qu'on n'ajoute pas deux fois le même voisin
            int shouldAddA = 1;
            int shouldAddB = 1;
            while (i < arites[noeudA - 1] && voisinages[noeudA - 1][i] != 0 && voisinages[noeudA - 1][i] != noeudB) {
                i += 1;
            }
            if (i < arites[noeudA - 1] && shouldAddA == 1) {
                voisinages[noeudA - 1][i] = noeudB;
            }

            i = 0;
            while (i < arites[noeudB - 1] && voisinages[noeudB - 1][i] != 0 && voisinages[noeudB - 1][i] != noeudA) {
                i += 1;
            }
            if (i < arites[noeudB - 1] && shouldAddB == 1) {
                voisinages[noeudB - 1][i] = noeudA;
            }
            // Debugging: Print the voisinages table

            
        }

    }
    fclose(fichier);
}
else {
    fprintf(stderr, "Unable to open fichier!\n");
}

printf("Voisinages:\n");
            for (int i = 0; i < nbPi; i++) {
                printf("Noeud %d: ", i + 1);
                for (int j = 0; j < arites[i]; j++) {
                    printf("%d ", voisinages[i][j]);
                }
                printf("\n");
            }

int portServeur = atoi(argv[1]);
struct sockaddr_in PIaddresses[nbPi];

//Init server------------

struct sockaddr_in servAddr;
int sockServer = socket(AF_INET, SOCK_DGRAM, 0);
if (sockServer == -1) {
    perror("socket");
    exit(EXIT_FAILURE);
}

servAddr.sin_family = AF_INET;
servAddr.sin_addr.s_addr = INADDR_ANY;
servAddr.sin_port = htons(portServeur);

if (bind(sockServer,(struct sockaddr*) &servAddr,sizeof(servAddr))==-1){
    perror("bind crash\n");
    exit(EXIT_FAILURE);
}


// lancer chaque Pi avec dans une boucle for ./client <i> <portServeur> <127.0.0.1> <portTCP (i*100)> <nbVoisins (arite)>
for (int i = 0; i < nbPi; i++) {
    int nbVoisins = arites[i];
    int portTCP = 5000 + (i+1 % 1000); // Plage de ports entre 5000 et 6000

    printf("Lancer ./client %d %d 127.0.0.1 %d %d\n", i+1, portServeur, portTCP, nbVoisins);

}

printf("Serveur prêt à recevoir les clients\n");

// Attendre les x processus
int receivedProcesses = 0;
struct sockaddr_in currentAddress;

while (receivedProcesses < nbPi) {
    int ind;
    socklen_t addrlen = sizeof(struct sockaddr_in);
    recvfrom(sockServer, &ind, sizeof(int), 0, (struct sockaddr *) &currentAddress, &addrlen); 
    if (ind > 0 && ind <= nbPi) {
    memcpy(&PIaddresses[ind - 1], &currentAddress, sizeof(struct sockaddr_in));
    printf("Process reçu:%d (stocké à l'index %d)\n", ind, ind - 1);
    } else {
        printf("Process reçu avec index invalide:%d\n", ind);
    }
    printf("Process reçu:%d\n", ind);
    receivedProcesses++;
}

printf("Tous les clients ont signalé leur existence\n");

for (int i=0;i<nbPi;i++){
    // envoyer à chaque Pi la liste des adresses de ses voisins pour faire du TCP
    int nbVoisins = arites[i];

    // Initialiser les adresses des voisins
    struct sockaddr_in voisins[nbVoisins];
    for (int j = 0; j < nbVoisins; j++) {
        struct sockaddr_in voisinj = {0}; // Initialisation complète à zéro
        voisinj.sin_family = AF_INET;
        if (inet_pton(AF_INET, "127.0.0.1", &voisinj.sin_addr) <= 0) {
            perror("inet_pton failed");
            exit(EXIT_FAILURE);
        }
        voisinj.sin_port = htons(5000 + (voisinages[i][j]) % 1000);
        voisins[j] = voisinj;

        // Debugging: Print each neighbor's address and port
        printf("Client %d, Voisin %d : IP = 127.0.0.1, Port = %d\n", i, j, ntohs(voisinj.sin_port));
    }

    // Ensure no uninitialized memory is sent
    memset(voisins + nbVoisins, 0, sizeof(struct sockaddr_in) * (nbVoisins));

    // Avant d'envoyer la liste des voisins
    printf("Envoi de la liste des voisins au client %d...\n", i);
    for (int j = 0; j < nbVoisins; j++) {
        printf("Voisin %d : Port = %d\n", j, ntohs(voisins[j].sin_port));
    }

    // Debugging: Print the size of data being sent
    printf("Envoi de %lu octets au client %d\n", sizeof(struct sockaddr_in) * nbVoisins, i);
    sendto(sockServer, voisins, sizeof(struct sockaddr_in) * nbVoisins, 0, (struct sockaddr*)&PIaddresses[i], sizeof(struct sockaddr_in));
    
}

printf("FINI\n");
close(sockServer);
}