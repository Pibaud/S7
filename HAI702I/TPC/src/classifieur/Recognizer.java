package classifieur;

import algebre.Vecteur;
import geste.Geste;
import geste.Lexique;
import geste.Trace;

public interface Recognizer {
	public void init(Lexique l);
	public Geste recognize(Trace t);
	public double squaredMahalanobis(Vecteur t, Vecteur g);
	public double[] test(Lexique lexicon) ;
}
