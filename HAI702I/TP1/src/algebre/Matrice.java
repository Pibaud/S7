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
				if (Math.abs(mat.m[i][j] - m[i][j]) > tolerance)
					errors++;
			}
		}
		if (errors > 0)
			System.out.println("nb errors = " + errors + " (tolerance = " + tolerance + ")");
		else
			System.out.println("no errors (tolerance = " + tolerance + ")");

		return (errors == 0);
	}

	public boolean isApproximatelyEqualTo(Matrice mat) {
		return isApproximatelyEqualTo(mat, Global.precision);
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
		System.out.println("error = " + error + (error >= tolerance));
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
		for (int i = 0; i < n; i++) {
			if (ma.get(i, i) == 0) {
				return null;
			}
			for (int j = 0; j < n; j++) {
				if (j != i) {
					double Ratio = ma.get(j, i) / ma.get(i, i);
					for (int k = 0; k < n; k++) {
						ma.m[j][k] = ma.get(j, k) - Ratio * ma.get(i, k);
						id.m[j][k] = id.get(j, k) - Ratio * id.get(i, k);
					}
				}
			}
		}
		for (int i = 0; i < n; i++) {
			for (int k = 0; k < n; k++) {
				id.m[i][k] = id.m[i][k] / ma.m[i][i];
			}
			ma.m[i][i] = 1;
		}
		return id;
	}

	public Vecteur mult(Vecteur v) {
		int n = v.getDimension();
		double[] coords = new double[n];
		for (int i = 0; i < n; i++) {
			coords[i] = 0;
			for (int j = 0; j < n; j++) {
				coords[i] += v.get(j) * this.get(j, i);
			}
		}
		return new Vecteur(coords); // todo: return the results of your computation instead
	}

	public Matrice mult(Matrice mat) {
		int n = this.getDimension();
		double[][] res = new double[n][n];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				double sum = 0;
				for (int k = 0; k < n; k++) {
					sum += this.get(i, k) * mat.get(k, j);
				}
				res[i][j] = sum;
			}
		}
		return new Matrice(res);
	}

	public static Matrice covariance(ArrayList<Vecteur> vecteurs) {
		
		int dim = vecteurs.get(0).getDimension();
		int n = vecteurs.size();
		
		double[][] data = new double[n][dim];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < dim; j++) {
				data[i][j] = vecteurs.get(i).coords[j];
			}
		}
		
		for (int j = 0; j < dim; j++) {
			double sum = 0;
			for (int i = 0; i < n; i++) {
				sum += data[i][j];
			}
			double moy = sum / n;
			for (int i = 0; i < n; i++) {
				data[i][j] -= moy;
			}
		}
		
		Matrice mat = new Matrice(data);
		Matrice matT = Matrice.transposee(mat);
		Matrice cov = matT.mult(mat);
		
		for (int i = 0; i < dim; i++) {
			for (int j = 0; j < dim; j++) {
				cov.m[i][j] /= (n - 1);
			}
		}
		return cov;
	}

	public static Matrice transposee(Matrice mat) {
		int dim = mat.getDimension();
		double[][] res = new double[dim][dim];
		for (int i = 0; i < dim; i++) {
			for (int j = 0; j < dim; j++) {
				res[j][i] = mat.get(i, j);
			}
		}
		return new Matrice(res);
	}

	public static Matrice esperance(ArrayList<Matrice> l) {
		int dim = l.get(0).getDimension();
		Matrice sumMat = new Matrice(dim);
		for (int i = 0; i < dim; i++) {
			for (int j = 0; j < dim; j++) {
				sumMat.m[i][j] = 0; // car new Matrice(dim) initialise une matrice identité de taille dim, on veut
									// enlever les 1 en diag
			}
		}
		for (int i = 0; i < l.size(); i++) {
			for (int j = 0; j < dim; j++) {
				for (int k = 0; k < dim; k++) {
					sumMat.m[j][k] += l.get(i).get(j, k);
				}
			}
		}
		for (int i = 0; i < dim; i++) {
			for (int j = 0; j < dim; j++) {
				sumMat.m[i][j] /= l.size();
			}
		}
		return sumMat;
	}
}
