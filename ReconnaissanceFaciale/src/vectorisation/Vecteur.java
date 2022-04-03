package vectorisation;

/*import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;*/

import acp.Matrice;

public class Vecteur {
	public double[] v;	
	
	public Vecteur() {
		v = new double[10000];
	}
	
	public int getNbLigne() {
		return this.v.length;
	}
	
	public void transfoVect(Matrice matrice) {
		// transformation de matrice à vecteur
         int indice = 0;
         int n = matrice.getN();
         
         while (indice < n*n) {
         	for (int i = 0; i < n; i++) {
	            	for (int j = 0; j < n; j++) {
	            		this.v[indice] = matrice.getPixels()[i][j].getIntensite();
	            		indice++;
	            	}
	            }
         }
	 }
	
}
