package classifieur;

import algebre.Matrice;
import algebre.Vecteur;

public interface Estimable {
	public void init();
	public Matrice getCovMatrix();
	public Vecteur getEsperance();
	public void initEstimators(Matrice m);
	public double getBias();
	public Vecteur getWeightVector();
}
