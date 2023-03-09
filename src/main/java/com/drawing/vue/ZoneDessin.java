package com.drawing.vue;

import com.drawing.entity.Formes;
import org.apache.commons.lang3.StringUtils;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Vector;
import java.util.logging.Logger;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class ZoneDessin extends JPanel{

	public static final String ENTREZ_TEXTE = "Veuillez entrez le texte à Saisir !";
	public static final String ELLIPSE = "ellipse";
	private static final Logger logger = Logger.getLogger(ZoneDessin.class.getName());
	public static final String RECTANGLE = "rectangle";
	public static final String LIGNE = "ligne";
	public static final String FORME = "forme";
	public static final String TEXTE = "texte";

	private Point origine;
	private Point extremite;
	private Vector<Formes> ensembleForme;
	private int indiceForme;
	public static String message= StringUtils.EMPTY;
	
	/*--------------------Constructeur--------------------*/
	public ZoneDessin() {
		initialisation();
		this.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e){
					origine = new Point(e.getX(), e.getY());
			}

			public void mouseClicked (MouseEvent e) {
				if(DrawingWindow.supprimer) {
					Point point = new Point(e.getX(), e.getY());
					boolean supprimer = contienPoint(point);
					if(supprimer) {
						supprimerForme(ensembleForme.elementAt(indiceForme));
						repaint();
					}

					Formes formeToDelete = getFormeToDelete(point);

					try {
						Connection.client.envoiForme(formeToDelete);
					} catch(Exception ee) {
						ee.printStackTrace();
					}
					repaint();
				}
				
				Point mousePoint =new Point(e.getX(), e.getY());

				if(DrawingWindow.texte) {
				    origine=mousePoint;
				    Formes formeToDraw = new Formes(TEXTE, DrawingWindow.pinceau, origine.x, origine.y, 80, 50);
				    String texteASaisir = JOptionPane.showInputDialog(null, ENTREZ_TEXTE, "SAISIE !",
							JOptionPane.QUESTION_MESSAGE);

					if(StringUtils.isNoneBlank(texteASaisir)) {
						formeToDraw.texteAecrire = texteASaisir;
					}
				    ensembleForme.addElement(formeToDraw);
				    try{
				    	Connection.client.envoiForme(formeToDraw);
					}catch(Exception ee){ ee.printStackTrace();}
				    repaint();
				}
			
			}
			
			public void mouseReleased(MouseEvent e) {
				extremite = new Point(e.getX(), e.getY());
				if(DrawingWindow.rectangle) {
					Formes f = new Formes(RECTANGLE, DrawingWindow.pinceau, origine.x, origine.y, extremite.x-origine.x,
							               extremite.y-origine.y);					
					
					ensembleForme.addElement(f);
					try{
						Connection.client.envoiForme(f);
					}	catch(Exception ee) {
						ee.printStackTrace();
					}
				}
				if(DrawingWindow.ellipse) {
					Formes f = new Formes(ELLIPSE, DrawingWindow.pinceau, origine.x, origine.y, extremite.x-origine.x,
							               extremite.y-origine.y);
					ensembleForme.addElement(f);
					try{
						Connection.client.envoiForme(f);
					}catch(Exception ee){ ee.printStackTrace();}
					
				}
				if(DrawingWindow.ligne) {
					Formes f=new Formes(LIGNE, DrawingWindow.pinceau, origine.x, origine.y, extremite.x,
							               extremite.y);
					ensembleForme.addElement(f);
					try{
						Connection.client.envoiForme(f);
						}catch(Exception ee){ ee.printStackTrace();}
				}
				
				repaint();
				System.out.println("client");
				
			}
		});
		
		this.addMouseMotionListener(new MouseMotionListener() {
			
			@Override
			public void mouseMoved(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseDragged(MouseEvent e) {
				// TODO Auto-generated method stub
				Point p = new Point(e.getX(), e.getY());				
				if(DrawingWindow.forme)
				{
					extremite=p;
					Formes f = new Formes(FORME, DrawingWindow.pinceau, origine.x, origine.y,extremite.x, extremite.y);
					ensembleForme.addElement(f);
					try{
						Connection.client.envoiForme(f);
						}catch(Exception ee){ ee.printStackTrace();}
					origine=extremite;
				}
				repaint();
			}
		});
	}
	
	/*-------------------Methode de sauvegarde----------------*/
	public void sauvegarder()throws IOException {
		File fichier = new File ("formes.objet");
		File nbForme = new File("nbObjets");
		ObjectOutputStream filetosaveOutputStream = new ObjectOutputStream (new FileOutputStream (fichier));
		ObjectOutputStream nbFormeOutputStream = new ObjectOutputStream (new FileOutputStream (nbForme));
		nbFormeOutputStream.write(ensembleForme.size());
		for(Formes formeToSave : ensembleForme) {
			filetosaveOutputStream.writeObject(formeToSave);
		}

		filetosaveOutputStream.close();
		nbFormeOutputStream.close();
	}
	
	/*------------------Methode de restauration---------------*/
	public void restaurer(String filename)throws Exception {
		 ObjectInputStream fileInputStream = new ObjectInputStream (new FileInputStream (filename));
		 ObjectInputStream nbObjectsInputStream =new ObjectInputStream(new FileInputStream ("nbObjets"));

		 int size =  nbObjectsInputStream.read();
		 for(int i=0; i < size; i++) {
			 Formes formeToRestore= (Formes) fileInputStream.readObject();
			 Connection.client.envoiForme(formeToRestore);
			 this.ajoutForme(formeToRestore);
		 }

		 repaint();
		 fileInputStream.close();
		 nbObjectsInputStream.close();
	}	
	
	
	/*-------------------Supprimer un dessin----------------*/
	public boolean contienPoint(Point p) {
		boolean included =false;
		int i=0;
		while( i < ensembleForme.size() && !isInclude(p, ensembleForme.elementAt(i))) {
			i++;
		}
		if(i < ensembleForme.size()) {
			indiceForme = i;
			included = true;
		}
		return included;
	}

	public boolean isInclude(Point p, Formes forme) {
		return (forme.get_x() < p.getX() && forme.get_y() < p.getY() &&
				forme.get_x() + forme.get_h() > p.getX()  && forme.get_y() + forme.get_w() > p.getY());
	}

	public Formes getFormeToDelete(Point mouseClickpoint) {
		Formes formeToReturn = new Formes("supp_Dessin", DrawingWindow.pinceau, indiceForme, 0, 0, 0);
		for (Formes forme : ensembleForme) {
			if (isInclude(mouseClickpoint, forme)) {
				formeToReturn = forme;
				break;
			}
		}
		return formeToReturn;
	}
	
	
	/*----------------------Initialisation------------------*/
	public void initialisation() {
		logger.info("Initialisation");
		this.setBackground(new Color(255,255,255));
		origine = new Point();
		extremite = new Point();
		ensembleForme = new Vector<>();
	}

	/*------------------getters et setters-------------------*/
	public Vector<Formes> getEnsembleForme() {
		return ensembleForme;
	}
	
	
	public void ajoutForme(Formes f) {
		ensembleForme.addElement(f);
	}
	
	public void supprimerForme(Formes f) {
		ensembleForme.remove(f);
	}
	
	/*--------------redefinition de Paincomponent-----------------*/
	@Override
	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		graphics.setFont(new Font("arial", Font.BOLD, 20));
		
		if(!ensembleForme.isEmpty()) {

			for(Formes forme : ensembleForme) {
				graphics.setColor(forme.couleur);
				switch (forme.getType()) {
					case RECTANGLE:
						graphics.drawRect(forme.get_x(), forme.get_y(), forme.get_h(), forme.get_w());
						break;
					case ELLIPSE:
						graphics.drawOval(forme.get_x(), forme.get_y(), forme.get_h(), forme.get_w());
						break;
					case LIGNE:
					case FORME:
						graphics.drawLine(forme.get_x(), forme.get_y(), forme.get_h(), forme.get_w());
						break;
					case TEXTE:
						graphics.drawString(forme.texteAecrire,forme.get_x() , forme.get_y());
						break;
					default:
						break;
				}
			}
		}
	}
}