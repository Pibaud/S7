package classifieur;

import algebre.Matrice;
import algebre.Vecteur;
import geste.Geste;
import geste.Lexique;
import geste.Trace;

import java.util.ArrayList;

public class Rubine implements Recognizer{
	private Lexique lexicon;
	private Matrice eotccm; // estimate of the common covariance matrix
	private Matrice inverseEotccm; // inverse of eotccm
	
	
	public Rubine() {
	}

	public void init(Lexique l) {
		this.lexicon = l;
		ArrayList<Matrice>covs = new ArrayList<Matrice>();
		for(Geste geste : l.getGestes()){

			geste.init();
			covs.add(geste.getCovariance());
		}

		eotccm = Matrice.esperance(covs);
		inverseEotccm = eotccm.inverseOptimized();

		for(Geste geste : l.getGestes()){
			geste.initEstimators(this.inverseEotccm);
		}

        //esperance des matrices de covariance

	}

	@Override
	//le lexique passé en paramètre doit être initialisé avant l'appel à test
	public double[] test(Lexique lexicon) {
		//todo
		return null; //a changer
	}

	double f(Vecteur x, Geste k){
		return k.getWeightVector().produitScalaire(x) + k.getBias();
	}

	@Override
	public Geste recognize(Trace t) {
		Vecteur g = t.getFeatureVector();
		double maxScore = Double.MIN_VALUE;
		Geste gesteReconnu = null;
		for(Geste j : lexicon.getGestes()){
			if(squaredMahalanobis(g, j.getEsperance()) < 5*g.getDimension()){
				double score = f(g, j);
				if(score > maxScore){
					maxScore = score;
					gesteReconnu = j;
				}
			}
		}
		return gesteReconnu;
	}

	public double squaredMahalanobis(Vecteur t, Vecteur g) {
		Vecteur tMoinsg = t.minus(g);
		Vecteur sigmatMoinsg = this.inverseEotccm.mult(tMoinsg);
		// par associativité du produit matriciel, si la multiplication est (t-g)^T*sigma*(t-g)
		// nous pouvons d'abord calculer sigma*(t-g)
		// puis calculer <(t-g), sigma*(t-g)> sans avoir à coder la transposée
		// (car ramené à des vecteurs, le produit matriciel correspond à un produit scalaire)
		return  tMoinsg.produitScalaire(sigmatMoinsg);
	}

	public double testGeste(Geste g) {
		double reconnus = 0;
		for(Trace t : g.getTraces()){
			if(g.equals(recognize(t))){
				reconnus ++;
			}
		}
		return reconnus / g.getTraces().size();
	}

	public double testLexique(Lexique lexicon) {
		double sum = 0;
		for(Geste g : lexicon.getGestes()){
			sum += testGeste(g);
		}
		return sum / lexicon.getGestes().size();
	}
}
