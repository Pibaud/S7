package algebre;

import java.util.ArrayList;

//light api for square matrices
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
// initialisation à l'identité
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
		return (errors == 0);
	}
	
	public boolean isApproximatelyEqualTo(Matrice mat) {
		return isApproximatelyEqualTo( mat, Global.precision);
	}
	
	public boolean isApproximatelyIdentity() {
		return isApproximatelyEqualTo(new Matrice(dimension), Global.precision);
	}

	public int getDimension() {
		return dimension;
	}

	private double get(int i, int j) {
		return m[i][j];
	}

	public static Matrice covariance(ArrayList<Vecteur> vecteurs) {
		int nbv = vecteurs.size();
		if (vecteurs.size() == 0)
			return null;
		int dimension = vecteurs.get(0).getDimension();
		double[][] covarianceValues = new double[dimension][dimension];
		Vecteur avg = Vecteur.esperance(vecteurs);
		for (Vecteur v : vecteurs) {
			for (int i = 0; i < dimension; i++) {
				for (int j = 0; j < dimension; j++) {
					covarianceValues[i][j] += (v.get(i) - avg.get(i)) * (v.get(j) - avg.get(j));
				}
			}
		}

		// Divide by (numVectors - 1) for sample covariance
		for (int i = 0; i < dimension; i++) {
			for (int j = 0; j < dimension; j++) {
				covarianceValues[i][j] /= (nbv - 1);
			}
		}
		return new Matrice(covarianceValues);
	}

	// returns the average matrix of a list of matrices
	// assume all the matrices in the list of matrices given as a parameter have the
	// same dimension
	public static Matrice esperance(ArrayList<Matrice> l) {
		int d = l.size();
		if (d == 0)
			return null;
		int dimension = l.get(0).getDimension();
		double mij[][] = new double[dimension][dimension];
		double e;
		for (int i = 0; i < dimension; i++) {
			for (int j = 0; j < dimension; j++) {
				e = 0;
				for (Matrice m : l) {
					e += m.get(i, j);
				}
				mij[i][j] = e / d;
			}
		}
		return new Matrice(mij);
	}

	// invert the matrix when possible by using a Gauss technique
	public Matrice inverse() {
		double[][] result = new Matrice(dimension).m;
		double[][] tmp = this.copy().m;
		double pivot, c;


		for (int p = 0; p < dimension; p++) {			
			pivot = tmp[p][p];
			if (pivot != 0) {
				// normalize if necessary
				if (pivot != 1) {
					for (int l = 0; l < dimension; l++) {
						tmp[p][l] /= pivot;
						result[p][l] /= pivot;
					}
				}

				// triangulate for the current pivot				
				for (int l = 0; l < dimension; l++) {
					if (l != p) {
						c = tmp[l][p];
						if (c != 0)
							for (int j = 0; j < dimension; j++) {
								tmp[l][j] -= c * tmp[p][j];
								result[l][j] -= c * result[p][j];
							}
					}
				}
			} else {
				System.out.println("warning: matrix cannot be inverted");
				return null;
			}
		}
		return new Matrice(result);
	}
	
	// invert the matrix when possible by using a Gauss technique
	public Matrice inverseOptimized() {
		double[][] result = new Matrice(dimension).m;
		double[][] tmp = this.copy().m;
		double pivot, c;
		int idxPivot;

		for (int a = 0; a < dimension; a++) {
			// find new pivot (on prend le max des valeurs absolues des coefficients de la
			// colonne)
			idxPivot = a;
			for (int l = 0; l < dimension; l++) {
				if (Math.abs(tmp[l][a]) > Math.abs(tmp[idxPivot][a]))
					idxPivot = l;
			}
			pivot = tmp[idxPivot][a];
			if (pivot != 0) {
				// invert lines if necessary
				double tmpln1[], tmpln2[];

				if (idxPivot != a) {
					tmpln1 = tmp[a];
					tmp[a] = tmp[idxPivot];
					tmp[idxPivot] = tmpln1;

					tmpln2 = result[a];
					result[a] = result[idxPivot];
					result[idxPivot] = tmpln2;
				}
				// System.out.println("pivot = "+pivot);

				// normalize if necessary
				if (pivot != 1) {
					for (int l = 0; l < dimension; l++) {
						tmp[a][l] /= pivot;
						result[a][l] /= pivot;
					}
				}

				// triangulate for the current pivot
				for (int i = 0; i < dimension; i++) {
					if (i != a) {
						// System.out.println("Etape " + i);
						c = tmp[i][a];
						for (int j = 0; j < dimension; j++) {
							tmp[i][j] -= c * tmp[a][j];
							result[i][j] -= c * result[a][j];
						}
					}
				}
			} else {
				// handle exception?
				return null;
			}
		}
		return new Matrice(result);
	}

	public Vecteur mult(Vecteur v) {
		int dimension = v.getDimension();
		double[] coords = new double[dimension];
		for (int i = 0; i < dimension; i++) {
			coords[i] = 0;
			for (int j = 0; j < dimension; j++) {
				coords[i] += get(i, j) * v.get(j);
			}
		}
		return new Vecteur(coords);
	}
	
	public Matrice mult(Matrice m) {
		int dimension = m.getDimension();
		double[][] coef = new double[dimension][dimension];

		//initialisation
		for (int i = 0; i < dimension; i++) {
			for (int j = 0; j < dimension; j++) {				
					coef[i][j] = 0;
			}
		}
		
		//calcul
		for (int i = 0; i < dimension; i++) {
			for (int j = 0; j < dimension; j++) {
				for (int k = 0; k < dimension; k++)
					coef[i][j] += get(i, k) * m.get(k,j);
			}
		}
		return new Matrice(coef);
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

	public void printArray(double[][] tab) {
		(new Matrice(tab)).print();
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

	public static void main(String[] args) {
		double coef[][] = { { 1, 1, 2 }, { 1, 2, 1 }, { 2, 1, 1 } };
		Matrice m = new Matrice(coef);
		m.print();

		Matrice newM = m.inverse();
		System.out.println("Resultat ");
		newM.print();
		
		Matrice id = newM.mult(m);
		id.print();
		
		System.out.println("Résultat test inverse "+ id.isApproximatelyIdentity());
	}

	public double[] getLine(int i) {
		double[] line = new double[dimension];
		
		for (int j= 0; j < dimension; j++)
			line[i] = get(i,j);
		return line;
	}

	public static Matrice eotccm(ArrayList<Matrice> l, int df) {
		if (df == 0)
			return null;
		int dimension = l.get(0).getDimension();
		double mij[][] = new double[dimension][dimension];
		double e;
		for (int i = 0; i < dimension; i++) {
			for (int j = 0; j < dimension; j++) {
				e = 0;
				for (Matrice m : l) {
					e += m.get(i, j);
				}
				mij[i][j] = e / df;
			}
		}
		return new Matrice(mij);
	}
}
