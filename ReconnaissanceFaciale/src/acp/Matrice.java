package acp;

import Jama.Matrix;

public class Matrice {
	private int n;
	private int m;
	private Pixel[][] pixels;

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

	public Matrix matriceCovariance() {
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
		return mCov;
	}

}
