package acp;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import Jama.Matrix;
import Jama.SingularValueDecomposition;
import vectorisation.Vecteur;

public class Matrice {
	private int n;
	private int m;
	private Pixel[][] pixels;
	private Matrix matriceCovariance;
	private SingularValueDecomposition valeursPropres;
	private Matrix vecteursPropres;
	private Matrix matriceProjection;

	public Matrice(int n, int m) {
		this.n = n;
		this.m = m;
		this.pixels = new Pixel[this.n][this.m];
	}

	public final int getN() {
		return n;
	}

	public final void setN(int n) {
		this.n = n;
	}

	public final int getM() {
		return m;
	}

	public final void setM(int m) {
		this.m = m;
	}

	public final Pixel[][] getPixels() {
		return pixels;
	}

	public final void setPixels(Pixel[][] pixels) {
		this.pixels = pixels;
	}

	public final Matrix getMatriceCovariance() {
		return matriceCovariance;
	}

	public final void setMatriceCovariance(Matrix matriceCovariance) {
		this.matriceCovariance = matriceCovariance;
	}

	public final SingularValueDecomposition getValeursPropres() {
		return valeursPropres;
	}

	public final void setValeursPropres(SingularValueDecomposition valeursPropres) {
		this.valeursPropres = valeursPropres;
	}

	public final Matrix getVecteursPropres() {
		return vecteursPropres;
	}

	public final void setVecteursPropres(Matrix vecteursPropres) {
		this.vecteursPropres = vecteursPropres;
	}

	public final Matrix getMatriceProjection() {
		return matriceProjection;
	}

	public final void setMatriceProjection(Matrix matriceProjection) {
		this.matriceProjection = matriceProjection;
	}

	//methode d'initialisation de la matrice de covariance reduite
	public void matriceCovariance() {
		//creation d'une nouvelle matrice carre de la taille des colonnes de la matrice
		Matrix mCov = new Matrix(this.m, this.m);

		//multiplication de la transpose de la matrice image par la matrice images
		for (int i = 0; i < this.m; i++) {
			for (int j = 0; j < this.m; j++) {
				mCov.set(i, j, 0);

				for (int k = 0; k < this.n; k++) {
					double temp = mCov.get(i, j) + this.pixels[k][i].getIntensite() * this.pixels[k][j].getIntensite();
					mCov.set(i, j, temp);
				}

			}
		}
		this.setMatriceCovariance(mCov);
	}

	//methode pour recuperer et initialiser les valeurs propres de la matrice de covariance reduite
	public double[] valeursPropres() {
		SingularValueDecomposition svd = this.matriceCovariance.svd();

		this.setValeursPropres(svd);
		return this.valeursPropres.getSingularValues();
	}

	//methode pour calculer les vecteurs propres
	public Matrix vecteursPropres() {
		//creation d'une matrice de la meme taille que la matrice images
		Matrix U = new Matrix(this.n, this.m);

		//multiplication de la matrice images vace la matrice des vecteurs propres de la matrice de covariance reduite
		for (int i = 0; i < this.n; i++) {
			for (int j = 0; j < this.valeursPropres.getU().getColumnDimension(); j++) {
				U.set(i, j, 0);

				for (int k = 0; k < this.valeursPropres.getU().getRowDimension(); k++) {
					double temp = U.get(i, j) + this.pixels[i][k].getIntensite() * this.valeursPropres.getU().get(k, j);
					U.set(i, j, temp);
				}
			}
		}
		
		//transformation des vecteurs en leur vecteur unitaire
		for(int j=0;j<U.getColumnDimension();j++) {
			double norme=0;
			for(int i=0;i<U.getRowDimension();i++ ) {
				norme+=Math.pow(U.get(i, j), 2) ;
			}
			norme=Math.sqrt(norme);
			for(int i=0; i<U.getRowDimension();i++) {
				U.set(i, j, U.get(i, j)/norme);
			}
		}
		
		this.setVecteursPropres(U);
		return U;
	}
	
	//methode pour calculer la matrice de projection des images
	public Matrix matriceProjection() {
		//creation d'une nouvelle matrice
		Matrix mProj=new Matrix(this.m,this.m);
		
		//multiplication de la transpose de la matrice avec la matrice des vecteurs propres
		for (int i = 0; i < mProj.getRowDimension(); i++) {
			for (int j = 0; j < mProj.getColumnDimension(); j++) {
				mProj.set(i, j, 0);

				for (int k = 0; k < this.n; k++) {
					double temp = mProj.get(i, j) + this.pixels[k][i].getIntensite()*this.vecteursPropres.get(k, j);
					mProj.set(i, j, temp);
				}

			}
		}
		this.setMatriceProjection(mProj);
		return mProj;
	}
	
	//Methode pour recreer une image avec la matrice de projection
	public Vecteur reconstructionImage(int i) {
		//creation d'un vecteur de retour
		Vecteur imageI=new Vecteur();
		
		for(int j=0;j<this.vecteursPropres.getRowDimension();j++) {
			//initialisation des pixels de le vecteur de retour
			imageI.getPixels()[j]=new Pixel(0);
			//calcul de la valeur de l'intensite 
			for(int k=0;k<this.vecteursPropres.getColumnDimension();k++) {
				imageI.getPixels()[j].setIntensite(imageI.getPixels()[j].getIntensite()+this.vecteursPropres.get(j, k)*this.matriceProjection.get(i, k));
			}
			//ajout de la moyenne a l'image calculer
			imageI.getPixels()[j].setIntensite(imageI.getPixels()[j].getIntensite()+this.moyenne().getPixels()[j].getIntensite());
			
			
		}
		
		
		return imageI;
	}
	
	//methode pour calculer le vecteur moyen
	public Vecteur moyenne() {
		//creation d'un vecteur de retour
		Vecteur moy=new Vecteur();
		
		for(int i=0;i<this.n;i++) {
			//initialisation de chaque pixel
			moy.getPixels()[i]=new Pixel(0);
			
			//calcul de la moyenne de chaque ligne de la matrice
			for(int j=0;j<this.m;j++) {
				moy.getPixels()[i].setIntensite(moy.getPixels()[i].getIntensite()+this.pixels[i][j].getIntensite());
			}
			moy.getPixels()[i].setIntensite(moy.getPixels()[i].getIntensite()/this.m);
		}
		
		
		return moy;
	}
	
	//methode pour centraliser tous les vecteurs images de la matrice
	public void centralisation() {
		//creation d'une matrice de pixel
		Pixel[][] A= new Pixel[this.n][this.m];
		//recuperation de la moyenne
		Vecteur moy=this.moyenne();
		
		for(int i=0;i<this.n;i++){
			for(int j=0;j<this.m;j++) {
				//soustraction de la moyen a la valeur de chaque pixel
				double val=this.pixels[i][j].getIntensite()-moy.getPixels()[i].getIntensite();
				A[i][j] = new Pixel(val);
			}
		}
		//changement de la matrice image par celle centralisée
		this.setPixels(A);
	}

	public Vecteur transfoVect() {
		// transformation de matrice à vecteur
		Vecteur vec = new Vecteur();
		int indice = 0;
		int n = this.getN();
		 
		while (indice < n*n) {
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < n; j++) {
					vec.getPixels()[indice] = new Pixel(this.getPixels()[i][j].getIntensite());
					indice++;
		        }
			}
		}
		return vec;
	 }
	
	
	//Méthode pour afficher une matrice en niveau de gris
	public void affichage() {
		//Déclaration des variables 
		//Création de l'image
		BufferedImage img = new BufferedImage(this.m, this.n, BufferedImage.TYPE_INT_RGB);
		//Création du fichier qui va stocker l'image
		File f = new File("Image.jpg");
		//recherche du minimum
		double min=0;
		for(int i=0;i<this.n;i++){
			for(int j=0;j<this.m;j++) {
				if(min==0||min>this.getPixels()[i][j].getIntensite())
					min=this.getPixels()[i][j].getIntensite();
			}
		}
		
		for(int i=0;i<this.n;i++){
			for(int j=0;j<this.m;j++) {
				double valeurIntensite=1-this.getPixels()[i][j].getIntensite()+min;
				if(valeurIntensite<0) {
					valeurIntensite=0;
				}
				if(valeurIntensite>1) {
					valeurIntensite=1;
				}
				//On convertit la valeur du pixel en couleur
				Color couleur = new Color((int) (valeurIntensite*255d) , (int) (valeurIntensite*255d), (int) (valeurIntensite*255d));
				int gris = couleur.getRGB();
				img.setRGB(i,j, gris);
			}
		}
		//On écrit l'image dans le fichier f
		try {
			ImageIO.write(img, "jpg", f);
		}catch(IOException e ) {
			System.err.println("Erreur écriture image");
		}
	}
	
	//methode pour ajouter un vecteur dans la prochaine colonne vide de la matrice
	public void ajouterImage(Vecteur v) {
		int i = 0;
		//recherche de la premiere colonne vide
		while (this.pixels[0][i] != null && i < this.getM()) {
			i = i+1;
		}
		
		//si la matrice n'est pas pleine on ajoute pixel dans la matrice
		if(i<this.m) {
			for (int j = 0; j<this.getN(); j++) {
				this.pixels[j][i] = new Pixel(1- v.getPixels()[j].getIntensite());
			}	
		}
		
	}
	
	
	


}
