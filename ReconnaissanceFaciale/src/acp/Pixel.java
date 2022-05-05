package acp;

public class Pixel {
	private double intensite;
	private int positionN;
	private int positionM;
	
	/**
	 * Objectif : Construire un pixel avec l'intensité de donnée, ainsi que sa position
	 * @param  	intensite la valeur du pixel, un double, le niveau de gris
	 * @param	positionN le numéro de la ligne à laquelle est le pixel
	 * @param	positionM le numéro de la colonne à laquelle est le pixel
	 */
	public Pixel(double intensite, int positionN, int positionM) {
		this.intensite = intensite;
		this.positionN = positionN;
		this.positionM = positionM;
	}
	
	/**
	 * Objectif : Construire un pixel avec seulement l'intensité de donnée
	 * @param  intensite la valeur du pixel, un double, le niveau de gris
	 */
	public Pixel(double intensite) {
		this.intensite = intensite;
		
	}
	
	/**
	 * Objectif : Obtenir l'intensité du pixel
	 * @return 	intensite la valeur du pixel
	 */
	public final double getIntensite() {
		return intensite;
	}
	
	/**
	 * Objectif : Modifier la valeur de l'intensité
	 * @param 	intensite la nouvelle valeur de l'intensité
	 */
	public final void setIntensite(double intensite) {
		this.intensite = intensite;
	}
	
	/**
	 * Objectif : Obtenir le numéro de la ligne à laquelle est le pixel
	 * @return 	positionN le numéro de la ligne du pixel
	 */
	public final int getPositionN() {
		return positionN;
	}
	
	/**
	 * Objectif : Modifier la ligne à laquelle se situe le pixel
	 * @param 	positionN la nouvelle ligne du pixel
	 */
	public final void setPositionN(int positionN) {
		this.positionN = positionN;
	}
	
	/**
	 * Objectif : Obtenir le numéro de la colonne à laquelle est le pixel
	 * @return 	positionM le numéro de la colonne du pixel
	 */
	public final int getPositionM() {
		return positionM;
	}
	
	/**
	 * Objectif : Modifier la colonne à laquelle se situe le pixel
	 * @param 	positionM la nouvelle colonne du pixel
	 */
	public final void setPositionM(int positionM) {
		this.positionM = positionM;
	}
	
	
}