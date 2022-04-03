package resize;
import java.awt.Graphics2D;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;

import javax.imageio.ImageIO;
import java.io.*;
 
public class Size 
{
 public static void changeSize(String inImg, String outImg, int w, int h)
 throws IOException 
 {
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
 
 
 public static void main(String[] args) 
 {
        String inImg = ".jpg";
        String outImg = ".jpg";
 
        try 
        {
            int width = 960;
            int height = 960;
            Size.changeSize(inImg, outImg, width, height);
        }
        catch (IOException ex) 
        {
            ex.printStackTrace();
        }
 }
}
