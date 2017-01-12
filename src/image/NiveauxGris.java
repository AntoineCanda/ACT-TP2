package image;
import image.Couple;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class NiveauxGris {

	
	private List<Couple> liste;
	private int tab[][];
	private int taille;
	private int tabDistImageMin[][];
	
	public NiveauxGris(){
		this.liste = new ArrayList<Couple>();
		//this.tab = new int[this.liste.size()][this.liste.size()];
		this.tab = new int[7][7];
		this.taille = 7;
	}
	
	public NiveauxGris(ArrayList<Couple> liste, int nombreCouleur){
		this.liste = liste;
		this.taille = liste.size();
		this.tab = new int[this.taille][this.taille];
		//Initialise la table a -1
		for(int i=0; i< this.taille; i++){
			for(int j=0; j<this.taille;j++){
				this.tab[i][j] = -1;
			}
		}
		this.tabDistImageMin = new int[this.taille][nombreCouleur];
		//Initialise la table a -1
		for(int i=0; i<this.taille;i++){
			for(int j=0; j < nombreCouleur;j++){ 
				this.tabDistImageMin[i][j] = -1;
			}
		}
	}
	
	public void addCoupleListe(Couple cpl){
		this.liste.add(cpl);
	}
	
	public int meilleurGris(int debut, int fin){
		
		int sommePoids = 0;
		int sommeVal = 0;
		for(int i = debut; i <= fin; i++){
			Couple cpl = this.liste.get(i);
			sommePoids += cpl.getPoidsGris();
			sommeVal += cpl.getValeurGris()*cpl.getPoidsGris();
		}
			
		return Math.round(sommeVal/sommePoids);
	}
	
	public int distanceMin(int debut, int fin, int valGris){
		int res = 0;
		for(int i=debut; i <= fin; i++){
			Couple cpl = this.liste.get(i);
			res+=((cpl.getValeurGris()-valGris)*(cpl.getValeurGris()-valGris))*cpl.getPoidsGris();
		}
		return res;
	}
	
	public int distanceTotalMin(int debut, int nombreCouleur){
		int min = Integer.MAX_VALUE;
		if(nombreCouleur == 1){
			if(this.tab[debut][this.taille-1] == -1){
				int valGris = this.meilleurGris(debut, this.taille-1);
				this.tab[debut][this.taille-1] = this.distanceMin(debut, this.taille-1, valGris);
			}
			return this.tab[debut][this.taille-1];
		}
		
		for(int j=debut; j < this.taille - (nombreCouleur-1); j++){
		
			if(this.tab[debut][j] == -1){
				int valGris = this.meilleurGris(debut,j);
				this.tab[debut][j] = this.distanceMin(debut,j, valGris);
			}
			if(this.tabDistImageMin[j+1][nombreCouleur-1] == -1){
				this.tabDistImageMin[j+1][nombreCouleur -1] =  this.distanceTotalMin(j+1, nombreCouleur-1);
			}
			int res = this.tabDistImageMin[j+1][nombreCouleur-1] + this.tab[debut][j];
			
			if(min > res){
				min = res;
			}
		}		
		return min;
	}
	

	public void parcoursTableau(int k){
		System.out.println("test");
		for(int i=0; i<this.liste.size();i++){
			for(int j=0; j<k;j++){
				if(k==3){
					System.out.println("A tab["+i+"]["+j+"], contribution = "+this.tabDistImageMin[i][j]+".");
				}
				else{
					System.out.println("A tab["+i+"]["+j+"], contribution = "+this.tab[i][j]+".");
				}
			}
		}
	}
	
	
	public ArrayList<Interval> getInterval(int nombreCouleur, int res){
		ArrayList<Interval> listeInterval = new ArrayList<Interval>();
		int resultat = res;
		int debut = 0;
		for(int i = nombreCouleur-1; i>0; i--){
			for(int j = debut+1; j <= this.taille-i; j++){
				//System.out.println("res =" + resultat + " debut = " + debut +" i = "+ i+ " j = "+j);
				if(this.tab[debut][j-1] + this.tabDistImageMin[j][i] == resultat){
					Interval interval = new Interval(debut, j-1);
					listeInterval.add(interval);
				
					resultat = this.tabDistImageMin[j][i];
					debut = j;
					break;
				}
			}
		}
		Interval interval = new Interval(debut,this.taille-1);
		listeInterval.add(interval);
		return listeInterval;
	}

	
	public ArrayList<Integer> getCouleurFinal(ArrayList<Interval> listeInterval){
		ArrayList<Integer> listeValeur = new ArrayList<Integer>();
		
		for(Interval inter : listeInterval){
			int val = this.meilleurGris(inter.getDebut(), inter.getFin());
			listeValeur.add(val);
		}
		return listeValeur;
	}
	
	/**
	 * On va creer une map qui pour chaque valeur des couleurs initiales associe la valeur de la couleur finale.
	 * @param listeInterval
	 * @param listeValeurCoulRes
	 * @return map a HashMap<Integer,Integer>
	 */
	public HashMap<Integer,Integer> createNewPaletteGris(ArrayList<Interval> listeInterval, ArrayList<Integer> listeValeurCoulRes ){
		HashMap<Integer,Integer> map = new HashMap<Integer,Integer>();
		int j = 0;
		for(Interval interval : listeInterval){
			for(int i = interval.getDebut(); i < interval.getFin()+1;i++){
				map.put(this.liste.get(i).getValeurGris(), listeValeurCoulRes.get(j));
			}
			j++;
		}
		return map;
	}
	
	
	public static void main(String[] args){
		Couple cpl0 = new Couple(0);
		cpl0.setPoidsGris(5);
		Couple cpl1 = new Couple(20);
		cpl1.setPoidsGris(5);
		Couple cpl2 = new Couple(100);
		Couple cpl3 = new Couple(132);
		Couple cpl4 = new Couple(164);
		cpl4.incrementePoids();
		Couple cpl5 = new Couple(180);
		Couple cpl6 = new Couple(255);
		cpl6.setPoidsGris(10);
			
		ArrayList<Couple> liste = new ArrayList<Couple>();
		liste.add(cpl0);
		liste.add(cpl1);
		liste.add(cpl2);
		liste.add(cpl3);
		liste.add(cpl4);
		liste.add(cpl5);
		liste.add(cpl6);

		
		NiveauxGris nG = new NiveauxGris(liste,3);
		
		

		
		int res = nG.meilleurGris(0,1);
		System.out.println("gris 1 = "+res);
		
		int res2 = nG.meilleurGris(2,5);
		System.out.println("gris 2 = " +res2);
		
		int res3 = nG.meilleurGris(6,6);
		System.out.println("gris 3 = " +res3);
		
		int res4 = nG.distanceMin(0, 1, res);
		System.out.println("distance min 1 = "+ res4);
		
		int res5 = nG.distanceMin(2, 5, res2);
		System.out.println("distance min 2 = "+ res5);
		
		int res6 = nG.distanceMin(6, 6, res3);
		System.out.println("distance min 3 = "+ res6);
		
		int res7 = nG.distanceTotalMin(0,3);
		System.out.println(res7);


		nG.parcoursTableau(3);
		nG.parcoursTableau(7);
		ArrayList<Interval> listeI = nG.getInterval(3, res7);
		
		for(Interval inter : listeI){
			System.out.println("Interval debut: "+inter.getDebut()+" fin : "+inter.getFin());
		}
		
	}
}
