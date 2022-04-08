package vectorisation;

/*import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;*/

import acp.Matrice;
import acp.Pixel;

public class Vecteur {
	public Pixel[][] v;	
	
	public Vecteur() {
		v = new Pixel[10000][1];
	}
	
	public int getNbLigne() {
		return this.v.length;
	}
	
	public Pixel[][] getPixels() {
		return v;
	}
	
	public void setV(Pixel[][] v) {
		this.v = v;
	}

	public Matrice transfoMat() {
		Matrice matrice = new Matrice(100, 100);
		 int k = 0;
		 for (int i = 0; i<matrice.getN(); i++) {
			 for (int j = 0; j < matrice.getM(); j++) {
				 Pixel pixel = new Pixel(this.getPixels()[k][0].getIntensite(), i, j);
				 matrice.getPixels()[i][j] = pixel;
				 k++;
			 }
		 }
		 return matrice;
	}
}
