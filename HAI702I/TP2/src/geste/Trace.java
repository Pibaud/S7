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
	private String path;

	public Trace(boolean model) {
		points = new ArrayList<StampedCoord>();
		this.model = model;
	}

	public Trace(boolean model, String fileName) {
		this(model);
		this.path = fileName;
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
        double[] featuresList = new double[11];
        int n = points.size();
        if (n < 3) {
            features = new Vecteur(featuresList);
            return;
        }

        StampedCoord p0 = points.get(0);
        StampedCoord p1 = points.get(1);
        StampedCoord p2 = points.get(2);
        StampedCoord pn = points.get(n - 1);

        // f1, f2
        double dx01 = p1.x - p0.x;
        double dy01 = p1.y - p0.y;
        double norm01 = Math.hypot(dx01, dy01);
        featuresList[0] = (norm01 != 0) ? dx01 / norm01 : 0; // cos(angle avec l'axe X)
        featuresList[1] = (norm01 != 0) ? dy01 / norm01 : 0; // sin(angle avec l'axe X)

        // f3, f4
        double minx = p0.x, maxx = p0.x, miny = p0.y, maxy = p0.y;
        for (StampedCoord p : points) {
            if (p.x < minx) minx = p.x;
            if (p.x > maxx) maxx = p.x;
            if (p.y < miny) miny = p.y;
            if (p.y > maxy) maxy = p.y;
        }
        featuresList[2] = Math.hypot(maxx - minx, maxy - miny);
        featuresList[3] = Math.atan2(maxy - miny, maxx - minx);

        // f5
        double dxn = pn.x - p0.x;
        double dyn = pn.y - p0.y;
        double dist = Math.hypot(dxn, dyn);
        featuresList[4] = dist;

        // f6, f7
        featuresList[5] = (dist != 0) ? dxn / dist : 0;
        featuresList[6] = (dist != 0) ? dyn / dist : 0;

        // f8
        double sumLen = 0;
        for (int i = 0; i < n - 1; i++) {
            double dx = points.get(i + 1).x - points.get(i).x;
            double dy = points.get(i + 1).y - points.get(i).y;
            sumLen += Math.hypot(dx, dy);
        }
        featuresList[7] = sumLen;

        // f9, f10, f11
        double sumTheta = 0, sumAbsTheta = 0, sumCarreTheta = 0;
        for (int i = 1; i < n - 1; i++) {
            double dx1 = points.get(i).x - points.get(i - 1).x;
            double dy1 = points.get(i).y - points.get(i - 1).y;
            double dx2 = points.get(i + 1).x - points.get(i).x;
            double dy2 = points.get(i + 1).y - points.get(i).y;

            double num = dx2 * dy1 - dx1 * dy2;
            double den = dx2 * dx1 + dy2 * dy1;
            double theta = Math.atan2(num, den);

            sumTheta += theta;
            sumAbsTheta += Math.abs(theta);
            sumCarreTheta += theta * theta;
        }
        featuresList[8] = sumTheta;
        featuresList[9] = sumAbsTheta;
        featuresList[10] = sumCarreTheta;

//        // f12
//        double maxSpeed = Double.NEGATIVE_INFINITY; // macro pour la + petite valeur possible
//        for (int i = 0; i < n - 1; i++) {
//            double dx = points.get(i + 1).x - points.get(i).x;
//            double dy = points.get(i + 1).y - points.get(i).y;
//            double dt = points.get(i + 1).timeStamp - points.get(i).timeStamp;
//            if (dt != 0) {
//                double speed = (dx * dx + dy * dy) / (dt * dt);
//                if (speed > maxSpeed) maxSpeed = speed;
//            }
//        }
//        featuresList[11] = maxSpeed;
//
//        // f13
//        featuresList[12] = pn.timeStamp - p0.timeStamp;
//
//        features = new Vecteur(featuresList);
    }

	public Vecteur getFeatureVector() {
        if (features == null) {
            initFeatures();
        }
        return new Vecteur(features);
	}

	public int size() {
		return points.size();
	}

}
