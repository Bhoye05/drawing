package com.drawing.vue;

import com.drawing.entity.Formes;

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

import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class ZoneDessin extends JPanel{
	
	private int nbObjetEnregistre=0;
	private Point origine;
	private Point extremite;
	private Vector<Formes> ensembleForme;
	private int indiceForme;
	private File fichier;
	public static String message= new String();
	
	/*--------------------Constructeur--------------------*/
	public ZoneDessin() {
		// TODO Auto-generated constructor stub
		initialisation();
		this.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e){				
					Point p =new Point(e.getX(), e.getY());					
					origine=p;						
				
			}
			public void mouseClicked(MouseEvent e) 
			{
				if(DrawingWindow.supprimer)
				{
					Point pp = new Point(e.getX(), e.getY());	
					boolean a = contienPoint(pp);
					if(a)
					{
						supprimerForme(ensembleForme.elementAt(indiceForme));
						}
					
					Formes f=new Formes("supp_Dessin", DrawingWindow.pinceau, indiceForme, 0, 0,
				            0);
				try{
			
				Connection.client.envoiForme(f);

                 }catch(Exception ee){ ee.printStackTrace();}

					repaint();
				}
				
				Point p =new Point(e.getX(), e.getY());	
				if(DrawingWindow.texte)
				{
				    origine=p;	
				    Formes f =new Formes("texte", DrawingWindow.pinceau, origine.x, origine.y, 80, 50);
				    JOptionPane dialog = new JOptionPane();
				    String nom = dialog.showInputDialog(null, "Veuillez entrez le texte à Saisir !", "SAISIE !", JOptionPane.QUESTION_MESSAGE);
				    
				    if(!nom.equals(null))
				    {					    
					    f.texteAecrire=nom;					   
				    }
				    ensembleForme.addElement(f);
				    try{
				    	Connection.client.envoiForme(f);
						}catch(Exception ee){ ee.printStackTrace();}
				    
				    repaint();
				}
			
			
			}
			
			public void mouseReleased(MouseEvent e)
			{				
				Point p = new Point(e.getX(), e.getY());
				extremite=p;
				
				if(DrawingWindow.rectangle)
				{			
					Formes f = new Formes("rectangle", DrawingWindow.pinceau, origine.x, origine.y, extremite.x-origine.x,
							               extremite.y-origine.y);					
					
					ensembleForme.addElement(f);
					try{
					Connection.client.envoiForme(f);
					}catch(Exception ee){ ee.printStackTrace();}
				}
				if(DrawingWindow.ellipse)
				{
					Formes f = new Formes("ellipse", DrawingWindow.pinceau, origine.x, origine.y, extremite.x-origine.x,
							               extremite.y-origine.y);
					ensembleForme.addElement(f);
					try{
						Connection.client.envoiForme(f);
						}catch(Exception ee){ ee.printStackTrace();}
					
				}
				if(DrawingWindow.ligne)
				{
					Formes f=new Formes("ligne", DrawingWindow.pinceau, origine.x, origine.y, extremite.x,
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
					Formes f=new Formes("forme", DrawingWindow.pinceau, origine.x, origine.y,extremite.x, extremite.y);
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
	public void sauvegarder()throws IOException
	{
		fichier = new File ("formes.objet");
		File nbForme = new File("nbObjets");
		ObjectOutputStream oos = new ObjectOutputStream (new FileOutputStream (fichier));
		ObjectOutputStream oos1 = new ObjectOutputStream (new FileOutputStream (nbForme));
		oos1.write(ensembleForme.size());
		//oos.write(ensembleForme.size());
		nbObjetEnregistre=ensembleForme.size();
		for(Formes f : ensembleForme)
		{			
			oos.writeObject(f);			
		}	
		oos.close();
		oos1.close();
	}
	
	/*------------------Methode de restauration---------------*/
	public void restaurer(String nomFic)throws Exception
	{
		 
		 Vector<Formes> tmpForme = new Vector<Formes>();
		 ObjectInputStream ois = new ObjectInputStream (new FileInputStream (nomFic));
		 ObjectInputStream ois1 =new ObjectInputStream(new FileInputStream ("nbObjets"));
		 
		// int nbEntre = (int)ois.readInt();
		 int size = (int)ois1.read();
		 for(int i=0; i<size; i++)
		 {
			 Formes ff=(Formes)ois.readObject();
			 tmpForme.addElement(ff);			 
		 }
		 
		// if(tmpForme.size()>ensembleForme.size())
		 //{
			// ensembleForme=tmpForme;
			
		// }
		 for(int i=0;i<tmpForme.size();++i)
		 {
			 Connection.client.envoiForme(tmpForme.elementAt(i));
			 this.ajoutForme(tmpForme.elementAt(i));
		 }
		 repaint();
		 ois.close();
		 ois1.close();		 
	}	
	
	
	/*-------------------Supprimer un dessin----------------*/
	public boolean contienPoint(Point p)
	{
		boolean b =false;
		int i=0;
	
		while(i<ensembleForme.size() && (ensembleForme.elementAt(i).get_x() > p.x ||
				                         ensembleForme.elementAt(i).get_h()>p.y ||
				                         ensembleForme.elementAt(i).get_x()+ensembleForme.elementAt(i).get_h()<p.x ||
				                         ensembleForme.elementAt(i).get_y()+ensembleForme.elementAt(i).get_w()<p.y))
		{
			System.out.println("CoordFormeOrigine >"+ensembleForme.elementAt(i).get_x()+" - "+ensembleForme.elementAt(i).get_y());
			System.out.println("CoordFormeExtremite >"+ensembleForme.elementAt(i).get_x()+ensembleForme.elementAt(i).get_h()+" - "+
			                                           ensembleForme.elementAt(i).get_y()+ensembleForme.elementAt(i).get_w());
			System.out.println("coordPoint >"+p.x+" - "+p.y);
			
			i++;
		}
		if(i<ensembleForme.size())
		{
			indiceForme=i;
			b=true;
		}
		
		return b;
		
	}
	
	
	/*----------------------Initialisation------------------*/
	public void initialisation()
	{
		this.setBackground(new Color(255,255,255));
		origine = new Point();
		extremite = new Point();
		ensembleForme=new Vector<Formes>();		
	}

	/*------------------getters et setters-------------------*/
	public Vector<Formes> getEnsembleForme()
	{
		return ensembleForme;
	}
	
	
	public void ajoutForme(Formes f)
	{
		ensembleForme.addElement(f);
	}
	
	public void supprimerForme(Formes f)
	{
	  if(ensembleForme.contains(f))
	  {
		ensembleForme.remove(f);
	  }
	}
	
	public Point getOrigine()
	{
		return origine;
	}
	
	public Point getextremite()
	{
		return extremite;
	}
	
	public void setOrigine(Point p)
	{
		origine = p;
	}
	
	public void setExtremite(Point p)
	{
		extremite=p;
	}		
	
	/*--------------redefinition de Paincomponent-----------------*/
	@Override
	public void paintComponent(Graphics g) {
		// TODO Auto-generated method stub
		super.paintComponent(g);	
		g.setFont(new Font("arial", 1, 25));
		
		if(!ensembleForme.isEmpty())
		{			
			for(int i=0; i<ensembleForme.size();i++)
			{
			 g.setColor(ensembleForme.elementAt(i).couleur);
			 if(ensembleForme.elementAt(i).getType().equals("rectangle"))
			 {
				Formes f=ensembleForme.elementAt(i);				
				g.drawRect(f.get_x(), f.get_y(), f.get_h(), f.get_w());
			 }
			 else if(ensembleForme.elementAt(i).getType().equals("ellipse"))
			 {
				    Formes f=ensembleForme.elementAt(i);				
					g.drawOval(f.get_x(), f.get_y(), f.get_h(), f.get_w());
			 }
			 else if(ensembleForme.elementAt(i).getType().equals("ligne"))
			 {
				 Formes f = ensembleForme.elementAt(i);
				 g.drawLine(f.get_x(), f.get_y(), f.get_h(), f.get_w());
			 }
			 else if(ensembleForme.elementAt(i).getType().equals("forme"))
			 {
				Formes f = ensembleForme.elementAt(i);
				g.drawLine(f.get_x(), f.get_y(), f.get_h(), f.get_w());
			 }
			 else if(ensembleForme.elementAt(i).getType().equals("texte"))
			 {
				 Formes f = ensembleForme.elementAt(i);
				 g.drawString(f.texteAecrire,f.get_x() , f.get_y());
				
			 }
			}
		}
		
	}	
	
}