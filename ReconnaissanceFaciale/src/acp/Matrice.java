package acp;

public class Matrice {
	private int n;
	private int m;
	private Pixel[][] pixels;
	
	public Matrice(int n, int m) {
		this.n=n;
		this.m=m;
		this.pixels= new Pixel[this.n][this.m];
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
	
	
	
	
}
