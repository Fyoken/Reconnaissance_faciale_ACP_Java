package main;

import java.util.HashSet;
import java.util.Set;

import acp.Matrice;
import personne.Image;
import personne.Personne;
import vectorisation.Vecteur;

public class Main {
	public static Set<Personne> bdd=new HashSet<Personne>();
	
	public static void initialisationBDD() {
		bdd.add(Personne.AuzollesM);
		bdd.add(Personne.BarbosaM);
		bdd.add(Personne.ChambasM);
		bdd.add(Personne.RibayneM);
		bdd.add(Personne.SallM);
	}
	
	public static Matrice initialisationMatriceImages() {
		Matrice images= new Matrice(100*100,bdd.size()*Personne.AuzollesM.getImages().size());
		for (Personne personne : bdd) {
			for (Image image : personne.getImages()) {
				images.ajouterImage(image.getPhoto().transfoVect());
			}
			
		}
		
		return images;
	}
	
	public static void main(String[] args) {
		initialisationBDD();
		Matrice images=initialisationMatriceImages();
		images.centralisation();
		images.matriceCovariance();
		images.valeursPropres();
		images.vecteursPropres();
		images.matriceProjection();
		Vecteur test5=images.reconstructionImage(7);
		test5.transfoMat().affichage();
		
		
		
	}
}

