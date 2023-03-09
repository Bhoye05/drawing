package com.drawing.projet;

import com.drawing.entity.Formes;
import com.drawing.vue.DrawingWindow;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class Client implements Runnable{
    public static int s_num;
    public static int s_port=5555;

    private Socket socket = null;
    private InputStream in = null;
    private OutputStream out=null;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    public static DrawingWindow fentre=null;
    public static boolean isCanceled =false;
    private Thread t ;

    public Client(DrawingWindow f) {
        // TODO Auto-generated constructor stub
        fentre = f;

        try{
            socket = new Socket(InetAddress.getLocalHost(),s_port);
            out = socket.getOutputStream();
            in = socket.getInputStream();
        }catch(Exception e)
        {
            e.getStackTrace();
        }
    }
    public void connecter()
    {
        t = new Thread(this);
        t.start();
    }

    public void run()
    {
        try{
            while(true)
            {
                if(isCanceled) {
                    break;
                }

                ois = new ObjectInputStream(in);
                Formes formes = (Formes)ois.readObject();

                if(formes.getType().equals("QUIT")) {
                    isCanceled = true;
                    fentre.fermeture();

                } else {
                    if(formes.getType().equals("Dessiner")) {
                        fentre.degriser();
                        fentre.ceder();
                    }
                    else
                    {
                        if(formes.getType().equals("Demande"))
                        {
                            fentre.btnDessiner.setEnabled(true);
                            fentre.btnDessiner.setBackground(new Color(255,0,0));
                            fentre.btnDessiner.setText("Ceder");
                            fentre.degriser();

                        }
                        else
                        {
                            if(formes.getType().equals("Ceder"))
                            {
                                fentre.btnDessiner.setText("Dessiner");
                                fentre.griser();
                                fentre.btnDessiner.setBackground(new Color(0,0,255));


                            } else {
                                if(formes.getType().equals("griser")) {
                                    fentre.btnDessiner.setText("Attente");
                                    fentre.btnDessiner.setEnabled(false);
                                } else {
                                    if(formes.getType().equals("a_la_main")) {
                                        isCanceled =true;
                                        //fentre.dispose();
                                        fentre.fermeture();
                                    } else {
                                        if(formes.getType().equals("sortir"))
                                        {
                                            isCanceled =true;
                                            //fentre.dispose();
                                            fentre.fermeture();

                                        }
                                        else
                                        {
                                            if(formes.getType().equals("sortir_Att"))
                                            {
                                                isCanceled =true;
                                                //fentre.dispose();
                                                fentre.fermeture();
                                            }
                                            else
                                            {
                                                if(formes.getType().equals("supp_Dessin"))
                                                {
                                                    fentre.getZoneDessin().supprimerForme(fentre.getZoneDessin().getEnsembleForme().elementAt(formes.get_x()));
                                                    fentre.getZoneDessin().repaint();
                                                }
                                                else
                                                {
                                                    if(formes.getType().charAt(0)==' ')
                                                    {
                                                        fentre.nbclient.setText(formes.getType());
                                                    }
                                                    else
                                                    {
                                                        fentre.getZoneDessin().ajoutForme(formes);
                                                        fentre.getZoneDessin().repaint();
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }

                            }
                        }
                    }
                }

            }

            in.close();
            out.close();
            oos.close();
            ois.close();
            socket.close();

        }catch (Exception e)
        {
            fentre.setVisible(false);
            JOptionPane dia = new JOptionPane();
            dia.showMessageDialog(null,"Le serveur n'est pas lancé", "Accès refusé", JOptionPane.INFORMATION_MESSAGE,new ImageIcon("erreur.jpg"));
            fentre.fermeture();
            e.printStackTrace();
        }

        try {
            t.stop();
            System.exit(0);
        } catch (Exception e) {
            // TODO Auto-generated catch block

            e.printStackTrace();
        }

    }

    synchronized public void envoiForme(Formes f)throws Exception
    {
        out = socket.getOutputStream();
        oos = new ObjectOutputStream(out);
        oos.writeObject(f);


    }
    public void deconnection() throws IOException
    {

        oos.close();
        ois.close();
        in.close();
        out.close();
        socket.close();

    }

}