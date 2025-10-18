package geste;

import java.awt.Point;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import algebre.Vecteur;
import algebre.Vecteur2D;
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
		double[] featuresList = new double[13];

		// Vérification du nombre de points
		if (points == null || points.size() < 2) {
            return;
        }

		// 1. Cosinus de l'angle alpha (début du tracé)
        StampedCoord p0 = points.get(0); // Premier point
        StampedCoord p1 = points.get(1); // Deuxième point

        // 1. Cosinus de l'angle alpha (début du tracé)
        this.features = new Vecteur(13);
        Vecteur2D v1 = new Vecteur2D(points.get(0).getX(),points.get(0).getY());
        Vecteur2D v2 = new Vecteur2D(points.get(2).getX(),points.get(2).getY());
        featuresList[0] = v1.cosinus(v2);

        // 2. Sinus alpha
        featuresList[1] = v1.sinus(v2);

        // 3. Longueur de la diagonale du rectangle englobant
        double minx, miny, maxx, maxy;
        minx = points.getFirst().x;
        maxx = points.getFirst().x;
        miny = points.getFirst().y;
        maxy = points.getFirst().y;
        for (StampedCoord p : points) {
            if (p.x < minx)
                minx = p.x;
            if (p.y < miny)
                miny = p.y;
            if (p.x > maxx)
                maxx = p.x;
            if (p.y > maxy)
                maxy = p.y;
        }
		featuresList[2] = Math.sqrt((maxx - minx) * (maxx - minx) + (maxx - minx) * (maxy - miny));

        // 4. Orientation de la diagonale du rectangle englobant (gamma)
        featuresList[3] = Math.atan2(maxy - miny, maxx - minx);

        // 5. Distance entre le premier et le dernier point
        StampedCoord pn = points.get(points.size() - 1); // Dernier point
        double f5 = Math.sqrt((pn.x - p0.x) * (pn.x - p0.x) + (pn.y - p0.y) * (pn.y - p0.y));
        featuresList[4] = f5;

        // 6. Cosinus de l'angle de fin de tracé (beta)
        featuresList[5] = (pn.x - p0.x) / f5;

        // 7. Sinus beta
        featuresList[6] = (pn.y - p0.y) / f5;

        // 8. Longueur totale de la courbe
        double sum = 0;
        for(int i=0; i < points.size()-2; i++) {
            StampedCoord pi = points.get(i);
            StampedCoord pi1 = points.get(i+1);
            double deltaXp = pi1.x - pi.x;
            double deltaYp = pi1.y - pi.y;
            sum += Math.sqrt(Math.pow(deltaXp,2) + Math.pow(deltaYp,2));
        }
        featuresList[7] = sum;

        // 9. Somme des angles entre deux vecteurs consécutifs du tracé
        double sum2 = 0;
        for(int i=1; i < points.size()-2; i++) {
            StampedCoord pi = points.get(i);
            StampedCoord pi1 = points.get(i+1);
            StampedCoord piMoins1 =  points.get(i-1);
            double deltaXp = pi1.x - pi.x;
            double deltaYp = pi1.y - pi.y;
            double deltaXpMoins1 = pi.x - piMoins1.x;
            double deltaYpMoins1 = pi.y - piMoins1.y;
            sum2 += Math.atan2(deltaXp * deltaYpMoins1 - deltaXpMoins1 * deltaYpMoins1,
                              deltaXp * deltaXpMoins1 +  deltaYp * deltaYpMoins1);
        }
        featuresList[8] = sum2;

        // 10. Somme des valeurs absolues des angles entre deux vecteurs consécutifs du tracé
        double sum3 = 0;
        for(int i=1; i < points.size()-2; i++) {
            StampedCoord pi = points.get(i);
            StampedCoord pi1 = points.get(i+1);
            StampedCoord piMoins1 =  points.get(i-1);
            double deltaXp = pi1.x - pi.x;
            double deltaYp = pi1.y - pi.y;
            double deltaXpMoins1 = pi.x - piMoins1.x;
            double deltaYpMoins1 = pi.y - piMoins1.y;
            sum3 += Math.abs(Math.atan2(deltaXp * deltaYpMoins1 - deltaXpMoins1 * deltaYpMoins1,
                                       deltaXp * deltaXpMoins1 +  deltaYp * deltaYpMoins1));
        }
        featuresList[9] = sum3;

        // 11. Somme des carrés des angles entre deux vecteurs consécutifs du tracé
        double sum4 = 0;
        for(int i=1; i < points.size()-2; i++) {
            StampedCoord pi = points.get(i);
            StampedCoord pi1 = points.get(i+1);
            StampedCoord piMoins1 =  points.get(i-1);
            double deltaXp = pi1.x - pi.x;
            double deltaYp = pi1.y - pi.y;
            double deltaXpMoins1 = pi.x - piMoins1.x;
            double deltaYpMoins1 = pi.y - piMoins1.y;
            sum4 += Math.pow(Math.atan2(deltaXp * deltaYpMoins1 - deltaXpMoins1 * deltaYpMoins1,
                    deltaXp * deltaXpMoins1 +  deltaYp * deltaYpMoins1), 2);
        }
        featuresList[10] = sum4;

        // 12. Vitesse max : max de la vitesse calculée pour chaque vecteur du tracé
        double max = Double.MIN_VALUE;
        for(int i=0; i < points.size()-2; i++) {
            StampedCoord pi = points.get(i);
            StampedCoord pi1 = points.get(i+1);
            double deltaXp = pi1.x - pi.x;
            double deltaYp = pi1.y - pi.y;
            double deltaT = pi1.timeStamp - pi.timeStamp;
            double current = (Math.pow(deltaXp, 2) +  Math.pow(deltaYp, 2)) / Math.pow(deltaT, 2);
            if(current >= max && deltaT != 0){
                max = current;
            }
        }
        featuresList[11] = max;

        // 13. Temps total mis pour réaliser le tracé
        featuresList[12] = pn.timeStamp - p0.timeStamp;

		features = new Vecteur(featuresList);
	}

	public Vecteur getFeatureVector() {
		return new Vecteur(features);
	}

	public int size() {
		return points.size();
	}

}
