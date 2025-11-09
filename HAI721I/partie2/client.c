#include <stdlib.h>
#include <stdio.h>
#include <sys/socket.h>
#include <unistd.h>
#include <arpa/inet.h>
#include <string.h>

int main(int argc, char* argv[]){
    if (argc!=6){
        printf("Signature correcte: %s <index> <port> <ip> <portTCP> <nbVoisins>\n",argv[0]);
        exit(EXIT_FAILURE);
    }
    
    int index = atoi(argv[1]);
    int targetPort = atoi(argv[2]);
    int clientPort = 5000 + (index % 1000); // Plage de ports entre 5000 et 6000
    char* targetIp = argv[3];

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

printf("Socket créée\n");

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

// Dans client.c, après recvfrom
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
addrAcceptation.sin_addr.s_addr = INADDR_ANY; // listen on all interfaces
addrAcceptation.sin_port = htons(clientPort);

if (bind(sockAcceptation,(struct sockaddr*) &addrAcceptation,sizeof(addrAcceptation))==-1){
    perror("bind acceptation crash\n");
    exit(EXIT_FAILURE);
}

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

//pour éviter les interblocages, on commence par accepter les connexions des voisins avec un indice plus grand que le sien
listen(sockAcceptation,nbVoisinsPlusGrands);

for (int i=0;i<nbVoisinsPlusGrands;i++){
    if (adressesVoisins[voisinsPlusGrands[i]].sin_port > clientPort){
        int sockVoisin = accept(sockAcceptation,NULL,NULL);
        printf("Connexion acceptée d'un voisin\n");
    }else{
        printf("adressesVoisins[voisinsPlusGrands[i]].sin_port < clientPort : %d < %d\n", ntohs(adressesVoisins[voisinsPlusGrands[i]].sin_port), clientPort);
    }
}

// Puis on se connecte aux voisins avec un indice plus petit que le sien
// Avant de se connecter aux voisins
if(index > 1){ // pas de client plus petit
    printf("Connexion aux voisins avec un indice plus petit...\n");
    for (int i = 0; i < nbVoisinsPlusPetits; i++) {
        printf("Tentative de connexion au voisin %d : Port = %d\n", voisinsPlusPetits[i], ntohs(adressesVoisins[voisinsPlusPetits[i]].sin_port));
        int sockVoisin = socket(AF_INET,SOCK_STREAM,0);
        if (connect(sockVoisin,(struct sockaddr*) &adressesVoisins[voisinsPlusPetits[i]],sizeof(struct sockaddr_in))==-1){
            perror("connect crash\n");
            exit(EXIT_FAILURE);
        }
        printf("Connexion établie avec un voisin\n");
    }
}

//faire une saisie au clavier pour stoppper le programme
printf("Appuyez sur Entrée pour terminer le programme...\n");
getchar();

close(sockAcceptation);
}