package personne;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import acp.Matrice;
import acp.Pixel;

public class Image {
	protected String nomImage;
	protected Matrice photo;

	//Constructeur
	public Image(String nomImage) {
		this.nomImage = nomImage;
		this.photo =  new Matrice(50,50);
		this.changeSize();
		this.transformationNiveauGris();
	}
	public Image(String nomImage, Matrice mat) {
		this.nomImage = nomImage;
		this.photo = mat;
	}
	
	//Get et set du nom de l'image (= le lien d'où est stocké l'image)
	public String getNomImage() {
		return nomImage;
	}

	public void setNomImage(String nomImage) {
		this.nomImage = nomImage;
	}	
	
	public final Matrice getPhoto() {
		return photo;
	}
	public final void setPhoto(Matrice photo) {
		this.photo = photo;
	}
	//Méthode pour redimensionner la taille d'une image et la passer en niveau de gris
	public void changeSize() {
		String outImg;
		int w, h;
		
		//OutImg est l'image après modifications
		outImg = this.getNomImage();
		//La taille de l'image après modification sera de 70x70
		w = 50;
		h = 50;
		
		// lit l'image d'entrée
		File f = new File(this.getNomImage());
		 	
		try{
			BufferedImage inputImage = ImageIO.read(f);
			 
			// crée l'image de sortie
			BufferedImage img = new BufferedImage(w, h, inputImage.getType());
			 
			// entre l'image d'entrée dans l'image de sortie
			Graphics2D g = img.createGraphics();
			g.drawImage(inputImage, 0, 0, w, h, null);
			g.dispose();
			 
			// extrait l'extension du fichier de sortie
			String name = outImg.substring(outImg.lastIndexOf(".") + 1);
			 
			// écrit dans le fichier de sortie et la met en niveaux de gris 
			ColorConvertOp op = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null); 
			BufferedImage imageGrise = op.filter(img,null);
			ImageIO.write(imageGrise, name, new File(outImg));
		}catch (IOException e){
			System.err.println("Erreur lors de l'ouverture du fichier");
		}
	}
	
	//Méthode pour recupérer une image
	public void transformationNiveauGris() {
		// lit l'image d'entrée
		File f = new File(this.getNomImage());
		try {
			BufferedImage image = ImageIO.read(f);
			for(int x = 0; x < this.photo.getN(); x++) {
				for(int y = 0; y < this.photo.getM(); y++) {
					//on crée une couleur
			        Color couleur = new Color(image.getRGB(x,y));
			        int gris = couleur.getRed();
			        //On crée le pixel lié à la couleur
			        this.photo.getPixels()[x][y] = new Pixel(gris/255d, x ,y);
			    }
			}
		}catch(IOException e) {
			System.err.println("Erreur lecture fichier");
		}	      
	}

	
	@Override
	public String toString() {
		return(this.getNomImage());
	}
	//test
	public static void main(String[] args) {
		Image image = new Image("../BDD/Train/SALL_Marieme/SALL_Marieme_2.jpg");
		image.changeSize();
		image.getPhoto().affichage();
	}
	
}
