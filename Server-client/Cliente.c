#include "Socket.h"
#include <arpa/inet.h>
#include <string.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <memory.h>
#include "Socket_Cliente.h"
#include "Socket_Servidor.h"
#include <pthread.h>
#include <jmorecfg.h>
#include <stdbool.h>

int totalPoints = 0;
int totalPointsTemporary = 0;
int totalPellets = 173;
int gSpeed = 2;
int temporaryFruit = 0;
int lives = 2;
bool dead = false;
bool start = false;

void *myThreadFun(void *);
void action(char *);
void socketServer(char *);
void createGhost(void);
void createFruit(void);
void changeSpeed(void);
void createBigPellet(void);
void menu(void);
void reset(void);
void *socketServerMini(void *);


/**
 * Es el thread para poder enviar informacion y usar el menu a la vez
 * @param vargp
 * @return no retorna
 */
void *myThreadFun(void *vargp) {
    srand(getpid());
    while (1) {
        if(start){
            char stringNum[100];
            sprintf(stringNum, "%s", "D");
            if (gSpeed == 1) {
                usleep(1000 * 400);
            } else if (gSpeed == 2) {
                usleep(1000 * 70);
            } else if (gSpeed == 3) {
                usleep(1000 * 50);
            }
            if (dead) {
                sleep(2);
                dead = false;
            }
            int num = rand() % 5;
            switch (num) {
                case 1:
                    sprintf(stringNum, "%s ", "U");
                    break;
                case 2:
                    sprintf(stringNum, "%s ", "D");
                    break;
                case 3:
                    sprintf(stringNum, "%s ", "R");
                    break;
                case 4:
                    sprintf(stringNum, "%s ", "L");
                    break;
                default:
                    break;

            }
            socketServer(stringNum);

        }

    }
}


/**
 * Se ocupa de hacer modificaciones al server con la informacion del cliente (Java)
 * @param mensaje: mensaje enviado por el cliente
 */
void action(char *mensaje){
    if (totalPointsTemporary >= 10000){
        char stringNum1[10];
        lives++;
        sprintf(stringNum1, "%s %d", "E", lives);
        socketServer(stringNum1);
        totalPoints += totalPointsTemporary;
        totalPointsTemporary = 0;

    }
    if (totalPellets == 0) {
        char stringNum2[10];
        sprintf(stringNum2, "%s ", "W");
        socketServer(stringNum2);
        return;
    }

    if (strcmp(mensaje, "P") == 0){
        char stringNum3[100];
        totalPellets--;
        totalPointsTemporary += 10;
        sprintf(stringNum3, "%s", "S");
        socketServer(stringNum3);
    } else if (strcmp(mensaje, "F") == 0) {
        totalPointsTemporary += temporaryFruit;
        temporaryFruit = 0;
        if (totalPointsTemporary >= 10000){
            char stringNum4[100];
            lives++;
            sprintf(stringNum4, "%s %d", "E", lives);
            socketServer(stringNum4);
            totalPoints += totalPointsTemporary;
            totalPointsTemporary = 0;
        }
    } else if (strcmp(mensaje, "R") == 0) {
        reset();
    } else if (strcmp(mensaje, "G") == 0) {
        char stringNum5[100];
        totalPointsTemporary += 100;
        sprintf(stringNum5, "%s", "S");
    } else if(strcmp(mensaje, "M") == 0) {
        dead = true;
        lives--;
        if (lives == -1){
            char stringNum6[100];
            sprintf(stringNum6, "%s %d", "O", lives);
            lives = 2;
            reset();
            socketServer(stringNum6);
            return;
        } else {
            char stringNum7[100];
            sprintf(stringNum7, "%s %d", "E", lives);
            socketServer(stringNum7);
        }

    } else if (strcmp(mensaje, "T") == 0){
        char stringNum8[10];
        sprintf(stringNum8, "%s ", "S");
        totalPointsTemporary += 50;

    }
}

/**
 *Le saca el largo a un char
 * @param word un char
 * @return retorna el largo del char
 */
int lenW (char *word) {
    int lenword = 0;
    for (int i=0; word[i]!='\0'; i++)
        lenword ++;
    return lenword;
}
/**
 * Reinicia el juego
 */
void reset(void) {
    temporaryFruit = 0;
    totalPoints = 0;
    gSpeed = 2;
    totalPellets = 173;
}

/**
 * Se encarla de la trasferencia de informacion entre C y Java
 * @param mensaje mensaje a enviar para java
 */
void socketServer(char *mensaje){
    int Socket_Con_Servidor;
    int lenM = lenW(mensaje) + 1;
    int Aux;

    Socket_Con_Servidor = Abre_Conexion_Inet ("192.168.100.4", "cpp_java");
    if (Socket_Con_Servidor == 1) {
        printf ("No puedo establecer conexion con el servidor\n");
        exit (-1);
    }

    Aux = htonl (lenM);
    Escribe_Socket (Socket_Con_Servidor, (char *)&Aux, sizeof(lenM));
    Escribe_Socket (Socket_Con_Servidor, mensaje, lenM);
    close (Socket_Con_Servidor);
}

/**
 * Crea al los fantasmas Blinky, Clyde, Inky y Pinky, les da las posiciones iniciales y le da
 * la velocidad inicial deseados por el administrador
 */
void createGhost(void){
    int posXB = 0, posYB, numberOfChars;
    char stringNum[100];
    char *ghost = "G";
    printf(" Posiciones para Blinky \n\n");
    printf(" Posicion 'X' \n");
    scanf("%d", &posXB);
    printf(" Posicion 'Y' \n");
    scanf("%d", &posYB);

    int posXC, posYC;
    printf(" Posiciones para Clyde \n\n");
    printf(" Posicion 'X' \n");
    scanf("%d", &posXC);
    printf(" Posicion 'Y' \n");
    scanf("%d", &posYC);

    int posXI, posYI;
    printf(" Posiciones para Inky \n\n");
    printf(" Posicion 'X' \n");
    scanf("%d", &posXI);
    printf(" Posicion 'Y' \n");
    scanf("%d", &posYI);

    int posXP, posYP;
    printf(" Posiciones para Pinky \n\n");
    printf(" Posicion 'X' \n");
    scanf("%d", &posXP);
    printf(" Posicion 'Y' \n");
    scanf("%d", &posYP);

    int speed;
    printf(" Velocidad de los fantasmas \n");
    printf(" Seleccione 1 para velocidad 'Lenta' \n");
    printf(" Seleccione 2 para velocidad 'Normal' \n");
    printf(" Seleccione 3 para velocidad 'Rapida' \n");
    scanf("%d", &speed);
    switch (speed){
        case 1:
            gSpeed = 1;
            numberOfChars = sprintf(stringNum ," %s %d %d %d %d %d %d %d %d", ghost ,posXB, posYB, posXC, posYC, posXI, posYI, posXP, posYP);
            socketServer(stringNum);
            break;
        case 2:
            gSpeed = 2;
            numberOfChars = sprintf(stringNum ," %s %d %d %d %d %d %d %d %d", ghost ,posXB, posYB, posXC, posYC, posXI, posYI, posXP, posYP);
            socketServer(stringNum);
            break;
        case 3:
            gSpeed = 3;
            numberOfChars = sprintf(stringNum ," %s %d %d %d %d %d %d %d %d", ghost ,posXB, posYB, posXC, posYC, posXI, posYI, posXP, posYP);
            socketServer(stringNum);
            break;
        default:
            break;
    }
    start = true;
    stringNum[10];
}

/**
 * Crea la fruta y le da el puntaje deseado por el administrador
 */
void createFruit(void){
    int fruit = 0, numberOfChars;
    char stringNum[100] = "";;

    numberOfChars = sprintf(stringNum, "%s ", "F");
    int score = 0;
    printf(" Seleccione el puntaje de la fruta \n");
    scanf("%d", &score);
    if (score < 0 || score > 10000){
        printf(" \nPuntaje no valido \n");
    } else {
        temporaryFruit = score;
        totalPointsTemporary = score;
    }
    numberOfChars += sprintf(stringNum+ numberOfChars, "%d ", score);
    socketServer(stringNum);
}

/**
 * Cambia la velocidad de los fantasmas de acuerdo al administrador
 */
void changeSpeed(void){
    int speed = 0;
    printf(" Velocidad de los fantasmas \n");
    printf(" Seleccione 1 para velocidad 'Lenta' \n");
    printf(" Seleccione 2 para velocidad 'Normal' \n");
    printf(" Seleccione 3 para velocidad 'Rapida' \n");
    scanf("%d", &speed);
    switch (speed){
        case 1:
            gSpeed = 1;
            break;
        case 2:
            gSpeed = 2;
            break;
        case 3:
            gSpeed = 3;
            break;
        default:
            break;
    }
}
/**
 *Crea pastiallas en el mapa, deseado por el administrador
 */
void createBigPellet(void){

    totalPellets = totalPellets + 1;
            int posX = 0, posY;
            int numberOfChars = 0;
            char stringNum[100] = "";
            numberOfChars = sprintf(stringNum, "%s ", "P");
            printf("Posicion 'X' de la pastillas \n");
            scanf("%d", &posX);
            numberOfChars += sprintf(stringNum + numberOfChars, "%d ", posX);
            printf("Posicion 'Y' de la pastillas \n");
            scanf("%d", &posY);
            numberOfChars += sprintf(stringNum + numberOfChars, "%d ", posY);
      //      cont++;
            socketServer(stringNum);
        }

    //}

/**
 * Es el que se encarga de ofrecer el menu de opciones al administrador
 * para cambiar el juego a placer
 */
void menu(void){
    while(1) {
        int opcion;
        printf("Introduzca una opcion \n");
        printf("\t1. Crear fantasmas \n");
        printf("\t2. Asingar velocidad a fantasmas \n ");
        printf("\t3. Crear fruta \n");
        printf("\t4. Crear pastilla \n");
        printf("\t5. Salir \n");
        scanf("%d", &opcion);
        switch (opcion) {
            case 1:
                createGhost();
                break;
            case 2:
                changeSpeed();
                break;
            case 3:
                createFruit();
                break;
            case 4:
                createBigPellet();
                break;
            case 5:
                printf("   Adios. \n");
                exit(-1);
            default:
                break;
        }
    }

}

void *socketServerMini(void *vargp) {
    int Socket_Servidor;
    int Socket_Cliente;
    int Aux;
    int Longitud_Cadena;
    char Cadena[100];

    Socket_Servidor = Abre_Socket_Inet("cpp_c");
    if (Socket_Servidor == -1) {
        printf("No se puede abrir socket servidor\n");
        exit(-1);
    }
    while (true) {
        Socket_Cliente = Acepta_Conexion_Cliente(Socket_Servidor);
        if (Socket_Servidor == -1) {
            printf("No se puede abrir socket de cliente\n");
            exit(-1);
        }
        Lee_Socket(Socket_Cliente, (char *) &Aux, sizeof(Longitud_Cadena));
        Longitud_Cadena = ntohl(Aux);
        Lee_Socket(Socket_Cliente, Cadena, Longitud_Cadena);
        action(Cadena);

        close(Socket_Cliente);
    }
}


int main () {
    pthread_t thread_id2;
    pthread_create(&thread_id2, NULL, socketServerMini, NULL);
    pthread_t thread_id;
    pthread_create(&thread_id, NULL, myThreadFun, NULL);
    menu();
    return 0;
}

