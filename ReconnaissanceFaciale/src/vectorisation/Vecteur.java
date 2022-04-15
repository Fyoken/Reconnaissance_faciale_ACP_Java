package vectorisation;

import acp.Matrice;
import acp.Pixel;

public class Vecteur {
	public Pixel[] v;	
	
	public Vecteur() {
		v = new Pixel[2500];
	}
	
	public int getNbLigne() {
		return this.v.length;
	}
	
	public Pixel[] getPixels() {
		return v;
	}
	
	//Transforme un vecteur en matrice
	public Matrice transfoMat() {
		//Crée une nouvelle matrice de 50 par 50
		Matrice matrice = new Matrice(50,50);
		 int k = 0;
		 //Pour i allant de 0 au nombre de ligne de la matrice
		 for (int i = 0; i<matrice.getN(); i++) {
			 //Et j de 0 au nombre de colonne
			 for (int j = 0; j < matrice.getM(); j++) {
				 //On crée un nouveau pixel avec la valeur qu'il y a dans le vecteur à la position k
				 matrice.getPixels()[i][j]= new Pixel(this.v[k].getIntensite());
				 k++;
			 }
		 }
		 return matrice;
	}
}
