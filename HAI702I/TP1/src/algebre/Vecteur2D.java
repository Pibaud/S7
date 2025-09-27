package algebre;

public class Vecteur2D extends Vecteur {
	public Vecteur2D(double x, double y) {
		super(2);
		coords[0] = x;
		coords[1] = y;
	}
	
// sinus de l'angle de i à this
	public double sinus() {
		Vecteur2D i = new Vecteur2D(1, 0);
		return i.det(this)/(this.norme()*i.norme());
	}
	
// cosinus de l'angle de i à this
	public double cosinus() {
		Vecteur2D i = new Vecteur2D(1, 0);
		return this.scalaire(i)/this.norme()*i.norme();
	}
	
// tangente de l'angle de i à this
// attention this ne doit pas être vertical...
	public double tangente() {
		if(this.coords[0]==0) {
			System.out.println("vecteur vertical");
			return Double.NaN;
		}
		return this.sinus()/this.cosinus();	
	}
	
	//retourne la mesure de l'angle entre le vecteur appelant et le vecteur i, vecteur des abscisses 
	//(c'est à dire, premier vecteur de la base canonique du plan). L'angle est mesuré en radian entre -ℼ/2 et ℼ/2.
	public double angle() {
		double cos = this.cosinus();
		return Math.acos(cos);
	}
	
	private double det(Vecteur2D v) {
		return this.coords[0]*v.coords[1] - this.coords[1]*v.coords[0];
	}
	
	public double cosinus(Vecteur2D v) {
		return this.scalaire(v)/(this.norme()*v.norme());
	}

	public double sinus(Vecteur2D v) {
		return this.det(v)/(this.norme()*v.norme());
	}

	public double tangente(Vecteur2D v) {
		if(v.coords[0]==0) {
			System.out.println("vecteur vertical");
			return Double.NaN;
		}
		return this.sinus(v)/this.cosinus(v);	
	}

	public double angle(Vecteur2D s2) {
		double cos = this.cosinus(s2);
		return Math.acos(cos);
	}

	public double scalaire(Vecteur2D v){
		return this.coords[0]*v.coords[0] + this.coords[1]*v.coords[1];
	}
}
