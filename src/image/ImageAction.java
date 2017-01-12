package image;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import javax.imageio.ImageIO;

public class ImageAction {
	
	private BufferedImage image;
	
	public ImageAction(String file) throws IOException{
		this.image = ImageIO.read(new File(file));
	}
	
	public Color[][] loadPixelsFromImage(){

        Color[][] colors = new Color[this.image.getWidth()][this.image.getHeight()];

        for (int x = 0; x < this.image.getWidth(); x++) {
            for (int y = 0; y < this.image.getHeight(); y++) {
                colors[x][y] = new Color(this.image.getRGB(x, y));
            }
        }

        return colors;
    }

	   public ArrayList<Couple> getHistogramFromImage() {
	        
	        int histogramme[] = new int[256];

	        for (int x = 0; x < this.image.getWidth(); x++) {
	           for (int y = 0; y < this.image.getHeight(); y++) {
	            	Color color = new Color(this.image.getRGB(x, y));
	            	int value = color.getBlue();
	            	histogramme[value]++;
	            }
	        }

	        ArrayList<Couple> liste = new ArrayList<Couple>();
	        
	        for (int i=0; i<256;i++){
	        	if(histogramme[i]!=0){
		        	Couple cpl = new Couple(i,histogramme[i]);
		            liste.add(cpl);
	        	}
	        }
	        return liste;
	    }
	   
	   public void createNewImage(String nameFile,Color[][] colors, Map<Integer,Integer> newPalette) throws IOException{
		  
		
		   for(int x=0; x < this.image.getWidth(); x++){
			   for(int y=0; y < this.image.getHeight(); y++){
				   int newValue = newPalette.get(colors[x][y].getBlue());
				   Color color =new Color(newValue,newValue,newValue);
				   this.image.setRGB(x, y, color.getRGB());
			   }
		   }
		   
		   File f = new File(nameFile);
		   		 
		   ImageIO.write(this.image, "png", f);
		
	   }

	    public static void main(String[] args) throws IOException {
	    	ImageAction iA = new ImageAction("image/baboon_bray_reduit.png");
	        ArrayList<Couple> liste = iA.getHistogramFromImage();
	        
	        for(Couple cpl : liste){
		          System.out.println(cpl.getValeurGris() + " : " + cpl.getPoidsGris());
	        }
	        System.out.println(liste.size());
	        
	        Color[][] colors = iA.loadPixelsFromImage();
	        System.out.println("Color[0][0] = " + colors[0][0]);
	        
	        
	    }
}
