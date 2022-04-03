package acp;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import Jama.Matrix;
import Jama.SingularValueDecomposition;

public class Matrice {
	private int n;
	private int m;
	private Pixel[][] pixels;
	private Matrix matriceCovariance;
	private SingularValueDecomposition valeursPropres;
	private Matrix vecteursPropres;

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

	public void matriceCovariance() {
		Matrix mCov = new Matrix(this.m, this.m);

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

	public double[] valeursPropres() {
		SingularValueDecomposition svd = this.matriceCovariance.svd();

		this.setValeursPropres(svd);
		return this.valeursPropres.getSingularValues();
	}

	public Matrix vecteursPropres() {
		Matrix U = new Matrix(this.n, this.m);

		for (int i = 0; i < this.n; i++) {
			for (int j = 0; j < this.valeursPropres.getU().getColumnDimension(); j++) {
				U.set(i, j, 0);

				for (int k = 0; k < this.valeursPropres.getU().getRowDimension(); k++) {
					double temp = U.get(i, j) + this.pixels[i][k].getIntensite() * this.valeursPropres.getU().get(k, j);
					U.set(i, j, temp);
				}
			}
		}
		
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
	
	public Matrix matriceProjection() {
		Matrix mProj=new Matrix(this.m,this.m);
		
		for (int i = 0; i < mProj.getRowDimension(); i++) {
			for (int j = 0; j < mProj.getColumnDimension(); j++) {
				mProj.set(i, j, 0);

				for (int k = 0; k < this.n; k++) {
					double temp = mProj.get(i, j) + this.pixels[k][i].getIntensite()*this.vecteursPropres.get(k, j);
					mProj.set(i, j, temp);
				}

			}
		}
		
		return mProj;
	}
	
	public double[] reconstructionImage(int i) {
		double[] imageI=new double[this.n];
		int compteur=0;
		
		for(int j=0;j<imageI.length;j++) {
			imageI[compteur]=0;
			for(int k=0;k<this.matriceProjection().getColumnDimension();k++) {
				imageI[compteur]+=this.vecteursPropres.get(j, k)*this.matriceProjection().get(i, k);
			}
			compteur+=1;
		}
		
		return imageI;
	}
	
	public double[] moyenne() {
		double[] moy=new double[this.n];
		
		for(int i=0;i<this.n;i++) {
			moy[i]=0;
			for(int j=0;j<this.m;j++) {
				moy[i]+=this.pixels[i][j].getIntensite();
			}
			moy[i]=moy[i]/this.m;
		}
		
		
		return moy;
	}
	
	public void centralisation() {
		Pixel[][] A= new Pixel[this.n][this.m];
		double[] moy=this.moyenne();
		
		for(int i=0;i<this.n;i++){
			for(int j=0;j<this.m;j++) {
				double val=this.pixels[i][j].getIntensite()-moy[i];
				A[i][j].setIntensite(val);
			}
		}
			
		this.setPixels(A);
	}

	
	public void transformationNiveauGris(String inImg) throws IOException {
	      // lit l'image d'entrée
	      File f = new File(inImg);
	      BufferedImage inputImage = ImageIO.read(f);
	      for(int x = 0; x < this.n; x++) {
	          for(int y = 0; y < this.m; y++) {
	              Color color = new Color(inputImage.getRGB(x,y));
	              int gray = color.getRed();
	              this.getPixels()[x][y].setIntensite(gray / 255d);
	          }
	      }
	 }

}
