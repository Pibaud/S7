package geste;

import java.util.ArrayList;

import algebre.Matrice;
import algebre.Vecteur;
import algebre.Vecteur2D;

public class Geste {
	private String nom;
	private ArrayList<Trace> traces; 
	private Trace modele;
	private Matrice covariance;
	private Vecteur esperance;
	private Vecteur weightVector;
	private double bias;
	
	public Geste(String nom, Trace model) {
		this.nom = nom;
		this.modele = model;
		this.traces = new ArrayList<Trace>();
	}
		
	public void init() {
        /*
            calculer le vecteur de features pour chaque trace associée au geste appelant, la méthode initFeatures();de la classe Trace
    calculer le vecteur moyen des vecteurs de features représentant les tracés du geste appelant. Et, mémoriser le résultat dans l'attribut privé esperance de la classe Geste
    calculer la matrice de covariance construite d'après les vecteurs de features représentant les tracés (échantillons issus de l'apprentissage) du geste appelant, et mémoriser le résultat dans l'attribut privé covariance de la classe Geste

         */
		Lexique l = new Lexique();
        l.initData();
        ArrayList<Vecteur> vecteurs = new ArrayList<Vecteur>();

        for(Trace trace : traces) {
            trace.initFeatures();
            vecteurs.add(trace.getFeatureVector());
        }

        esperance = Vecteur.esperance(vecteurs);
        covariance = Matrice.covariance(vecteurs);
	}

	public ArrayList<Trace> getTraces() {
		return this.traces;
	}

	public void addTrace(Trace t) {
		traces.add(t);	
	}

	public Trace get(int i) {
		return traces.get(i);
	}

	public void clear() {
		traces.clear();		
	}

	public String getName() {
		return nom;
	}

	public Matrice getCovariance() {
		return covariance;
	}

	public void initEstimators(Matrice inverseEotccm) {
		weightVector = inverseEotccm.mult(this.esperance);
		bias = -0.5*this.esperance.produitScalaire(weightVector);
	}

	public double getBias() {
		return bias;
	}

	public Vecteur getWeightVector() {
		return weightVector;
	}

	public Vecteur getEsperance() {
		return esperance;
	}

	@Override
	public String toString() {
		return "Geste{" +
				"nom='" + nom + '\'' +
				", traces=" + traces +
				", modele=" + modele +
				", covariance=" + covariance +
				", esperance=" + esperance +
				", weightVector=" + weightVector +
				", bias=" + bias +
				'}';
	}
}
