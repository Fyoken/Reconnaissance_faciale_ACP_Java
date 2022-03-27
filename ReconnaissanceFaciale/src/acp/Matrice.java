package acp;

import Jama.Matrix;
import Jama.SingularValueDecomposition;

public class Matrice {
	private int n;
	private int m;
	private Pixel[][] pixels;
	private Matrix matriceCovariance;
	private SingularValueDecomposition valeursPropres;

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

	public void matriceCovariance() {
		Matrix mCov = new Matrix(this.n, this.n);

		for (int i = 0; i < this.n; i++) {
			for (int j = 0; j < this.n; j++) {
				mCov.set(i, j, 0);

				for (int k = 0; k < this.m; k++) {
					double temp = mCov.get(i, j) + this.pixels[i][k].getIntensite() * this.pixels[j][k].getIntensite();
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

}
