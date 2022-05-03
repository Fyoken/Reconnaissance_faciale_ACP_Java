package personne;

import java.util.HashSet;
import java.util.Set;

public class Personne{
	private String nom;
	private String prenom;
	private Set<Image> images;
	
	/**
	 * Objectif : Construire une personne avec son nom, prenom, et ses images donnés
	 * @param nom 	nom de la personne
	 * @param prenom  prénom de la personne
	 * 	@param images  les images associées à la personne
	 */
	public Personne(String nom, String prenom, Set<Image> images) {
		this.nom = nom;
		this.prenom = prenom;
		this.images = images;
	}
	
	/**
	 * Objectif : Construire une personne avec son nom et prénom de donnés
	 * @param nom 	nom de la personne
	 * 	@param	prenom  prénom de la personne
	 */
	public Personne(String nom, String prenom) {
		this.nom = nom;
		this.prenom = prenom;
		this.images= new HashSet<Image>();
		this.ajouterImages();
	}
	
	/*
	 * Objectif des méthodes ci-dessous : Créer toutes les personnes que nous avons dans notre base de données
	 * 										en utilisant seulement le nom et le prénom
	 */
	public static final Personne AuzollesM=new Personne("AUZOLLES","Melina");
	public static final Personne BarbosaM=new Personne("BARBOSA","Mathias");
	public static final Personne ChambasM=new Personne("CHAMBAS","Mathilde");
	public static final Personne RibayneM=new Personne("RIBAYNE","Maxime");
	public static final Personne SallM=new Personne("SALL","Marieme");
	public static final Personne BertailsC=new Personne("BERTAILS","Clement");
	public static final Personne BlondeyB=new Personne("BLONDEY","Benjamin");
	public static final Personne DonneyV=new Personne("DONNEY","Vincent");
	public static final Personne JousselinH=new Personne("JOUSSELIN","Hugo");
	public static final Personne LamyM=new Personne("LAMY","Martin");
	public static final Personne LasgleizesD=new Personne("LASGLEIZES","David");
	public static final Personne MongourM=new Personne("MONGOUR","Mateo");
	public static final Personne RibasJ=new Personne("RIBAS","Justine");
	public static final Personne RodriguesS=new Personne("RODRIGUES","Samuel");
	
	
	/**
	 * Objectif : Obtenir le nom de la personne
	 * @return nom (nom de la personne)
	 */
	public String getNom() {
		return nom;
	}
	
	/**
	 * Objectif : Modifier le nom de la personne
	 * @param nom (le nouveau nom de la personne)
	 */
	public void setNom(String nom) {
		this.nom = nom;
	}
	
	/**
	 * Objectif : Obtenir le prénom de la personne
	 * @return prenom (le prénom de la personne)
	 */
	public String getPrenom() {
		return prenom;
	}
	
	/**
	 * Objectif : Modifier le prénom de la personne
	 * @param prenom (le nouveau prénom de la personne)
	 */
	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	/**
	 * Objectif : Obtenir l'ensemble des images de la personne
	 * @return images (l'ensemble des images de la personne)
	 */
	public Set<Image> getImages() {
		return images;
	}

	/**
	 * Objectif : Modifier l'ensemble des images de la personne
	 * @param images (le nouvel ensemble des images de la personne)
	 */
	public void setImages(Set<Image> images) {
		this.images = images;
	}
	
	/**
	 * Objectif : Ajouter directement toutes les images d'une personne à l'ensemble
	 */
	public void ajouterImages() {
		//On déclare nom pour stocker l'information du nom et prénom de la personne
		String nomImage = this.getNom() + "_" + this.getPrenom();
		//Pour toutes les images de la base d'entrainement
		for(int i = 1; i <4; i++) {
			//On crée une nouvelle image que l'on va ensuite ajouter au set de la personne concernée
			Image image = new Image("BDD/Train/" + nomImage + "/" + nomImage + "_" +i+".jpg");
			this.images.add(image);
		}
	}
	
}
