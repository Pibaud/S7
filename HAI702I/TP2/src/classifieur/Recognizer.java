package classifieur;

import geste.Geste;
import geste.Lexique;
import geste.Trace;

public interface Recognizer {
    public void init(Lexique l);
    public Geste recognize(Trace t);
    public double[] test(Lexique lexicon) ;
    public double testGeste(Geste g);
    public double testLexique(Lexique lexicon);
}
