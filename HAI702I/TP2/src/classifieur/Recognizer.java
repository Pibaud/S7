package classifieur;

import geste.Geste;
import geste.Lexique;
import geste.Trace;

public interface Recognizer {
	public Geste recognize(Trace t);
	public double[] test(Lexique lexicon) ;
}
