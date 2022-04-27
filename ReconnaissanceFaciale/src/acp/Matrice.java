package acp;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import Jama.Matrix;
import Jama.SingularValueDecomposition;
import personne.Image;
import vectorisation.Vecteur;

public class Matrice {
	private int n;
	private int m;
	private Pixel[][] pixels;
	private Matrix matriceCovariance;
	private SingularValueDecomposition valeursPropres;
	private Matrix vecteursPropres;
	private Matrix matriceProjection;
	private Vecteur moy;

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

	public Vecteur getMoy() {
		return moy;
	}

	public void setMoy(Vecteur moy) {
		this.moy = moy;
	}

	// methode d'initialisation de la matrice de covariance reduite
	public void matriceCovariance() {
		// creation d'une nouvelle matrice carre de la taille des colonnes de la matrice
		Matrix mCov = new Matrix(this.m, this.m);

		// multiplication de la transpose de la matrice image par la matrice images
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

	// methode pour recuperer et initialiser les valeurs propres de la matrice de
	// covariance reduite
	public double[] valeursPropres() {
		SingularValueDecomposition svd = this.matriceCovariance.svd();

		this.setValeursPropres(svd);
		return this.valeursPropres.getSingularValues();
	}

	// methode pour calculer les vecteurs propres
	public Matrix vecteursPropres() {
		// creation d'une matrice de la meme taille que la matrice images
		Matrix U = new Matrix(this.n, this.m);

		// multiplication de la matrice images vace la matrice des vecteurs propres de
		// la matrice de covariance reduite
		for (int i = 0; i < this.n; i++) {
			for (int j = 0; j < this.valeursPropres.getU().getColumnDimension(); j++) {
				U.set(i, j, 0);

				for (int k = 0; k < this.valeursPropres.getU().getRowDimension(); k++) {
					double temp = U.get(i, j) + this.pixels[i][k].getIntensite() * this.valeursPropres.getU().get(k, j);
					U.set(i, j, temp);
				}
			}
		}

		// transformation des vecteurs en leur vecteur unitaire
		for (int j = 0; j < U.getColumnDimension(); j++) {
			double norme = 0;
			for (int i = 0; i < U.getRowDimension(); i++) {
				norme += Math.pow(U.get(i, j), 2);
			}
			norme = Math.sqrt(norme);
			for (int i = 0; i < U.getRowDimension(); i++) {
				U.set(i, j, U.get(i, j) / norme);
			}
		}

		this.setVecteursPropres(U);
		return U;
	}

	// methode pour calculer la matrice de projection des images
	public Matrix matriceProjection() {
		// creation d'une nouvelle matrice
		Matrix mProj = new Matrix(this.m, this.m);

		// multiplication de la transpose de la matrice avec la matrice des vecteurs
		// propres
		for (int i = 0; i < mProj.getRowDimension(); i++) {
			for (int j = 0; j < mProj.getColumnDimension(); j++) {
				mProj.set(i, j, 0);

				for (int k = 0; k < this.n; k++) {
					double temp = mProj.get(i, j) + this.pixels[k][i].getIntensite() * this.vecteursPropres.get(k, j);
					mProj.set(i, j, temp);
				}

			}
		}
		this.setMatriceProjection(mProj);
		return mProj;
	}

	// Methode pour recreer une image avec la matrice de projection
	public Vecteur reconstructionImage(int i, int K) {
		// creation d'un vecteur de retour
		Vecteur imageI = new Vecteur();

		for (int j = 0; j < this.vecteursPropres.getRowDimension(); j++) {
			// initialisation des pixels du vecteur de retour
			imageI.getPixels()[j] = new Pixel(0);
			// calcul de la valeur de l'intensite
			for (int k = 0; k < K; k++) {
				imageI.getPixels()[j].setIntensite(imageI.getPixels()[j].getIntensite()
						+ this.vecteursPropres.get(j, k) * this.matriceProjection.get(i, k));
			}
			// ajout de la moyenne a l'image calculer
			imageI.getPixels()[j]
					.setIntensite(imageI.getPixels()[j].getIntensite() + this.moyenne().getPixels()[j].getIntensite());

		}

		return imageI;
	}

	// methode pour calculer le vecteur moyen
	public Vecteur moyenne() {
		// creation d'un vecteur de retour
		Vecteur moy = new Vecteur();

		for (int i = 0; i < this.n; i++) {
			// initialisation de chaque pixel
			moy.getPixels()[i] = new Pixel(0);

			// calcul de la moyenne de chaque ligne de la matrice
			for (int j = 0; j < this.m; j++) {
				moy.getPixels()[i].setIntensite(moy.getPixels()[i].getIntensite() + this.pixels[i][j].getIntensite());
			}
			moy.getPixels()[i].setIntensite(moy.getPixels()[i].getIntensite() / this.m);
		}
		this.setMoy(moy);
		return moy;
	}

	// methode pour centraliser tous les vecteurs images de la matrice
	public void centralisation() {
		// creation d'une matrice de pixel
		Pixel[][] A = new Pixel[this.n][this.m];
		// recuperation de la moyenne
		Vecteur moy = this.getMoy();

		for (int i = 0; i < this.n; i++) {
			for (int j = 0; j < this.m; j++) {
				// soustraction de la moyen a la valeur de chaque pixel
				double val = this.pixels[i][j].getIntensite() - moy.getPixels()[i].getIntensite();
				A[i][j] = new Pixel(val);
			}
		}
		// changement de la matrice image par celle centralisée
		this.setPixels(A);
	}

	public Vecteur transfoVect() {
		// transformation de matrice à vecteur
		Vecteur vec = new Vecteur();
		int indice = 0;
		int n = this.getN();

		while (indice < n * n) {
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < n; j++) {
					vec.getPixels()[indice] = new Pixel(this.getPixels()[i][j].getIntensite());
					indice++;
				}
			}
		}
		return vec;
	}

	// Méthode pour afficher une matrice en niveau de gris
	public void affichage() {
		// Déclaration des variables
		// Création de l'image
		BufferedImage img = new BufferedImage(this.m, this.n, BufferedImage.TYPE_BYTE_GRAY);
		// Création du fichier qui va stocker l'image
		File f = new File("Image.jpg");
		// recherche du minimum
		double min = 0;
		for (int i = 0; i < this.n; i++) {
			for (int j = 0; j < this.m; j++) {
				if (min == 0 || min > this.getPixels()[i][j].getIntensite())
					min = this.getPixels()[i][j].getIntensite();
			}
		}

		for (int i = 0; i < this.n; i++) {
			for (int j = 0; j < this.m; j++) {
				double valeurIntensite = 1 - this.getPixels()[i][j].getIntensite() + min;
				if (valeurIntensite < 0) {
					valeurIntensite = 0;
				}
				if (valeurIntensite > 1) {
					valeurIntensite = 1;
				}
				// On convertit la valeur du pixel en couleur
				Color couleur = new Color((int) (valeurIntensite * 255d), (int) (valeurIntensite * 255d),
						(int) (valeurIntensite * 255d));
				int gris = couleur.getRGB();
				img.setRGB(i, j, gris);
			}
		}
		// On écrit l'image dans le fichier f
		try {
			ImageIO.write(img, "jpg", f);
		} catch (IOException e) {
			System.err.println("Erreur écriture image");
		}
	}

	// methode pour ajouter un vecteur dans la prochaine colonne vide de la matrice
	public void ajouterImage(Vecteur v) {
		int i = 0;
		// recherche de la premiere colonne vide
		while (this.pixels[0][i] != null && i < this.getM()) {
			i = i + 1;
		}

		// si la matrice n'est pas pleine on ajoute pixel dans la matrice
		if (i < this.m) {
			for (int j = 0; j < this.getN(); j++) {
				this.pixels[j][i] = new Pixel(1 - v.getPixels()[j].getIntensite());
			}
		}

	}

	public void affichageEigenfaces() {
		// creation d'une grande image
		BufferedImage eigenfaces = new BufferedImage(220, 150, BufferedImage.TYPE_INT_RGB);
		Graphics2D fond = eigenfaces.createGraphics();
		fond.setColor(Color.white);
		fond.fillRect(0, 0, 220, 150);

		// Création du fichier qui va stocker l'image
		File f = new File("eigenfaces.jpg");

		for (int i = 0; i < 6; i++) {
			// creation d'un vecteur avec le i eme eigenface
			Vecteur eigenface = new Vecteur();
			for (int j = 0; j < this.n; j++) {
				eigenface.getPixels()[j] = new Pixel(this.vecteursPropres.get(j, i));
			}
			// recherche du min
			double min = 0;
			for (int k = 0; k < this.n; k++) {

				if (min == 0 || min > eigenface.getPixels()[k].getIntensite())
					min = eigenface.getPixels()[k].getIntensite();

			}

			for (int k = 0; k < eigenface.transfoMat().getN(); k++) {
				for (int j = 0; j < eigenface.transfoMat().getM(); j++) {
					double valeurIntensite = eigenface.transfoMat().getPixels()[k][j].getIntensite() - min;
					if (valeurIntensite < 0) {
						valeurIntensite = 0;
					}
					if (valeurIntensite > 1) {
						valeurIntensite = 1;
					}
					// On convertit la valeur du pixel en couleur
					Color couleur = new Color((int) (valeurIntensite * 255d) * 5, (int) (valeurIntensite * 255d) * 5,
							(int) (valeurIntensite * 255d) * 5);
					int gris = couleur.getRGB();
					if (i < 3)
						eigenfaces.setRGB(k + 10 + 75 * i, j + 10, gris);
					else
						eigenfaces.setRGB(k + 10 + 75 * (i - 3), j + 75, gris);
				}
			}
		}
		// On écrit l'image dans le fichier f
		try {
			ImageIO.write(eigenfaces, "jpg", f);
		} catch (IOException e) {
			System.err.println("Erreur écriture image");
		}
	}

	// Méthode pour afficher le graphique de l'évolution de l'erreur pour une image
	// et pour différentes valeurs de K
	public double[] affichageGraphique(Image img, int n) {

		Matrice JM = img.getPhoto();
		double[] d = new double[this.getVecteursPropres().getColumnDimension() + 1];

		// On transforme J en vecteur pour calculer la distance
		Vecteur J = JM.transfoVect();

		// On inverse les valeurs de J
		for (int i = 0; i < J.getNbLigne(); i++) {
			J.getPixels()[i].setIntensite(1 - J.getPixels()[i].getIntensite());
		}
		// Pour chaque valeur de K
		for (int K = 1; K <= this.getVecteursPropres().getColumnDimension(); K++) {

			// On récupère l'image projetée Jp, le n correspond à dire que J est la n-ième
			// image soumise à l'ACP
			Vecteur Jp = this.reconstructionImage(n, K);
			// On calcule la distance euclidienne entre J et Jp et on l'ajoute à la liste
			// des erreurs
			double s = 0;
			for (int i = 0; i < Jp.getNbLigne(); i++) {
				s += Math.pow(J.getPixels()[i].getIntensite() - Jp.getPixels()[i].getIntensite(), 2);
			}
			d[K] = Math.sqrt(s);
		}

		return d;

	}

	// Méthode pour normaliser les valeurs propres et afficher l'évolution de la
	// variance cumulée des eigenfaces
	public double[] normaliserEtAfficherVariation(double[] vp) {
		// Valeur de normalisation qui vaut la somme des valeurs propres
		double s = 0;

		// Variance cumulée
		double res = 0;

		double[] affichage = new double[vp.length];

		// On calcule s
		for (int i = 0; i < vp.length; i++) {
			s += vp[i];
		}

		// On normalise les valeurs propres et affiche l'évolution de la variation
		// cumulée
		for (int i = 0; i < vp.length; i++) {
			res += 100 * vp[i] / s;
			affichage[i] = res;
		}
		return affichage;
	}

}
