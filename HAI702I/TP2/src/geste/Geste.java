package geste;

import java.util.ArrayList;

import algebre.Matrice;
import algebre.Vecteur;

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
		//todo	
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

	public void initEstimators(Matrice inverseEotccm) {
		//todo
	}



}
