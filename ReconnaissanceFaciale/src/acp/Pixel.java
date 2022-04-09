package acp;

public class Pixel {
	private double intensite;
	private int positionN;
	private int positionM;
	
	
	
	public Pixel(double intensite, int positionN, int positionM) {
		this.intensite = intensite;
		this.positionN = positionN;
		this.positionM = positionM;
	}
	public Pixel(double intensite) {
		this.intensite = intensite;
		
	}
	
	public final double getIntensite() {
		return intensite;
	}
	public final void setIntensite(double intensite) {
		this.intensite = intensite;
	}
	public final int getPositionN() {
		return positionN;
	}
	public final void setPositionN(int positionN) {
		this.positionN = positionN;
	}
	public final int getPositionM() {
		return positionM;
	}
	public final void setPositionM(int positionM) {
		this.positionM = positionM;
	}
	
	
}
