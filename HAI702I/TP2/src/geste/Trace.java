package geste;

import java.awt.Point;
import java.io.File;
import java.util.ArrayList;

import algebre.Vecteur;
import classifieur.Featured;
import io.ReadWritePoint;

public class Trace implements Featured {
	private ArrayList<StampedCoord> points;
	private Vecteur features;
	boolean model;

	public Trace(boolean model) {
		points = new ArrayList<StampedCoord>();
		this.model = model;
	}

	public Trace(boolean model, String fileName) {
		this(model);
		File f = new File(fileName);
		ReadWritePoint rwp = new ReadWritePoint(f);
		points = rwp.read();
	}

	public void add(Point p, long timeStamp) {
		add(new StampedCoord(p.x, p.y, timeStamp));
	}

	public void add(StampedCoord p) {
		points.add(p);
	}

	public void initFeatures() {
		// Initialisation du tableau des features
		double[] featuresArray = new double[7];

		// Vérification du nombre de points
		if (points == null || points.size() < 2) {
			return;
		}

		// Points de référence
		StampedCoord p0 = points.get(0); // Premier point
		StampedCoord p1 = points.get(1); // Deuxième point
		StampedCoord pn = points.get(points.size() - 1); // Dernier point

		// 1. Cosinus de l'angle alpha (début du tracé)
		double dx1 = p1.x - p0.x;
		double dy1 = p1.y - p0.y;
		double norm1 = Math.sqrt(dx1 * dx1 + dy1 * dy1);
		
		features = new Vecteur(featuresArray);
	}

	public Vecteur getFeatureVector() {
		return new Vecteur(features);
	}

	public int size() {
		return points.size();
	}

}
