package image;

public class Couple {
	
	private int valeurGris;
	private int poidsGris;
	
	public Couple(int valeurGris){
		this.valeurGris = valeurGris;
		this.poidsGris =1;
	}
	
	public Couple(int valeurGris, int poids){
		this.valeurGris = valeurGris;
		this.poidsGris = poids;
	}

	public int getValeurGris(){
		return this.valeurGris;
	}
	
	public int getPoidsGris(){
		return this.poidsGris;
	}
	
	public void setPoidsGris(int val){
		this.poidsGris = val;
	}
	
	public void incrementePoids(){
		this.poidsGris++;
	}
	
	public int compareTo(Couple cpl){
		return (this.getValeurGris()>cpl.getValeurGris()?1:-1);
	}
}
