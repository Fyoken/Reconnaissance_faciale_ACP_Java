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
	private String[] noms;
	private Matrix matriceVisage;
	private Pixel[][] matriceCentralisee;

	/**
	 * Objectif : Retourner la matrice centralisée
	 * @return 	matriceCentralisee la matrice centralisée
	 */
	public Pixel[][] getMatriceCentralisee() {
		return matriceCentralisee;
	}

	/**
	 * Objectif : Changer les valeurs de la matrice centralisée
	 * @param 	matriceCentralisee la nouvelle matrice centralisée, avec les nouvelles valeurs
	 */
	public void setMatriceCentralisee(Pixel[][] matriceCentralisee) {
		this.matriceCentralisee = matriceCentralisee;
	}
	
	/**
	 * Objectif : Retourner la matrice de visages
	 * @return 	matriceVisage la matrice de visages
	 */
	public Matrix getMatriceVisage() {
		return matriceVisage;
	}

	/**
	 * Objectif : Changer les valeurs de la matrice visage
	 * @param 	matriceVisage la nouvelle matrice de visage, avec les nouvelles valeurs
	 */
	public void setMatriceVisage(Matrix matriceVisage) {
		this.matriceVisage = matriceVisage;
	}

	/**
	 * Objectif : Retourner les noms de la matrice
	 * @return 	noms 
	 */
	public String[] getNoms() {
		return noms;
	}

	/**
	 * Objectif : Modifier les valeurs des strings des noms
	 * @param 	noms les valeurs modifiées des noms
	 */
	public void setNoms(String[] noms) {
		this.noms = noms;
	}

	/**
	 * Objectif : Construire une matrice selon une taille donnée, avec les valeurs des pixels se situant à la position n et m
	 * @param n le nombre de lignes 
	 * @param m le nombre de colonnes
	 */
	public Matrice(int n, int m) {
		this.n = n;
		this.m = m;
		this.pixels = new Pixel[this.n][this.m];
	}

	/**
	 * Objectif : Avoir le nombre de ligne de la matrice
	 * @return 	n le nombre de ligne de la matrice
	 */
	public final int getN() {
		return n;
	}

	/**
	 * Objectif : Modifier le nombre de ligne de la matrice
	 * @param 	n le nouveau nombre de ligne de la matrice
	 */
	public final void setN(int n) {
		this.n = n;
	}

	/**
	 * Objectif : Obtenir le nombre de colonne de la matrice
	 * @return 	m le nouveau nombre de colonne de la matrice
	 */
	public final int getM() {
		return m;
	}

	/**
	 * Objectif : Modifier le nombre de colonne de la matrice
	 * @param 	m le nouveau nombre de colonne de la matrice
	 */
	public final void setM(int m) {
		this.m = m;
	}

	/**
	 * Objectif : Obtenir le tableau à 2 dimensions de pixels
	 * @return 	pixels le tableau de pixel
	 */
	public final Pixel[][] getPixels() {
		return pixels;
	}

	/**
	 * Objectif : Modifier les valeurs du tableau de pixels
	 * @param pixels le tableau avec les nouvelles valeurs de pixels
	 */
	public final void setPixels(Pixel[][] pixels) {
		this.pixels = pixels;
	}

	/**
	 * Objectif : Obtenir la matrice de covariance
	 * @return 	matriceCovariance la matrice de covariance
	 */
	public final Matrix getMatriceCovariance() {
		return matriceCovariance;
	}
	
	/**
	 * Objectif : Modifier les valeurs de la matrice de covariance
	 * @param 	matriceCovariance les nouvelles valeurs de la matrice de covariance
	 */
	public final void setMatriceCovariance(Matrix matriceCovariance) {
		this.matriceCovariance = matriceCovariance;
	}

	/**
	 * Objectif : Obtenir les valeurs propres
	 * @return 	valeursPropres les valeurs propres de la matrice
	 */
	public final SingularValueDecomposition getValeursPropres() {
		return valeursPropres;
	}

	/**
	 * Objectif : Modifier les valeurs propres
	 * @param valeursPropres les nouvelles valeurs propres
	 */
	public final void setValeursPropres(SingularValueDecomposition valeursPropres) {
		this.valeursPropres = valeursPropres;
	}

	/**
	 * Objectif : Obtenir les vecteurs propres
	 * @return 	vecteursPropres les vecteurs propres
	 */
	public final Matrix getVecteursPropres() {
		return vecteursPropres;
	}

	/**
	 * Objectif : Modifier les valeurs des vecteurs propres
	 * @param 	vecteursPropres les nouvelles valeurs des vecteurs propres
	 */
	public final void setVecteursPropres(Matrix vecteursPropres) {
		this.vecteursPropres = vecteursPropres;
	}

	/**
	 * Objectif : Obtenir la matrice de projection
	 * @return matriceProjection la matrice de projection
	 */
	public final Matrix getMatriceProjection() {
		return matriceProjection;
	}

	/**
	 * Objectif : Modifier les valeur de la matrice de projection
	 * @param 	matriceProjection les nouvelles valeurs de la matrice de projection
	 */
	public final void setMatriceProjection(Matrix matriceProjection) {
		this.matriceProjection = matriceProjection;
	}

	/**
	 * Objectif : Obtenir la moyenne de la matrice
	 * @return  moy la moyenne de la matrice
	 */
	public Vecteur getMoy() {
		return moy;
	}

	/**
	 * Objectif : Modifier la valeur de la moyenne
	 * @param 	moy la nouvelle moyenne
	 */
	public void setMoy(Vecteur moy) {
		this.moy = moy;
	}

	/**
	 * Objectif : Initialiser les noms
	 * @param 	noms une nouvelle liste de noms
	 */
	public void noms(String[] noms) {
		setNoms(noms);
	}

	/**
	 * Objectif : Initialiser la matrice de visage
	 */
	public void matriceVisage() {
		// creation d'une nouvelle matrice carre de la taille des colonnes de la matrice
		Matrix mVis = new Matrix(this.n, this.m);
		for (int i = 0; i < this.n; i++) {
			for (int j = 0; j < this.m; j++) {
				mVis.set(i, j, this.pixels[i][j].getIntensite());
			}
		}
		this.setMatriceVisage(mVis);
	}

	/**
	 * Objectif : Initialiser la matrice de covariance réduite
	 */
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

	/**
	 * Objectif : Récupérer et initialiser les valeurs propres de la matrice de covariance réduite
	 * @return 	this.valeursPropres.getSingularValues les valeurs propres de l'objet actuel
	 */
	public double[] valeursPropres() {
		SingularValueDecomposition svd = this.matriceCovariance.svd();

		this.setValeursPropres(svd);
		return this.valeursPropres.getSingularValues();
	}

	/**
	 * Objectif : Calculer les vecteurs propres
	 * @return 	U la matrice de vecteurs propres
	 */
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

	/**
	 * Objectif : Calculer la matrice de projection
	 * @return 	mProj la matrice de projection des visages
	 */
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

	/**
	 * Objectif : Recréer une image grâce à la matrice de projection
	 * @param  	i l'indice de l'image à recréer
	 * @param 	K le nombre d'eigenface utilisé
	 * @return 	imageI le vecteur de l'image recrée
	 */
	public Vecteur reconstructionImage(int i, int K) {
		// creation d'un vecteur de retour
		Vecteur imageI = new Vecteur();

		for (int j = 0; j < this.vecteursPropres.getRowDimension(); j++) {
			// initialisation des pixels du vecteur de retour
			imageI.getPixels()[j] = new Pixel(0);
			// calcul de la valeur de l'intensite
			for (int k = 0; k < K; k++) {
				imageI.getPixels()[j].setIntensite(imageI.getPixels()[j].getIntensite()+ this.vecteursPropres.get(j, k) * this.matriceProjection.get(i, k));
			}
			// ajout de la moyenne a l'image calculer
			imageI.getPixels()[j].setIntensite(imageI.getPixels()[j].getIntensite() + this.moy.getPixels()[j].getIntensite());

		}

		return imageI;
	}

	/**
	 * Objectif : Calculer le vecteur moyen
	 * @return  moy le vecteur moyen
	 */
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

	/**
	 * Objectif : Centraliser tous les vecteurs images de la matrice
	 */
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
		this.setMatriceCentralisee(A);
	}

	/**
	 * Objectif : Transformer une matrice en vecteur
	 * @return 	vec le vecteur de la matrice
	 */
	public Vecteur transfoVect() {
		// transformation de matrice à vecteur
		Vecteur vec = new Vecteur();
		int indice = 0;
		int n = this.getN();

		//Tant que l'indice est plus petit que le nombre d'élément du vecteur
		while (indice < n * n) {
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < n; j++) {
					//On ajoute le pixel de la case i, j dans le vecteur à l'indice k
					vec.getPixels()[indice] = new Pixel(this.getPixels()[i][j].getIntensite());
					indice++;
				}
			}
		}
		return vec;
	}

	/**
	 * Objectif : Afficher une matrice en niveau de gris
	 * @param 	name le nom de la personne correspondante
	 */
	public void affichage(String name) {
		// Déclaration des variables
		// Création de l'image
		BufferedImage img = new BufferedImage(this.m, this.n, BufferedImage.TYPE_BYTE_GRAY);
		// Création du fichier qui va stocker l'image
		File f = new File(name);
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

	/**
	 * Objectif : Ajouter un vecteur à la prochaine colonne vide de la matrice
	 * @param 	v le vecteur à ajouter à la matrice
	 */
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

	/**
	 * Objectif : Afficher les eigenfaces
	 */
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

	/**
	 * Objectif : Afficher le graphique de l'évolution de l'erreur pour une image et pour différentes valeur de K
	 * @param  	img l'image dont on veut afficher le graphique d'erreur
	 * @param 	n 	le numéro de l'image soumise à K
	 * @return  d un tableau de double avec les différentes valeurs de l'erreur
	 */
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

	/**
	 * Objectif : Normaliser les valeurs propres et afficher l'évolution de la variance cumulée des eigenfaces
	 * @param  	vp le tableau des valeurs propres
	 * @return  affichage (le tableau contenant les valeurs propres normalisées
	 */
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

	/**
	 * Objectif : Projeter une image
	 * @param  	img l'image à projeter
	 * @param	K  le nombre d'eigenfaces
	 * @return  projection le tableau de l'image projetée
	 */
	public double[] projection(Image img, int K) {
		// creation d'un vecteur a partir de l'image
		Vecteur image = img.getPhoto().transfoVect();
		// inversion des valeurs et centralisation
		for (int i = 0; i < image.getNbLigne(); i++) {
			
			image.getPixels()[i].setIntensite(1 - image.getPixels()[i].getIntensite());
			
			image.getPixels()[i].setIntensite(image.getPixels()[i].getIntensite() - this.moy.getPixels()[i].getIntensite());
		}

		double[] projection = new double[K];
		// calcul de la projection de l'image dans la base des vecteurs propres
		for (int k = 0; k < projection.length; k++) {
			projection[k] = 0;

			for (int i = 0; i < image.getNbLigne(); i++) {
				projection[k] += image.getPixels()[i].getIntensite() * this.vecteursPropres.get(i, k);
			}
		}

		return projection;
	}

	/**
	 * Objectif : Stocker les projections des images de références
	 */
	public void projectionReference() {
		// On projette et stocke chaque image de référence
		int n = (int) Math.sqrt(this.getMatriceVisage().getRowDimension());

		// On crée le dossier reference s'il n'existe pas
		File ref = new File("reference");
		ref.mkdirs();

		// On récupère la j ieme image
		for (int j = 0; j < this.getVecteursPropres().getColumnDimension(); j++) {
			Matrice imageM = new Matrice(n, n);
			int z = 0;
			for (int i = 0; i < n; i++) {
				for (int k = 0; k < n; k++) {
					imageM.pixels[i][k] = new Pixel(this.getMatriceVisage().get(z, j));
					z++;
				}
			}

			// On projette la j eme image et on la stocke dans le dossier reference
			Vecteur proj = this.reconstructionImage(j, this.vecteursPropres.getColumnDimension());
			Matrice projAffichage = proj.transfoMat();
			projAffichage.affichage(ref.getPath() + "/" + j + ".jpg");

		}
	}

	/**
	 * Objectif : Trouver l'image la plus ressemblante à celle passée en paramètre
	 * @param image l'image dont on doit trouver la correspondance
	 * @param	K 	le nombre d'eigenfaces
	 * @param 	s  	la distance à ne pas dépasser
	 * @return 	indice l'indice de l'image avec qui la correspondance à fonctionné, retourne -1 si cela ne correspond à personne
	 */
	public int reconnaissance(Image image, int K, int s) {
		//recuperation de la projection de l'image
		double[] projImage = this.projection(image,K);
		//initialisation des variables
		double min = -1;
		double distance;
		int indice = -1;
		
		//calcul de la distance entre la projection de l'image et celles des images de la bdd
		for (int j = 0; j < this.matriceProjection.getRowDimension(); j++) {
			distance=0;
			for (int i = 0; i < projImage.length; i++) {
				distance+=Math.pow(this.matriceProjection.get(j, i)-projImage[i], 2);
			}
			distance=Math.sqrt(distance);
			//recherche de la plus petite distance
			if(min==-1||distance<min) {
				min=distance;
				indice=j;
			}
		}
		//rejet de la valeur trouver si la plus petite distance est trop loin
		if(min>s) {
			indice=-1;
		}
		
		return indice;

	}

}
