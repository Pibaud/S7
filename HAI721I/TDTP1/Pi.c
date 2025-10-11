#include <stdio.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netdb.h>
#include <stdlib.h>
#include <arpa/inet.h>
#include <string.h>

/* Programme client */

int main(int argc, char *argv[])
{

   if (argc != 4)
   {
      printf("utilisation : %s id ip_serveur port_serveur\n", argv[0]);
      exit(1);
   }

   int ds = socket(PF_INET, SOCK_DGRAM, 0);

  if (ds == -1){
    perror("Client : pb creation socket :");
    exit(1);
  }

  struct sockaddr_in server_addr;
  server_addr.sin_family = PF_INET;
  server_addr.sin_port = htons(atoi(argv[3]));
  server_addr.sin_addr.s_addr = inet_addr(argv[2]);

  socklen_t server_len = sizeof(server_addr);

  printf("j'envoie mon id : %s , de taille %ld\n", argv[1], strlen(argv[1]));
  //envoyer l'id au serveur passé en paramètre
  int id = atoi(argv[1]);
  int res = sendto(ds, &id, sizeof(int), 0, (struct sockaddr*)&server_addr, server_len);
  if (res < 0) {
    perror("Client : erreur envoi id :");
    close(ds);
    exit(1);
  }
  printf("Client : envoi id réussi\n");

  struct sockaddr_in adresse_voisin;
  recv(ds, &adresse_voisin, sizeof(adresse_voisin), 0);
  printf("Adresse reçue du serveur : %s:%d\n", inet_ntoa(adresse_voisin.sin_addr), ntohs(adresse_voisin.sin_port));

  /* Etape 6 : fermer la socket (lorsqu'elle n'est plus utilisée)*/

  struct Message {
      int idClient;
      int counter;
  } ;

  struct Message msgInitial;
  msg.idClient = id;
  msg.counter = 0;

  //connexion TCP avec le voisin

  int socketVoisin = socket(PF_INET, SOCK_STREAM, 0);

  bind(socketVoisin, NULL, 0);

  connect(socketVoisin, (struct sockaddr*)&adresse_voisin, sizeof(adresse_voisin));

  send(socketVoisin, &msgInitial, sizeof(msgInitial), 0);

  printf("Client : message initial envoyé au voisin\n");

  if(listen(ds, 10) < 0){
    perror("erreur de listen");
  }

  int initialMessageReceived = 0;
  while(!initialMessageReceived){
    struct Message msgRecu;

    //acpeter une connexion
    int socketClient = accept(ds, NULL, NULL);
    if(socketClient < 0){
      perror("erreur d'accept");
      continue;
    }

    //recevoir un message
    int rcv = recv(socketClient, &msgRecu, sizeof(msgRecu), 0);
    if (rcv < 0) {
        perror("Client : erreur réception message :");
        continue;
    }
    if(msgRecu.idClient == id) {
        initialMessageReceived = 1;
        printf("Client : message reçu du voisin : idClient=%d, counter=%d\n", msgRecu.idClient, msgRecu.counter);
    }
    else if(!initialMessageReceived) {
        msgRecu.counter++;
        printf("Client : message reçu du voisin : idClient=%d, counter=%d\n", msgRecu.idClient, msgRecu.counter);
        send(socketVoisin, &msgRecu, sizeof(msgRecu), 0);
        printf("Client : message renvoyé au voisin : idClient=%d, counter=%d\n", msgRecu.idClient, msgRecu.counter);
    }
  printf("Client : je termine\n");
  close(ds);
  return 0;
}}