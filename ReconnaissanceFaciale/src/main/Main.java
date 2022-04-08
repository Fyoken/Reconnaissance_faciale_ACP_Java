package main;

import Jama.Matrix;
import acp.Matrice;
import vectorisation.Vecteur;

public class Main {
	public static void main(String[] args) {
		Vecteur vecteur1;
		Vecteur vecteur2;
		Vecteur vecteur3;
		Vecteur vecteur4;
			
        String inImg = "2.jpg";	 
		Matrice image1 = new Matrice(100,100);
		Matrice image1_2 = new Matrice(100,100);
		
		String inImg2 = "3.jpg";	 
		Matrice image2 = new Matrice(100,100);
		Matrice image2_2 = new Matrice(100,100);
		
		String inImg3 = "4.jpg";	 
		Matrice image3 = new Matrice(100,100);
		Matrice image3_2 = new Matrice(100,100);
		
		String inImg4 = "5.jpg";	 
		Matrice image4 = new Matrice(100,100);
		Matrice image4_2 = new Matrice(100,100);
		
		double[] valp1;
		Matrix vecp1;
		
		double[] valp2;
		Matrix vecp2;
		
		double[] valp3;
		Matrix vecp3;
		
		double[] valp4;
		Matrix vecp4;
		
		Matrice images = new Matrice(10000,4);
		Matrice reconstruction;
		
		Vecteur testImage1 = new Vecteur();
		Matrice matriceTest = new Matrice(100,100);

		image1.transformationNiveauGris(inImg);
		vecteur1 = image1.transfoVect();
		image1_2 = vecteur1.transfoMat();
		
		image2.transformationNiveauGris(inImg2);
		vecteur2 = image2.transfoVect();
		image2_2 = vecteur2.transfoMat();
		
		image3.transformationNiveauGris(inImg3);
		vecteur3 = image3.transfoVect();
		image3_2 = vecteur3.transfoMat();
		
		image4.transformationNiveauGris(inImg4);
		vecteur4 = image4.transfoVect();
		image4_2 = vecteur4.transfoMat();
		
		images.ajouterImage(vecteur1);
		images.ajouterImage(vecteur2);
		images.ajouterImage(vecteur3);
		images.ajouterImage(vecteur4);
		images.centralisation();
		images.matriceCovariance();
		
		images.valeursPropres();
		images.vecteursPropres();
		
		images.matriceProjection();
		
		testImage1 = images.reconstructionImage(0);
		matriceTest = testImage1.transfoMat();
		
		matriceTest.affichage();
		
		System.out.println("hello");
		
	}
}
