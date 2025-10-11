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

	// précondition - les vecteurs de la liste ont tous la meme dimension
	public static Vecteur esperance(ArrayList<Vecteur> lv) {
		if (lv.size() == 0)
			return null;
		int dimension = lv.get(0).getDimension();
		double resultat[] = new double[dimension];
		for (Vecteur v : lv) {
			for (int i = 0; i < dimension; i++) {
				resultat[i] += v.coords[i];
			}
		}

		for (int i = 0; i < dimension; i++) {
			resultat[i] /= dimension;
		}

		return new Vecteur(resultat);
	}

	// précondition - les deux vecteurs (this et v) sont de même dimension
	public double produitScalaire(Vecteur v) {
		int somme = 0;
		int n = v.getDimension();
		for (int i = 0; i < n; i++) {
			somme += this.get(i) * v.get(i);
		}
		return somme; // todo: return the results of your computation instead
	}

	public double normeCarre() {
		double res = 0;
		int n = this.getDimension();
		for (int i = 0; i < n; i++) {
			res += Math.pow(this.get(i), 2);
		}
		return res;
	}

	public double norme() {
		double res = Math.sqrt(this.normeCarre());
		return res;
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

	public String toString() {
		String result = "";
		for (int i = 0; i < coords.length; i++) {
			result += this.coords[i] + ", ";
		}
		return result;
	}

	public void print() {
		System.out.println(this);

	}

	// Approfondissement

	/*
	 * Intérêt : Calcule la distance euclidienne (distance « à vol d’oiseau ») entre
	 * deux vecteurs.
	 * C’est la mesure la plus classique pour comparer la similarité ou la
	 * différence entre deux points
	 * dans un espace vectoriel.
	 * 
	 * Utilité : Permet de savoir à quel point deux tracés (ou points) sont proches
	 * dans l’espace.
	 */

	public double distanceEuclidienne(Vecteur v) {
		if (this.getDimension() != v.getDimension())
			throw new IllegalArgumentException("Dimensions différentes");
		double somme = 0;
		for (int i = 0; i < this.getDimension(); i++) {
			double diff = this.get(i) - v.get(i);
			somme += diff * diff;
		}
		return Math.sqrt(somme);
	}

	/*
	 * Intérêt : Calcule le cosinus de l’angle entre deux vecteurs. Cette mesure est
	 * comprise
	 * entre -1 et 1 et indique si deux vecteurs pointent dans la même direction
	 * (1), dans des directions
	 * opposées (-1), ou sont orthogonaux (0).
	 * 
	 * Utilité : Très utilisée pour comparer des formes ou des directions
	 * indépendamment de leur norme (longueur).
	 * Pratique pour la reconnaissance de motifs où seule la direction compte.
	 */

	public double similariteCosinus(Vecteur v) {
		double prod = this.produitScalaire(v);
		double norme1 = this.norme();
		double norme2 = v.norme();
		if (norme1 == 0 || norme2 == 0)
			return 0;
		return prod / (norme1 * norme2);
	}

	/*
	 * Intérêt : Calcule la somme des valeurs absolues des différences entre les
	 * coordonnées (aussi appelée distance L1). C’est la distance parcourue « en
	 * damier » (comme dans une ville à rues perpendiculaires).
	 * Utilité : Moins sensible aux grandes différences sur une seule coordonnée que
	 * la distance euclidienne. Parfois plus robuste aux outliers.
	 */

	public double distanceManhattan(Vecteur v) {
		if (this.getDimension() != v.getDimension())
			throw new IllegalArgumentException("Dimensions différentes");
		double somme = 0;
		for (int i = 0; i < this.getDimension(); i++) {
			somme += Math.abs(this.get(i) - v.get(i));
		}
		return somme;
	}

	/*
	 * Intérêt : Calcule la moyenne des coordonnées du vecteur.
	 * Utilité : Sert à centrer ou normaliser les données, ou à obtenir une valeur
	 * représentative du vecteur.
	 */

	public double moyenne() {
		double somme = 0;
		for (int i = 0; i < this.getDimension(); i++) {
			somme += this.get(i);
		}
		return somme / this.getDimension();
	}

	/*
	 * Intérêt : Transforme le vecteur pour qu’il ait une norme de 1 (même
	 * direction, mais longueur 1).
	 * Utilité : Permet de comparer des vecteurs uniquement selon leur direction,
	 * pas leur longueur. Très utile avant de calculer la similarité cosinus.
	 */

	public Vecteur normaliser() {
		double n = this.norme();
		if (n == 0)
			throw new ArithmeticException("Norme nulle");
		double[] res = new double[this.getDimension()];
		for (int i = 0; i < this.getDimension(); i++) {
			res[i] = this.get(i) / n;
		}
		return new Vecteur(res);
	}

	/**
	 * Retourne un vecteur centré (moyenne nulle)
	 */
	public Vecteur centrer() {
		double moy = this.moyenne();
		double[] res = new double[this.getDimension()];
		for (int i = 0; i < this.getDimension(); i++) {
			res[i] = this.get(i) - moy;
		}
		return new Vecteur(res);
	}

	/**
	 * Calcule l'écart-type des coordonnées du vecteur
	 */
	public double ecartType() {
		double moy = this.moyenne();
		double somme = 0;
		for (int i = 0; i < this.getDimension(); i++) {
			double diff = this.get(i) - moy;
			somme += diff * diff;
		}
		return Math.sqrt(somme / this.getDimension());
	}

	/**
	 * Calcule la distance de Mahalanobis entre deux vecteurs, avec matrice de covariance donnée
	 */
	public double distanceMahalanobis(Vecteur v, Matrice covariance) {
		if (this.getDimension() != v.getDimension())
			throw new IllegalArgumentException("Dimensions différentes");
		Vecteur diff = new Vecteur(this.getDimension());
		for (int i = 0; i < this.getDimension(); i++) {
			diff.set(i, this.get(i) - v.get(i));
		}
		Matrice invCov = covariance.inverse();
		Vecteur tmp = invCov.mult(diff);
		double somme = 0;
		for (int i = 0; i < this.getDimension(); i++) {
			somme += diff.get(i) * tmp.get(i);
		}
		return Math.sqrt(somme);
	}

}
