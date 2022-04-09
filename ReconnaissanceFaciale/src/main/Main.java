package main;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import acp.Matrice;
import acp.Pixel;
import vectorisation.Vecteur;

public class Main {
	// Méthode pour afficher le graphique de l'évolution de l'erreur pour différentes valeurs de K
	public static double[] affichageGraphique(String inImg) throws IOException {	
		
		// On récupère l'image d'entrée de la base d'apprentissage dans A
		File f = new File(inImg);
		BufferedImage img = ImageIO.read(f);
		Pixel[][] A = new Pixel[100][100];
		
		// JM est A en Matrice, les deux boucles for et le setPixels permettent de lui donner ses valeurs
		Matrice JM = new Matrice(100,100);
		
		// d est une liste qui comporte l'évolution de l'erreur pour chaque valeurs de K
		double[] d = new double[JM.getVecteursPropres().getColumnDimension()+1];
		
		for(int i=0;i<JM.getN();i++){
			for(int j=0;j<JM.getM();j++) {
				A[i][j].setIntensite(img.getRGB(i, j));
			}
		}
		JM.setPixels(A);
		
		// Pour chaque valeur de K
		for (int K=1; K < JM.getVecteursPropres().getColumnDimension(); K++) {
			
			// On récupère l'image projetée Jp, le 0 correspond à dire que J est la première image soumise à l'ACP
			Vecteur Jp = JM.reconstructionImage(0, K);
			
			// On transforme J en vecteur pour calculer la distance
			Vecteur J=JM.transfoVect();
			
			// On calcule la distance euclidienne entre J et Jp et on l'ajoute à la liste des erreurs
			double s = 0;
			for (int i = 0; i < Jp.getNbLigne(); i++) {
				s+=Math.pow(J.v[i].getIntensite() - Jp.v[i].getIntensite(),2);
			}
			d[K] = Math.sqrt(s);
		}
		
		// Affichage de chaque valeur de la liste
		for (int i = 0; i < d.length; i++) {
			System.out.println("L'erreur pour K = "+i+" est : "+d[i]);
		}
		
		return d;
			
	}
	public static void main(String[] args) throws IOException {
		
	}
}

