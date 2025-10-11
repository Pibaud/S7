#include <arpa/inet.h>
#include <netdb.h>
#include <netinet/in.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/socket.h>
#include <sys/types.h>
#include <unistd.h>

int main(int argc, char *argv[]) {

  int nbPi = 6;

  if (argc != 2) {
    printf("utilisation : %s port_serveur\n", argv[0]);
    exit(1);
  }

  int ds = socket(PF_INET, SOCK_DGRAM, 0);
  if (ds == -1) {
    perror("Serveur : pb creation socket :");
    exit(1);
  }
  printf("Serveur : création de la socket réussie \n");

  struct sockaddr_in serverAddr;
  serverAddr.sin_family = AF_INET;
  serverAddr.sin_port = htons(atoi(argv[1]));
  serverAddr.sin_addr.s_addr = INADDR_ANY;

  socklen_t tailleStructServ = sizeof(serverAddr);
  if (bind(ds, (struct sockaddr *)&serverAddr, tailleStructServ) < 0) {
    perror("Serveur : erreur lors du bind :");
    close(ds);
    exit(1);
  }
  printf("Serveur : bind réussi\n");

  int clientsRecus = 0;

  //liste de couples (idClient, addrClient)
  struct client {
      int idClient;
      struct sockaddr_in addrClient;
  } ;

  struct client clients[6];
  printf("%d", clients[0].idClient);

  while(clientsRecus < 6){
    int tailleStructClient = sizeof(clients[clientsRecus].addrClient);
    int rcv = recvfrom(ds, &clients[clientsRecus].idClient, sizeof(int), 0, (struct sockaddr*)&clients[clientsRecus].addrClient, &tailleStructClient);
    if (rcv < 0) {
        perror("Serveur : erreur réception message :");
        continue;
    }
    printf("Serveur : message reçu de %s:%d, id = %d\n", inet_ntoa(clients[clientsRecus].addrClient.sin_addr), ntohs(clients[clientsRecus].addrClient.sin_port), clients[clientsRecus].idClient);
    clientsRecus++;
  }

  printf("Serveur : tous les clients ont été reçus\n");

  // envoi des infos à chaque Pi
  // pour chaque client, sendto l'adresse de son voisin

  for(int i = 0; i < clientsRecus; i++){
    struct sockaddr_in voisin = clients[(i+1)%clientsRecus].addrClient;
    sendto(ds, &voisin, sizeof(voisin), 0, (struct sockaddr*)&clients[i].addrClient, sizeof(clients[i].addrClient));
  }

  printf("Serveur : toutes les adresses ont été envoyées\n");

  close(ds);
  printf("Serveur : je termine\n");
  return 0;
}