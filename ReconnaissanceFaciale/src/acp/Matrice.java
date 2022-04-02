package acp;

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

		this.setVecteursPropres(U);
		return U;
	}

}
