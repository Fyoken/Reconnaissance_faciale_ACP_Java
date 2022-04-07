package main;

import Jama.Matrix;
import acp.Matrice;
import vectorisation.Vecteur;

public class Main {
	public static void main(String[] args) {
		Vecteur vecteur1;
			
        String inImg = "2.jpg";	 
		Matrice image1 = new Matrice(100,100);
		Matrice image1_2 = new Matrice(100,100);
		
      
		image1.transformationNiveauGris(inImg);
		
		vecteur1 = image1.transfoVect();

		image1_2 = vecteur1.transfoMat();
		
		image1_2.matriceCovariance();

		System.out.println(image1_2.getPixels()[0][0].getIntensite());
		System.out.println(vecteur1.getPixels()[0][0].getIntensite());

	}
}
