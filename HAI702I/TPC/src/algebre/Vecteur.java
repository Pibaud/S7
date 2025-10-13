package algebre;

import java.util.ArrayList;

public class Vecteur {
	double coords[];
	
	public Vecteur(int dimension) {
		coords = new double[dimension];
	}
	
	public Vecteur(double[] bk) {
		coords = new double[bk.length];
		for (int i = 0; i < bk.length; i++)
			coords[i] = bk[i];
	}

	public Vecteur(Vecteur v) {
		coords = new double[v.getDimension()];
		for (int i = 0; i < coords.length; i++)
			coords[i] = v.get(i);
	}

	// version 0 - hypothèse = les vecteurs de la liste ont tous la meme dimension
	public static Vecteur esperance(ArrayList<Vecteur> lv) {
		if (lv.size() == 0) return null;
		int dimension = lv.get(0).getDimension();
		double resultat[] = new double[dimension];
		for (Vecteur v: lv) {
			for (int i = 0; i < dimension; i++) {
				resultat[i] += v.coords[i]; 
			}
		}
		
		for (int i = 0; i < dimension; i++) {
			resultat[i] /= dimension; 
		}
		
		return new Vecteur(resultat);
	}	
	
	// version 0 - hypothèse = les deux vecteurs (this et v) sont de même dimension
	public double produitScalaire(Vecteur v) {
		double resultat = 0;
		for (int i = 0; i < coords.length; i++) {
			resultat += v.coords[i] * this.coords[i]; 
		}
		return resultat;
	}	

	public double norme() {
		double resultat = 0;
		for (int i = 0; i < coords.length; i++) {
			resultat += this.coords[i] * this.coords[i]; 
		}
		return resultat;
	}

	public double get(int i) {
		return coords[i];
	}

	public int getDimension() {
		return this.coords.length;
	}

	public void set(int i, double value) {
		this.coords[i] = value;	
	}

	public Vecteur minus(Vecteur g) {
		double[] resultat = new double[g.getDimension()];
		for (int i = 0; i < coords.length; i++) {
			resultat[i] = this.coords[i] - g.coords[i]; 
		}
		return new Vecteur(resultat);
	}
	
	public String toString() {
		String result = "";
		for (int i = 0; i < coords.length; i++) {
			result += this.coords[i] +", "; 
		}
		return result;
	}
	
}
