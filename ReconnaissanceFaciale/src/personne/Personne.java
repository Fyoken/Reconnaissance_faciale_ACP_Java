package personne;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class Personne implements Comparable<Personne>{
	private String nom;
	private String prenom;
	private Set<Image> images;
	
	//Constructeur
	public Personne(String nom, String prenom, Set<Image> images) {
		this.nom = nom;
		this.prenom = prenom;
		this.images = images;
	}
	public Personne(String nom, String prenom) {
		this.nom = nom;
		this.prenom = prenom;
		this.images= new HashSet<Image>();
	}
	
	
	//Get et Set pour le prénom
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	
	
	//Get et set pour le nom
	public String getPrenom() {
		return prenom;
	}
	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	
	//Get et Set pour les images
	public Set<Image> getImages() {
		return images;
	}

	public void setImages(Set<Image> images) {
		this.images = images;
	}
	
	//Méthode permettant d'ajouter directement toutes les images d'une personne dans le set
	public void ajouterImages() {
		//On déclare nom pour stocker l'information du nom et prénom de la personne
		String nomImage = this.getNom() + "_" + this.getPrenom();
		//Pour toutes les images de la base d'entrainement
		for(int i = 0; i <3; i++) {
			//On crée une nouvelle image que l'on va ensuite ajouter au set de la personne concernée
			Image image = new Image("../BDD/Train/" + nomImage + "/" + nomImage + "_" +i+".jpg");
			this.images.add(image);
		}
	}
	
	public int compareTo(Personne pers2) {
		return 0;
	}
	
	public static void main(String[] args) {
		Image image = new Image("../BDD/Train/AUZOLLES_Melina/AUZOLLES_Melina_1.jpg");
		Set<Image> set = new HashSet<Image>();
		set.add(image);
		Personne moi = new Personne("AUZOLLES", "Melina");
		
		moi.ajouterImages();
	}
	
}
