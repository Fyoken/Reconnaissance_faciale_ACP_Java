package resize;
import java.awt.Graphics2D;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;

import javax.imageio.ImageIO;
import java.io.*;
 
public class Size 
{
	final static int n = 100;
	public static void changeToBW(String inImg, String outImg, int w, int h) throws IOException {
      // lit l'image d'entrée
      File f = new File(inImg);
      BufferedImage inputImage = ImageIO.read(f);
 
      // crée l'image de sortie
      BufferedImage img = new BufferedImage(w, h, inputImage.getType());
 
      // balancer l'image d'entrée à l'image de sortie
      Graphics2D g = img.createGraphics();
      g.drawImage(inputImage, 0, 0, w, h, null);
      g.dispose();
 
      // extrait l'extension du fichier de sortie
      String name = outImg.substring(outImg.lastIndexOf(".") + 1);
 
      // écrit dans le fichier de sortie et la met en niveaux de gris 
      ColorConvertOp op = new ColorConvertOp( 
 	         ColorSpace.getInstance(ColorSpace.CS_GRAY), 
 	         null); 
      BufferedImage imageGrise = op.filter(img,null);
      ImageIO.write(imageGrise, name, new File(outImg));
 }
 
 public static void imageResize(String inImg, String outImg, int w, int h) throws IOException {
	 File f = new File(inImg);
     BufferedImage inputImage = ImageIO.read(f);

     // crée l'image de sortie
     BufferedImage img = new BufferedImage(n, n, inputImage.getType());

     // balancer l'image d'entrée à l'image de sortie
     Graphics2D g = img.createGraphics();
     g.drawImage(inputImage, 0, 0, n, n, null);
     g.dispose();

     // extrait l'extension du fichier de sortie
     String name = outImg.substring(outImg.lastIndexOf(".") + 1);
     ImageIO.write(img, name, new File(outImg));
 }
 
 
 public static void main(String[] args) throws IOException 
 {
	 	String[] imgE = new String[6];
        for (int i = 0; i < 2; i++) {
        	int j = i+7;
        	imgE[i] = j+".jpg";
        	File f = new File(imgE[i]);
            BufferedImage inputImage = ImageIO.read(f);
     
            try 
            {
                int width = inputImage.getWidth();
                int height = inputImage.getHeight();
                changeToBW(imgE[i], imgE[i], width, height);
                imageResize(imgE[i], imgE[i], width, height);
            }
            catch (IOException ex) 
            {
                ex.printStackTrace();
            }
        }
 }
}