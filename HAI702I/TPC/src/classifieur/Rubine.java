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
	

	@Override
	public void init(Lexique l) {
		//todo
	}

	@Override
	//le lexique passé en paramètre doit être initialisé avant l'appel à test
	public double[] test(Lexique lexicon) {
		return null; //todo
	}

	@Override
	public Geste recognize(Trace t) {
		return null; //todo
	}

	@Override
	public double squaredMahalanobis(Vecteur t, Vecteur g) {
		return 0; //todo
	}


}
