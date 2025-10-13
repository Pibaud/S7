package classifieur;

import algebre.Matrice;
import algebre.Vecteur;
import geste.Geste;
import geste.Lexique;
import geste.Trace;

public class Rubine implements Recognizer{
	private Lexique lexicon;
	private Matrice eotccm; // estimate of the common covariance matrix
	private Matrice inverseEotccm; // inverse of eotccm
	
	
	public Rubine() {		
	}

	public void init(Lexique l) {
		//todo
	}

	@Override
	//le lexique passé en paramètre doit être initialisé avant l'appel à test
	public double[] test(Lexique lexicon) {
//todo
		return null; //a changer
	}



	@Override
	public Geste recognize(Trace t) {
//todo
		return null; //a changer...
	}


	public double squaredMahalanobis(Vecteur t, Vecteur g) {
	//todo
		return 0;// a changer
	}


}
