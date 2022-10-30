package com.drawing.projet;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

import static com.drawing.utils.Utils.SERVER_PORT;

public class Serveur {
    private static Logger logger = Logger.getLogger(Serveur.class.getName());
    private static Serveur serveur;
    private static ServerSocket serverSocket =null;
    public static void main(String args[]) throws IOException
    {
        serveur = new Serveur();
        logger.info("===================Demmarage du serveur====================");
        logger.info("port d'ecoute du serveur : " + SERVER_PORT);
        logger.info("Serveur à l'ecoute");
        logger.info("===================FIN Demmarage du serveur=================");

        try {
            serverSocket = new ServerSocket(SERVER_PORT);
            while(true){
                Socket client = serverSocket.accept();
                Commun commun = new Commun(client, serveur);
                commun.start();
                if(commun.closeServer()) {
                    break;
                }
                logger.info("connecté");
            }
            logger.info("fermer");
        }   catch(Exception e){
            logger.info(e.getMessage());
        }

        logger.info("Aucun client n'est connecté le serveur est arreté");
        System.exit(0);
    }
    //====================deconnection=========================//
    synchronized public void fermer() throws IOException {
        serverSocket.close();
        logger.info("fermer");
    }

}
