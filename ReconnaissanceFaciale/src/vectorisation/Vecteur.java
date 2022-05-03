package vectorisation;

import acp.Matrice;
import acp.Pixel;

public class Vecteur {
	public Pixel[] v;	
	
	/**
	 * Objectif : Construire un vecteur de 2500 lignes (50*50)
	 */
	public Vecteur() {
		v = new Pixel[2500];
	}
	
	/**
	 * Objectif : Obtenir le nombre de ligne du vecteur
	 * @return this.v.length (le nombre de ligne du vecteur)
	 */
	public int getNbLigne() {
		return this.v.length;
	}
	
	/**
	 * Objectif : Récupérer le tableau de pixels du vecteur
	 * @return v (le tableau de pixels du vecteur)
	 */
	public Pixel[] getPixels() {
		return v;
	}
	
	/**
	 * Objectif : Transformer un vecteur en matrice
	 * @return  matrice (la matrice construite depuis le vecteur)
	 */
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
