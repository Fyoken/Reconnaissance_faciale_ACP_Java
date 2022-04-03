package vectorisation;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Vectorisation {
	 static int n = 960;
	 public static void vecto(String inImg,double[][] matrice)
			 throws IOException 
			 {
			      // lit l'image d'entrée
			      File f = new File(inImg);
			      BufferedImage inputImage = ImageIO.read(f);
			      for(int x = 0; x < n; x++) {
			          for(int y = 0; y < n; y++) {
			              Color color = new Color(inputImage.getRGB(x,y));
			              int gray = color.getRed();
			              matrice[x][y] = gray / 255d;
			          }
			      }
			 }
	 
	 public static void transfoVect(double[][] matrice, double[] vecteur) {
		// transformation de matrice à vecteur
         int k = 0;
         while (k < n*n) {
         	for (int i = 0; i < n; i++) {
	            	for (int j = 0; j < n; j++) {
	            		vecteur[k]=matrice[i][j];
	            		k++;
	            	}
	            }
         }
         // vecteur est de taille n*n x 1
	 }
	 
	 public static void transfoMat(double[][] matrice, double[] vecteur) {
		 int k = 0;
		 int i = 0;
		 while (i < n-1) {
			 for (int j = 0; j < n; j++) {
				 matrice[i][j]=vecteur[k];
				 k++;
			 }
			 i++;
		 }
		 // matrice est de taille n x n
	}
	 
	 public static void main(String[] args) 
	 {
	        String inImg = "test.jpg";	 
	        try 
	        {
	            double[][] matrice = new double[n][n];
	            double[] vecteur = new double[n*n];
	       
	            Vectorisation.vecto(inImg, matrice);
	            // matrice est de taille n x n
	            Vectorisation.transfoVect(matrice, vecteur);
	            for (int i = 0; i < n; i++) {
	            	for (int j = 0; j < n; j++) {
	            		matrice[i][j]=0;
	            	}
	            }
	            Vectorisation.transfoMat(matrice, vecteur);
	            System.out.println(matrice[1][0]);
	            System.out.println(vecteur[960]);
	        }
	        catch (IOException ex) 
	        {
	            ex.printStackTrace();
	        }
	 }
}
