package com.drawing.entity;

import java.awt.Color;
import java.io.Serializable;

public class Formes implements Serializable{
	public static int numForme=1;
	private String typeForme;
	public String texteAecrire;
	public int numero;
	public Color couleur;
	private int x;
	private int y;
	private int h;
	private int w;
	
	public Formes(String nom,Color c,int x1, int y1, int h1, int w1) {

		typeForme = nom;
		x=x1;
		y=y1;
		h=h1;
		w=w1;

		if( typeForme.equals("QUIT")) {
			numero=-1;
		} else {
			numero = numForme;
		}
		numForme ++;
		couleur = c;
		texteAecrire = new String("none");
	}
	
	public String getType()
	{
		return typeForme;
	}
	
	public int get_x() {
		return x;
	}
	
	public int get_y()
	{
		return y;
	}
	
	public int get_h()
	{
		return h;
	}
	
	public int get_w()
	{
		return w;
	}
	
	public void set_x(int nx)
	{
		x=nx;
	}
	
	public void set_y(int ny)
	{
		y=ny;
	}
	
	public void set_h(int nh)
	{
		h=nh;
	}


	public void set_w(int nw)
	{
		w=nw;
	}
	public void set_Type(String s)
	{
		typeForme=s;	
	}
	public boolean estEgal(Formes f)
	{
		if(this.numero==f.numero)
		{
			return true;
		}
		return false;			
	}
	
	
	
}