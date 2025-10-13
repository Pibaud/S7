package algebre;

import geste.PointVisible;

public class Vecteur2D extends Vecteur {
	public Vecteur2D(double x, double y) {
		super(2);
		coords[0] = x;
		coords[1] = y;
	}
	public Vecteur2D(PointVisible a, PointVisible b) {
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
	
	public double sinus(Vecteur2D v) {
		double n1 = this.norme(), n2 = v.norme();
		if (n1 == 0 || n2 == 0) return 0;
		return det(v) / (n1*n2);
	}
	
	private double det(Vecteur2D v) {
		return this.coords[0] * v.coords[1] - v.coords[0] * this.coords[1];
	}

// cosinus de l'angle de i à this
	public double cosinus() {
		double n = this.norme();
		if (n == 0) return 0;
		return coords[0] / n;
	}
	
// tangente de l'angle de i à this
// attention this ne doit pas être vertical...
	public double tangente() {
		if (coords[0] == 0) 
			System.out.println("warning - calcul d'une tangente pour vecteur vertical exception à gérer dans la version suivante...");
		return coords[1] / coords[0];
	}
	
	public double angle() {
		return Math.atan(tangente());
	}

	public void setCoords(double x, double y) {
		this.coords[0] = x;
		this.coords[1] = y;
	}

	public double angle(Vecteur2D s2) {
		//on pourrait utiliser cosinus et sinus mais cela introduirait le bruit des normes...
		return Math.atan(det(s2)/produitScalaire(s2));
	}


}
