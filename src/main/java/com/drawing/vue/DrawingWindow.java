package com.drawing.vue;

import com.drawing.entity.Formes;
import com.drawing.projet.Client;
import com.drawing.utils.ButtonsConstants;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.logging.Logger;

import static com.drawing.utils.Utils.*;

public class DrawingWindow extends JFrame implements ActionListener, WindowListener {

    private static Logger logger = Logger.getLogger(DrawingWindow.class.getName());
    public static boolean rectangle = false;
    public static boolean ellipse = false ;
    public static boolean forme = false;
    public static boolean texte = false;
    public static boolean polygone = false;
    public static boolean ligne = false;
    public static boolean supprimer =false;
    public static boolean quit =false;
    public static boolean connect =false;

    public boolean mouseDown;
    private JLabel labelCouleur;

    /*------------Buttons-----------*/
    private JButton btnEllipse;
    private JButton btnRectangle;
    private JButton btnForme;
    private JButton btnText;
    private JButton btnLigne;
    private JButton btnExit;
    public static JButton btnDessiner;
    public static JButton btnEnregistre;
    public static JButton restaurer;
    public static JButton gomme;

    private JButton red;
    private JButton jaune;
    private JButton vert;
    private JButton bleu;
    private JButton noir;
    private JButton violet;
    private JButton actif;
    public static JButton exit;
    public static JButton ceder;
    public static JLabel nbclient= new JLabel("0");
    /*-----------Panels-------------*/
    private ZoneDessin zoneDessin;
    private JPanel topPanel;
    private JPanel rightPanel;
    private JPanel leftPanel;

    /*-----------points--------------*/
    private Point origine;
    private Point extremite;


    /*-----------couleur Pinceau-------*/
    public static Color pinceau;
    private  JScrollPane scrollPane;

    public static Client client;

    public DrawingWindow() {
        this.initialisation();
        origine=new Point();
        extremite=new Point();
    }

    public void initialisation() {
        pinceau = new Color(0,0,0);
        mouseDown=false;
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        labelCouleur = new JLabel("Active");
        /*-------------creation des boutons-------------*/
        btnExit = new JButton("Exit");
        btnEllipse = new JButton("Ellipse");
        btnDessiner = new JButton("Dessiner");
        btnForme= new JButton("Forme");
        btnRectangle = new JButton("Rectangle");
        btnText = new JButton("Texte");
        btnLigne = new JButton("Ligne");
        btnEnregistre = new JButton(new ImageIcon(IMAGE_PATH + "enregistrer.png"));
        restaurer = new JButton(new ImageIcon(IMAGE_PATH + "restaurer.jpg"));
        gomme =  new JButton(new ImageIcon(IMAGE_PATH + "gomme.jpg"));

        /*--------------taille des boutons-------------*/
        btnExit.setMaximumSize(new Dimension(70, 20));
        btnEllipse.setMaximumSize(new Dimension(70, 20));
        btnDessiner.setMaximumSize(new Dimension(70, 20));
        btnForme.setMaximumSize(new Dimension(70, 20));
        btnRectangle.setMaximumSize(new Dimension(70, 20));
        btnText.setMaximumSize(new Dimension(70, 20));
        btnLigne.setMaximumSize(new Dimension(70, 20));
        btnEnregistre.setMaximumSize(new Dimension(20,20));
        btnEnregistre.setPreferredSize(new Dimension(20,20));
        restaurer.setPreferredSize(new Dimension(20,20));
        gomme.setPreferredSize(new Dimension(20,20));

        /*------------------- boutons couleurs------------------*/
        red=new JButton();
        red.setName(ButtonsConstants.RED);
        jaune = new JButton();
        jaune.setName(ButtonsConstants.JAUNE);
        vert=new JButton();
        vert.setName(ButtonsConstants.VERT);
        bleu = new JButton();
        bleu.setName(ButtonsConstants.BLEU);
        noir = new JButton();
        noir.setName(ButtonsConstants.NOIR);
        violet = new JButton();
        violet.setName(ButtonsConstants.VIOLET);
        actif = new JButton();
        actif.setName(ButtonsConstants.ACTIF);
        exit = new JButton(new ImageIcon(IMAGE_PATH + "deconnexion.png"));
        exit.addActionListener(this);
        exit.setPreferredSize(new Dimension(25,25));
        exit.setMaximumSize(new Dimension(25,25));

        /*-------------------  gestion de la main------------------*/
        ceder = new JButton();
        ceder.addActionListener(this);
        ceder.setEnabled(false);

        /*------------------- couleurs des boutons -----------------*/
        red.setBackground(new Color(255,0,0));
        jaune.setBackground(new Color(255,255,0));
        vert.setBackground(new Color(0,255,255));
        bleu.setBackground(new Color(0,0,255));
        noir.setBackground(new Color(0,0,0));
        violet.setBackground(new Color(255, 0, 255));
        actif.setBackground(new Color(0,0,0));

        /*------------------- dimmensions des boutons -----------------*/
        red.setPreferredSize(new Dimension(30,30));
        jaune.setPreferredSize(new Dimension(30,30));
        vert.setPreferredSize(new Dimension(30,30));
        bleu.setPreferredSize(new Dimension(30,30));
        noir.setPreferredSize(new Dimension(30,30));
        violet.setPreferredSize(new Dimension(30,30));
        actif.setPreferredSize(new Dimension(30,30));

        /*------------- creation des panels-------------*/
        topPanel = new JPanel();
        leftPanel = new JPanel();
        rightPanel =  new JPanel();
        zoneDessin=new ZoneDessin();

        scrollPane = new JScrollPane(zoneDessin);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setBounds(zoneDessin.getBounds());

        topPanel.setLayout(new FlowLayout(0));
        topPanel.add(btnEllipse);
        topPanel.add(btnForme);
        topPanel.add(btnRectangle);
        topPanel.add(btnText);
        topPanel.add(btnLigne);

        JLabel espacement = new JLabel();
        JLabel espacement1 = new JLabel();
        JLabel espacement2 = new JLabel();
        JLabel espacement3 = new JLabel();
        JLabel espacement4 = new JLabel();
        JLabel client = new JLabel("Connecter: ");
        //JLabel nbclient = new JLabel("0");
        espacement.setPreferredSize(new Dimension(80,20));
        espacement1.setPreferredSize(new Dimension(50,20));
        espacement2.setPreferredSize(new Dimension(10,20));
        espacement3.setPreferredSize(new Dimension(10,20));
        espacement4.setPreferredSize(new Dimension(10,20));

        topPanel.add(espacement);
        topPanel.add(btnDessiner);
        topPanel.add(espacement1);
        topPanel.add(btnEnregistre);
        topPanel.add(espacement2);
        topPanel.add(restaurer);
        topPanel.add(espacement3);
        topPanel.add(gomme);
        topPanel.add(espacement4);
        topPanel.add(client);
        topPanel.add(nbclient);

        JPanel panel = new JPanel();
        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();
        panel2.setLayout(new FlowLayout());
        panel.setLayout(new GridLayout(2, 3));

        panel.add(red);
        panel.add(jaune);
        panel.add(vert);
        panel.add(bleu);
        panel.add(noir);
        panel.add(violet);
        panel.setMaximumSize(new Dimension(250,250));

        BoxLayout layout = new BoxLayout(rightPanel, BoxLayout.PAGE_AXIS);

        //rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.PAGE_AXIS));
        rightPanel.setLayout(layout);
        rightPanel.setAlignmentX(LEFT_ALIGNMENT);

        rightPanel.add(new JLabel("Couleur"));
        rightPanel.add(panel);
        JLabel label1 = new JLabel(" ");
        rightPanel.add(label1);
        rightPanel.add(labelCouleur);
        rightPanel.add(actif);
        JLabel label2 = new JLabel(" ");
        rightPanel.add(label2);
        JLabel label3 = new JLabel("Exit");
        rightPanel.add(label3);
        rightPanel.add(exit);
        rightPanel.add(panel2);
        rightPanel.add(label2);
        zoneDessin.setBackground(new Color(255,255,255));

        this.setLayout(new BorderLayout());
        this.add(BorderLayout.WEST, leftPanel);
        this.add(BorderLayout.NORTH, topPanel);
        this.add(BorderLayout.EAST, rightPanel);
        this.add(BorderLayout.CENTER, scrollPane);

        /*--------------------Action Listner-------------*/
        btnDessiner.addActionListener(this);
        btnDessiner.setBackground(new Color(0,0,255));
        btnEllipse.addActionListener(this);
        btnExit.addActionListener(this);
        btnForme.addActionListener(this);
        btnRectangle.addActionListener(this);
        btnText.addActionListener(this);
        btnLigne.addActionListener(this);

        btnEnregistre.addActionListener(this);
        gomme.addActionListener(this);
        restaurer.addActionListener(this);

        red.addActionListener(this);
        jaune.addActionListener(this);
        vert.addActionListener(this);
        bleu.addActionListener(this);
        noir.addActionListener(this);
        violet.addActionListener(this);

        this.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e){
                JOptionPane jOptionPane = new JOptionPane();
                int userAction = jOptionPane.showConfirmDialog(null, "Voulez-vous quitter l'application",
                        "Confirmation",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE);
                if (userAction != JOptionPane.CANCEL_OPTION){
                    applicationExit();
                }
                else {
                    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
                }
            }
        });

        this.griser();
        pack();
    }

    public Color getColor(JButton b)
    {
        return actif.getBackground();
    }

    public void setRectangle() {
        rectangle = true ;
        ellipse = false ;
        forme = false;
        texte = false;
        polygone = false;
        ligne = false;
        supprimer=false;
        quit =false;
        connect=false;
    }

    public void setEllipse() {
        rectangle = false;
        ellipse = true ;
        forme = false;
        texte = false;
        polygone = false;
        ligne = false;
        supprimer=false;
        quit =false;
        connect=false;
    }

    public void setForme() {
        rectangle = false;
        ellipse = false ;
        forme = true;
        texte = false;
        polygone = false;
        ligne = false;
        supprimer=false;
        quit =false;
        connect=false;
    }

    public void seTtexte() {
        rectangle = false;
        ellipse = false ;
        forme = false;
        texte = true;
        polygone = false;
        ligne = false;
        supprimer=false;
        quit =false;
        connect=false;
    }

    public void polygone() {
        rectangle = false;
        ellipse = false ;
        forme = false;
        texte = false;
        polygone = true;
        ligne = false;
        supprimer=false;
        quit =false;
        connect=false;
    }

    public void setLigne() {
        rectangle = false;
        ellipse = false ;
        forme = false;
        texte = false;
        polygone = false;
        ligne = true;
        supprimer=false;
        quit =false;
        connect=false;
    }

    public void setSupprimer() {
        rectangle = false;
        ellipse = false ;
        forme = false;
        texte = false;
        polygone = false;
        ligne = false;
        supprimer=true;
        quit =false;
        connect=false;
    }

    public void applicationExit() {

        rectangle = false;
        ellipse = false ;
        forme = false;
        texte = false;
        polygone = false;
        ligne = false;
        supprimer=false;
        quit =true;
        connect=false;

        String message = StringUtils.EMPTY;

        switch (btnDessiner.getText()) {
            case ATTENTE:
                message="sortir_Att";
                break;
            case CEDER:
                message="a_la_main";
                break;
            case ACTIF:
                message="sortir";
                break;
            case DESSINER:
                message="QUIT";
                break;
            default:
                break;
        }

        Formes formes = new Formes(message, DrawingWindow.pinceau, 0, 0, 0, 0);
        try {
            Connection.client.envoiForme(formes);
        }catch(Exception e) {
            logger.info(e.getMessage());
        }
    }

    public void sendDrawedFormesToNewlyConnectClient() {
        rectangle = false;
        ellipse = false ;
        forme = false;
        texte = false;
        polygone = false;
        ligne = false;
        supprimer=false;
        quit =false;
        connect=true;
        Formes formes = new Formes(btnDessiner.getText(), DrawingWindow.pinceau, 0, 0, 0, 0);
        try {

            Connection.client.envoiForme(formes);

        }  catch(Exception e){
            logger.info(e.getMessage());
        }

    }

    public void griser() {
        btnEllipse.setEnabled(false);
        btnForme.setEnabled(false);
        btnRectangle.setEnabled(false);
        btnLigne.setEnabled(false);
        btnText.setEnabled(false);
        btnEnregistre.setEnabled(false);
        restaurer.setEnabled(false);
        gomme.setEnabled(false);

    }

    public void degriser() {
        btnEllipse.setEnabled(true);
        btnForme.setEnabled(true);
        btnRectangle.setEnabled(true);
        btnLigne.setEnabled(true);
        btnText.setEnabled(true);
        btnEnregistre.setEnabled(true);
        restaurer.setEnabled(true);
        gomme.setEnabled(true);

    }

    //=================fermer fenetre========================//
    public void fermeture() {
        logger.info("Fermeture de l'application");
        System.exit(0);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String source = ((JButton) e.getSource()).getName();
        logger.info(source);

        switch (source) {
            case ButtonsConstants.BLEU:
                actif.setBackground(bleu.getBackground());
                pinceau=actif.getBackground();
                break;
            case ButtonsConstants.RED:
                actif.setBackground(red.getBackground());
                pinceau=red.getBackground();
                break;

        }

        if(e.getSource()==bleu)
        {
            actif.setBackground(bleu.getBackground());
            pinceau=actif.getBackground();
        }
        if(e.getSource()==red)
        {
            actif.setBackground(red.getBackground());
            pinceau=red.getBackground();
        }
        if(e.getSource()==jaune)
        {
            actif.setBackground(jaune.getBackground());
            pinceau=jaune.getBackground();
        }
        if(e.getSource()==vert)
        {
            actif.setBackground(vert.getBackground());
            pinceau=vert.getBackground();
        }
        if(e.getSource()==noir)
        {
            actif.setBackground(noir.getBackground());
            pinceau=noir.getBackground();
        }
        if(e.getSource()==violet)
        {
            actif.setBackground(violet.getBackground());
            pinceau=violet.getBackground();
        }

        if(e.getSource()==btnEllipse)
        {
            this.setEllipse();
        }

        if(e.getSource()==btnForme)
        {
            this.setForme();
        }
        if(e.getSource()==btnLigne)
        {
            this.setLigne();
        }
        if(e.getSource()==btnRectangle)
        {
            this.setRectangle();
        }
        if(e.getSource()==btnText)
        {
            this.seTtexte();
        }
        if(e.getSource()==gomme)
        {
            this.setSupprimer();
        }

        if(e.getSource()==btnEnregistre)
        {
            try{
                zoneDessin.sauvegarder();
            }catch(Exception ee)
            {
                ee.printStackTrace();
            }
        }
        if(e.getSource()==restaurer)
        {
            try{
                zoneDessin.restaurer("formes.objet");

            }catch(Exception ee)
            {
                ee.printStackTrace();
            }
        }
        if(e.getSource()==exit)
        {
            this.applicationExit();
            //this.setVisible(false);
        }
        if(e.getSource()==btnDessiner)
        {
            zoneDessin.message=btnDessiner.getLabel();
            this.sendDrawedFormesToNewlyConnectClient();
        }
    }

    public void ceder() {
        btnDessiner.setText("Actif");
        btnDessiner.setBackground(new Color(0,255,255));
        btnDessiner.setEnabled(false);
    }

    public ZoneDessin getZoneDessin() {
        return zoneDessin;
    }

    @Override
    public void windowOpened(WindowEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void windowClosing(WindowEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void windowClosed(WindowEvent e) {
        // TODO Auto-generated method stub
    }

    @Override
    public void windowIconified(WindowEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void windowDeiconified(WindowEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void windowActivated(WindowEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void windowDeactivated(WindowEvent e) {
        // TODO Auto-generated method stub

    }

    /*------------------main----------------------*/
    public static void main(String[] args) {
        DrawingWindow drawingWindow = new DrawingWindow();
        drawingWindow.setSize(800, 600);
        drawingWindow.setTitle("Interface de Dessin 1.0");
        drawingWindow.setVisible(true);
    }

}