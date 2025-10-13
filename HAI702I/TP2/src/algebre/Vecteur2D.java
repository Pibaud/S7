package algebre;

import geste.StampedCoord;

public class Vecteur2D extends Vecteur {
	public Vecteur2D(double x, double y) {
		super(2);
		coords[0] = x;
		coords[1] = y;
	}
	
	public Vecteur2D(StampedCoord a, StampedCoord b) {
		super(2);
		coords[0] = b.getX() - a.getX();
		coords[1] = b.getY() - a.getY();
	}

	
// sinus de l'angle de i à this
	public double sinus() {
		double n = this.norme();
		if (n == 0) return 0;
		return coords[1] / n;
	}
	

// cosinus de l'angle de i à this
	public double cosinus() {
		double n = this.norme();
		if (n == 0) return 0;
		return coords[0] / n;
	}
	
// tangente de l'angle de i à this
	public double tangente() {
		return coords[1] / coords[0];
	}
	
// cas particulier ou this est vertical, géré...
	public double angle() {
		if (coords[0] < Global.precision && coords[0] > -Global.precision) // cas particulier
			return coords[1] > 0 ? Math.PI /2: -Math.PI/2;
		return Math.atan(tangente());
	}

	public void setCoords(double x, double y) {
		this.coords[0] = x;
		this.coords[1] = y;
	}
	public double sinus(Vecteur2D v) {
		double n1 = this.norme(), n2 = v.norme();
		if (n1 == 0 || n2 == 0) return 0;
		return det(v) / (n1*n2);
	}
	
	private double det(Vecteur2D v) {
		return this.coords[0] * v.coords[1] - v.coords[0] * this.coords[1];
	}
	
	public double cosinus(Vecteur2D v) {
		double n1 = this.norme(), n2 = v.norme();
		if (n1 == 0 || n2 == 0) return 0;
		return produitScalaire(v) / (n1*n2);
	}
	
	// attention au cas particulier ou this et v forment un angle droit pas géré...
	public double tangente(Vecteur2D v) {
		return det(v) / produitScalaire(v);
	}

	// cas particulier ou this et v forment un angle droit géré...
	public double angle(Vecteur2D s2) {
		double ps = produitScalaire(s2), det = det(s2);
		if (ps < Global.precision && ps > -Global.precision) // cas particulier
			return det > 0 ? Math.PI /2: -Math.PI/2;
		return Math.atan(det/ps); 
	}


}
