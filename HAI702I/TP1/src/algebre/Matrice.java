package algebre;

import java.util.ArrayList;

//square matrices
public class Matrice {
	private double[][] m;
	private int dimension;

	public Matrice(double[][] matrix) {
		dimension = matrix.length;
		m = new double[dimension][dimension];
		for (int i = 0; i < dimension; i++) {
			for (int j = 0; j < dimension; j++) {
				m[i][j] = matrix[i][j];
			}
		}
	}

	public Matrice(int dimension) {
		this.dimension = dimension;
		m = new double[dimension][dimension];
		for (int i = 0; i < dimension; i++) {
			for (int j = 0; j < dimension; j++) {
				m[i][j] = (i == j) ? 1 : 0;
			}
		}
	}
	
	public boolean isApproximatelyEqualTo(Matrice mat, double tolerance) {
		int errors = 0;
		for (int i = 0; i < dimension; i++) {
			for (int j = 0; j < dimension; j++) {
				if (Math.abs(mat.m[i][j] - m[i][j])> tolerance)
					errors++;
			}
		}
		if (errors > 0) 
			System.out.println("nb errors = "+ errors + " (tolerance = "+tolerance+")");
		else 
			System.out.println("no errors (tolerance = "+tolerance+")");
			
		return (errors == 0);
	}
	
	public boolean isApproximatelyEqualTo(Matrice mat) {
		return isApproximatelyEqualTo( mat, Global.precision);
	}
	
	public boolean isApproximatelyIdentity() {
		return isApproximatelyEqualTo(new Matrice(dimension), Global.precision);
	}
	
	public boolean isIdentity(double tolerance) {
		double error = 0;
		for (int i = 0; i < dimension; i++) {
			for (int j = 0; j < dimension; j++) {
				error += (i == j) ? Math.abs(1 - m[i][j]) : Math.abs(0 - m[i][j]);
			}
		}
		System.out.println("error = "+error+ (error >= tolerance));
		return (error <= tolerance);
	}

	public int getDimension() {
		return dimension;
	}

	private double get(int i, int j) {
		return m[i][j];
	}

	public String toString() {
		String s = "";
		for (int i = 0; i < dimension; i++) {
			for (int j = 0; j < dimension; j++) {
				s += get(i, j) + "\t";
			}
			s += "\n";
		}
		return s;
	}

	public void print() {
		System.out.println("<-----------------");
		System.out.println(toString());
		System.out.println("----------------->");
	}

	private Matrice copy() {
		double[][] m = new double[dimension][dimension];
		for (int i = 0; i < dimension; i++) {
			for (int j = 0; j < dimension; j++) {
				m[i][j] = this.get(i, j);
			}
		}
		return new Matrice(m);
	}

	public Matrice inverse() {
        int n = this.dimension;
        Matrice ma = this.copy();
        Matrice id = new Matrice(n);
        for (int i = 0; i<n; i++){
            if (ma.get(i,i) == 0){
                return null;
            }
            for (int j = 0; j<n; j++) {
                if (j!= i) {
                    double Ratio = ma.get(j, i) / ma.get(i, i);
                    for (int k = 0; k < n; k++) {
                        ma.m[j][k] =  ma.get(j, k) - Ratio * ma.get(i, k);
                        id.m[j][k] =  id.get(j, k) - Ratio * id.get(i, k);
                    }
                }
            }
        }
        for (int i = 0; i<n; i++){
            for (int k = 0; k < n; k++) {
                id.m[i][k]= id.m[i][k] / ma.m[i][i];
            }
            ma.m[i][i] = 1;
        }
        ma.print();
        return id;
    }

	public Vecteur mult(Vecteur v) {
		double [] coords;
		int n = v.getDimension();
		for(int i=0; i<n; i++){
			for(int j=0; i<n; j++){
				coords[j] += v.get(j)*
			}
		}
		return new Vecteur(coords); //todo: return the results of your computation instead
	}
	
	public Matrice mult(Matrice m) {
		return null; //todo: return the results of your computation instead
	}

	public static Matrice covariance(ArrayList<Vecteur> vecteurs) {
		return null; //todo: return the results of your computation instead
	}

	public static Matrice esperance(ArrayList<Matrice> l) {
		return null; //todo: return the results of your computation instead
	}
}
