package com.drawing.projet;

import com.drawing.entity.Formes;
import com.drawing.vue.DrawingWindow;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Logger;

public class Commun extends Thread{

    private static Logger logger = Logger.getLogger(Commun.class.getName());
    private InputStream in = null;
    private OutputStream out = null;
    private Socket m_client;
    public static  Vector<DrawingWindow> m_connecte = new Vector<DrawingWindow>();
    private static Vector<Socket> tabClients = new Vector<Socket>();
    private static Vector<Formes> ensFormes = new Vector<Formes>();
    public static Map<Socket,Long> clientAttente = new HashMap<Socket, Long>();
    private static Vector<Socket> Client_main = new Vector<Socket>();
    public static boolean dessin=true;
    public int n_client;
    int nbclient=0;
    public Serveur ser;
    public boolean close =false;
    public Commun(Socket client, Serveur serveur)
    {
        m_client = client;
        ser = serveur;
        try{
            setnbrclient(m_client);
            in = m_client.getInputStream();
            out = m_client.getOutputStream();
            nbclient();
            if(!ensFormes.isEmpty())
            {
                envoieFormes(m_client);
            }

        }catch(Exception e){e.printStackTrace();}
    }

    public void run()
    {
        try {
            receptionMsg();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public int receive(DrawingWindow f)
    {
        m_connecte.addElement(f);
        return m_connecte.size();
    }
    //==================envoie nbclient======================//
    synchronized public void nbclient(){
        int k = tabClients.size();
        String s = " "+k;
        Formes frm=new Formes(s, DrawingWindow.pinceau, 0, 0, 0,
                0);
        for(int i=0;i<tabClients.size();++i)
        {
            OutputStream out = null;
            try {
                out = tabClients.elementAt(i).getOutputStream();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            if (out != null)
            {
                ObjectOutputStream oos = null;
                try {
                    oos = new ObjectOutputStream(out);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                try {
                    oos.writeObject(frm);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }
    //=====================ajout formes====================//
    synchronized public void ajoutFormes(Formes f)
    {
        ensFormes.addElement(f);
    }
    //=============================envoie des formes à un nouveau client==========//
    synchronized public void envoieFormes(Socket s) throws IOException
    {
        for(int i=0; i<ensFormes.size(); ++i)
        {
            OutputStream out = s.getOutputStream();
            if(out != null)
            {
                ObjectOutputStream oos = new ObjectOutputStream(out);
                oos.writeObject(ensFormes.elementAt(i));
            }

        }
    }
    //======================ajout Client en attente================//
    synchronized public void ajoutClient(Socket client, Long l)
    {
        clientAttente.put(client, l);
    }
    //========================voir s'il ya un client qui dessine==================//
    synchronized public boolean est_ce_ClientDessine(Socket s)
    {
        if(dessin)
        {
            System.out.println(dessin);
            Client_main.addElement(s);
        }

        return dessin;


    }
    //========================client dessiner===========//
    synchronized public void dessine()
    {
        dessin=false;
    }

    //===========================disponible===========================//
    synchronized public void dispo()
    {
        if(!dessin)
        {
            dessin=true;
        }
    }
    //=========================parcours client connecter======================//
    synchronized public Socket choisirClient()
    {
        Long t=System.currentTimeMillis();

        Socket sock=null;
        for(Map.Entry<Socket, Long> entry : clientAttente.entrySet()) {
            Socket cle = entry.getKey();
            Long valeur = entry.getValue();
            if(t>valeur)
            {
                t=valeur;
                sock=cle;
            }
        }
        //Map<Socket, Long> map = new ConcurrentHashMap<Socket, Long>();
        Socket s=null;
        for (Socket key : clientAttente.keySet()) {
            if (clientAttente.get(key).longValue()==t) {
                s=key;

            }
        }
        clientAttente.remove(s);
        return sock;
    }
    //===============envoie forme à tous les client=============//
    synchronized public void sendObject(Formes obj) throws IOException
    {
        for(int i=0; i<tabClients.size();++i)
        {
            OutputStream out = tabClients.elementAt(i).getOutputStream();
            if(i!=tabClients.indexOf(m_client))
            {
                if (out != null)
                {
                    ObjectOutputStream oos = new ObjectOutputStream(out);
                    oos.writeObject(obj);
                }
            }

        }

    }

    synchronized  public void setnbrclient(Socket s)
    {
        tabClients.addElement(s);


    }

    void receptionMsg() throws IOException
    {


        while(true)
        {
			/*
			if(tabClients.size()==5)
			{
				break;
			}
			*/
            try{
                ObjectInputStream tmpIn = new ObjectInputStream(in);
                Formes obj = (Formes)tmpIn.readObject();
                System.out.println(tabClients.size());
                if(obj.getType().equals("QUIT"))
                {
                    OutputStream out = m_client.getOutputStream();
                    if (out != null)
                    {ObjectOutputStream oos = new ObjectOutputStream(out);
                        oos.writeObject(obj);
                    }

                    tabClients.remove(tabClients.indexOf(m_client));

                }
                else
                {
                    if(obj.getType().equals("Dessiner"))
                    {

                        if(!est_ce_ClientDessine(m_client))
                        {
                            ajoutClient(m_client, System.currentTimeMillis());
                            OutputStream out = Client_main.elementAt(0).getOutputStream();
                            if (out != null)
                            {
                                ObjectOutputStream oos = new ObjectOutputStream(out);
                                obj.set_Type("Demande");
                                oos.writeObject(obj);
                            }

                            OutputStream out1 = m_client.getOutputStream();
                            Formes obj1=obj;
                            if (out1 != null)
                            {
                                ObjectOutputStream oos = new ObjectOutputStream(out1);
                                obj1.set_Type("criser");
                                oos.writeObject(obj1);
                            }
                        }
                        else
                        {
                            dessine();
                            System.out.println("client dessine "+n_client);
                            OutputStream out = m_client.getOutputStream();
                            if (out != null)
                            {
                                ObjectOutputStream oos = new ObjectOutputStream(out);
                                oos.writeObject(obj);
                            }
                        }
                    }
                    else
                    {
                        if(obj.getType().equals("Ceder"))
                        {
                            OutputStream out = Client_main.elementAt(0).getOutputStream();
                            if (out != null)
                            {
                                ObjectOutputStream oos = new ObjectOutputStream(out);
                                obj.set_Type("Ceder");
                                oos.writeObject(obj);
                            }

                            Client_main.remove(0);

                            Formes obj2 =obj;

                            System.out.println("hhhhh");
                            Socket s = choisirClient();
                            Client_main.addElement(s);
                            if(clientAttente.size()!=0)
                            {
                                obj2.set_Type("Demande");
                            }
                            else
                            {
                                obj2.set_Type("Dessiner");
                            }
                            OutputStream out2 = s.getOutputStream();
                            if (out2 != null)
                            {
                                ObjectOutputStream oos = new ObjectOutputStream(out2);
                                oos.writeObject(obj);
                            }

                        }
                        else
                        {
                            if(obj.getType().equals("a_la_main"))
                            {
                                OutputStream out = m_client.getOutputStream();
                                if (out != null)
                                {
                                    ObjectOutputStream oos = new ObjectOutputStream(out);
                                    oos.writeObject(obj);
                                }

                                if(clientAttente.size()>0)
                                {
                                    Socket s = choisirClient();
                                    tabClients.remove(m_client);
                                    Client_main.remove(0);
                                    Client_main.addElement(s);
                                    if(clientAttente.size()>=1)
                                    {
                                        obj.set_Type("Demande");
                                    }
                                    else
                                    {
                                        obj.set_Type("Dessiner");
                                    }
                                    OutputStream out2 = s.getOutputStream();
                                    if (out2 != null)
                                    {
                                        ObjectOutputStream oos = new ObjectOutputStream(out2);
                                        oos.writeObject(obj);
                                    }

                                }
                                tabClients.remove(tabClients.indexOf(m_client));
                            }
                            else
                            {
                                if(obj.getType().equals("sortir"))
                                {

                                    Client_main.remove(0);
                                    dispo();
                                    OutputStream out2 = m_client.getOutputStream();
                                    if (out2 != null)
                                    {
                                        ObjectOutputStream oos = new ObjectOutputStream(out2);
                                        oos.writeObject(obj);
                                    }
                                    tabClients.remove(tabClients.indexOf(m_client));
                                }
                                else
                                {
                                    if(obj.getType().equals("sortir_Att"))
                                    {
                                        OutputStream out2 = m_client.getOutputStream();
                                        if (out2 != null)
                                        {
                                            ObjectOutputStream oos = new ObjectOutputStream(out2);
                                            oos.writeObject(obj);
                                        }

                                        if(clientAttente.size()==1)
                                        {
                                            OutputStream out3 = Client_main.elementAt(0).getOutputStream();
                                            if (out3 != null)
                                            {
                                                Formes obj3 =obj;
                                                obj3.set_Type("Dessiner");
                                                ObjectOutputStream oos = new ObjectOutputStream(out3);
                                                oos.writeObject(obj3);
                                            }
                                        }

                                        tabClients.remove(tabClients.indexOf(m_client));
                                        System.out.println("taille avant ="+clientAttente.size());
                                        clientAttente.remove(m_client);
                                        System.out.println("taille supp ="+clientAttente.size());
                                    }
                                    else
                                    {
                                        if(obj.getType().equals("supp_Dessin"))
                                        {
                                            ensFormes.remove(obj.get_x());
                                            sendObject(obj);
                                        }
                                        else
                                        {
                                            ajoutFormes(obj);
                                            sendObject(obj);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }


                nbclient();
                if(closeServer())
                {
                    System.out.println("fermeture");
                    break;
                }
            }catch(Exception e){}

        }
        in.close();
        out.close();
        ser.fermer();

    }
    //========================fermeture de socket=====================//;
    synchronized boolean closeServer()
    {
        boolean isClosed = false;
        if(tabClients.size()==0)
        {
           logger.info("Fermeture du serveur");
            isClosed = true;
            close = true;
        }
        return isClosed;
    }

}