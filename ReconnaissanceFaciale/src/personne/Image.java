package personne;

import java.awt.Graphics2D;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Image {
	protected String nomImage;

	//Constructeur
	public Image(String nomImage) {
		this.nomImage = nomImage;
	}
	
	public String getNomImage() {
		return nomImage;
	}

	public void setNomImage(String nomImage) {
		this.nomImage = nomImage;
	}	
	
	//Méthode pour redimensionner la taille d'une image et la passer en niveau de gris
	public void changeSize() {
		String outImg;
		int w, h;
		
		//OutImg est l'image après modifications
		outImg = this.getNomImage();
		//La taille de l'image après modification sera de 70x70
		w = 70;
		h = 70;
		
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
	
	@Override
	public String toString() {
		return(this.getNomImage());
	}
	
	
}
