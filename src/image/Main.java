package image;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


public class Main {

	public static void main(String[] args) throws IOException{
		// TODO Auto-generated method stub

			ImageAction iA = new ImageAction(args[0]);
	        ArrayList<Couple> liste = iA.getHistogramFromImage();
	        
	         Color[][] colors = iA.loadPixelsFromImage();
	         int k = Integer.parseInt(args[1]);
			 NiveauxGris nG = new NiveauxGris(liste, k );
			 int res = nG.distanceTotalMin(0, k);
			 ArrayList<Interval> listeInterval = nG.getInterval(k, res);
			 ArrayList<Integer> listeValFinal = nG.getCouleurFinal(listeInterval);
			 
			 HashMap<Integer,Integer> newPalette = nG.createNewPaletteGris(listeInterval, listeValFinal);
			 
			 iA.createNewImage(args[2], colors, newPalette);
	}

}
