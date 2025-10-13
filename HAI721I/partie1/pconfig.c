#include <stdlib.h>
#include <stdio.h>
#include <sys/socket.h>
#include <unistd.h>
#include <arpa/inet.h>
#include <string.h>






int main(int argc, char* argv[]){//Premier arg: port, 2e arg: nb PI

if (argc!=3){
     printf("Signature correcte: %s <port> <nombre de processus>\n",argv[0]);
    exit(EXIT_FAILURE);
}

//init variables
int port = atoi(argv[1]);
int nbPi = atoi(argv[2]);
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
servAddr.sin_port = htons(port);

if (bind(sockServer,(struct sockaddr*) &servAddr,sizeof(servAddr))==-1){
    perror("bind crash\n");
    exit(EXIT_FAILURE);
}

// Attendre les x processus
int receivedProcesses = 0;
struct sockaddr_in currentAddress;

while (receivedProcesses < nbPi) {
    int ind;
    socklen_t addrlen = sizeof(struct sockaddr_in); // <-- ADD THIS
    recvfrom(sockServer, &ind, sizeof(int), 0, (struct sockaddr *) &currentAddress, &addrlen); 
    memcpy(&PIaddresses[ind], &currentAddress, sizeof(struct sockaddr_in));
    printf("Process reçu:%d\n", ind);
    receivedProcesses++;
}

printf("Tous les clients ont signalé leur existence\n");



for (int i=0;i<nbPi;i++){
    currentAddress= PIaddresses[i];
    struct sockaddr_in nextAdress = PIaddresses[(i+1)%nbPi];

    sendto(sockServer,&nextAdress,sizeof(struct sockaddr_in),0,(struct sockaddr*) &currentAddress,sizeof(struct sockaddr_in));

}




printf("FINI\n");
close(sockServer);







}
