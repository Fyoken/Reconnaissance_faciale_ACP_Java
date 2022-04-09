package vectorisation;

/*import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;*/

import acp.Matrice;
import acp.Pixel;

public class Vecteur {
	public Pixel[] v;	
	
	public Vecteur() {
		v = new Pixel[10000];
	}
	
	public int getNbLigne() {
		return this.v.length;
	}
	
	public Pixel[] getPixels() {
		return v;
	}
	
	
	public Matrice transfoMat() {
		Matrice matrice = new Matrice(100, 100);
		 int k = 0;
		 for (int i = 0; i<matrice.getN(); i++) {
			 for (int j = 0; j < matrice.getM(); j++) {
				 matrice.getPixels()[i][j].setIntensite(this.v[k].getIntensite());
				 k++;
			 }
		 }
		 return matrice;
	}
}

