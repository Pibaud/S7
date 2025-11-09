#include <stdlib.h>
#include <stdio.h>
#include <sys/socket.h>
#include <unistd.h>
#include <arpa/inet.h>



int main(int argc, char* argv[]){
    
    if (argc!=5){
        printf("Signature correcte: %s <index> <port> <ip> <portTCP>\n",argv[0]);
        exit(EXIT_FAILURE);
    }
    
    
    int index = atoi(argv[1]);
    int targetPort = atoi(argv[2]);
    int clientPort= atoi(argv[4]);
    char* targetIp = argv[3];

//Structure échangée
struct messinfo {
    int Pi;
    int count;

};


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

//Attendre le début
struct sockaddr_in adresseVoisin;


socklen_t len = sizeof(struct sockaddr_in);
recvfrom(sockClient, &adresseVoisin, sizeof(adresseVoisin), 0, (struct sockaddr*) &servAddr, &len);

printf("J'ai l'adresse du voisin.\n");

close(sockClient);


//Connection TCP avec mon voisin

int sockAcceptation = socket(AF_INET,SOCK_STREAM,0);
struct sockaddr_in addrAcceptation;
addrAcceptation.sin_family = AF_INET;
addrAcceptation.sin_addr.s_addr = INADDR_ANY; // listen on all interfaces
addrAcceptation.sin_port = htons(clientPort);


if (bind(sockAcceptation,(struct sockaddr*) &addrAcceptation,sizeof(addrAcceptation))==-1){
    perror("bind voisin acceptation crash\n");
    exit(EXIT_FAILURE);
}



int sockVoisin = socket(AF_INET,SOCK_STREAM,0);




if (index==0){
    //le premier se connecte d'abord
    connect(sockVoisin,(struct sockaddr*)&adresseVoisin,sizeof(struct sockaddr_in));
    printf("connecté au voisin\n");
}

listen(sockAcceptation,5);
int sockPrecedent = accept(sockAcceptation,NULL,NULL);




if (index!=0){
    //les autres se connectent après
    connect(sockVoisin,(struct sockaddr*)&adresseVoisin,sizeof(struct sockaddr_in));
    printf("connecté au voisin\n");
}

struct messinfo envoi = {index,0};

send(sockVoisin,&envoi,sizeof(struct messinfo),0);

while (1){

    recv(sockPrecedent,&envoi,sizeof(struct messinfo),0);
    envoi.count++;
    send(sockVoisin,&envoi,sizeof(struct messinfo),0);
    printf("--envoi--\n");
    if (envoi.Pi==index){break;}
}
printf("%d\n",envoi.count);

close(sockClient);
}