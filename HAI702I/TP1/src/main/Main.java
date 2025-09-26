package main;
import java.util.ArrayList;
import algebre.Matrice;

public class Main {

	public static void main(String s[]) {
		//todo tests 
		
		// starting with example from TD
		ArrayList<Matrice> tests = new ArrayList<>();
		tests.add(new Matrice(new double[][]  {{0, 1, 2}, {1, 2, 1}, {2, 1, 1}}));
		tests.add(new Matrice(new double[][]  {{0, 1, 2}, {0, 0, 0}, {1, 2, 3}}));
		tests.add(new Matrice(new double[][]  {{2, 0, 0}, {0, 3, 0 }, { 0, 0, 4 }}));
		tests.add(new Matrice(new double[][]  {{1, 0, 0}, {0, 0, 0 }, { 0, 0, 5 } }));
		tests.add(new Matrice(new double[][]  {{2, 1, 3}, {0, 1, 4}, {1, 0, 1}}));
		tests.add(new Matrice(new double[][]  {{1, 2, 3}, {4, 5, 6},  {7, 8, 9}}));
		tests.add(new Matrice(new double[][]  {{4, 0, 8}, {0.8,0,898},{65,0,9}}));
		tests.add(new Matrice(new double[][]  {{6, 16,0.5},{10,0,9.6},{1,89,8}}));
		tests.add(new Matrice(new double[][]  {{1, 2, 3}, {0, 1, 4 }, { 5, 6, 0 } }));
		tests.add(new Matrice(new double[][]  {{2, 4, 1}, {1, 2, 1 }, { 0, 0, 0 } }));
		tests.add(new Matrice(new double[][]  {{0, 1, 2}, {0,3,4},{0,5,6}}));
		tests.add(new Matrice(new double[][]  {{4, 5, 6}, {1,2,3},{7,8,9}}));
		tests.add(new Matrice(new double[][]  {{1, 2, 3}, {5,7,11},{13,17,19}}));
		tests.add(new Matrice(new double[][]  {{1, 1, 2}, {3,5,8},{13,21,34}}));
		tests.add(new Matrice(new double[][]  {{1, 1, 1}, {1, 0, 1 }, { 1, 1, 1 } }));
		tests.add(new Matrice(new double[][]  {{1, 1, 1}, {1, 0, 1 }, { 1, 1, 2 } }));
		tests.add(new Matrice(new double[][]  {{3, 6, 9}, {4, 2, 0 }, { 8, 8, 8 } }));
		tests.add(new Matrice(new double[][]  {{3, 6, 9}, {4, 2, 0 }, { 7, 8, 8 } }));
		tests.add(new Matrice(new double[][]  {{5, 6, 7}, {1,1,3},{1,10,3}}));
		tests.add(new Matrice(new double[][]  {{0, 1, 0}, {1,1,0},{0,0,7}}));
		tests.add(new Matrice(new double[][]  {{1, 1, 2}, { 1, 2, 1 }, { 2, 1, 1 } }));
		tests.add(new Matrice(new double[][]  {{1, 2, 3}, {4,5,6 }, { 7,8,9 } }));
		tests.add(new Matrice(new double[][]  {{0, 1, 0}, {1,0,1},{0,1,1} }));
		tests.add(new Matrice(new double[][]  {{0, 1, 2}, {0,0,0},{1,2,3}}));
		tests.add(new Matrice(new double[][]  {{3, 5, 7}, { 2, 4, 3 }, { 2, 3, 0 }}));
		tests.add(new Matrice(new double[][]  {{1, 1, 1}, { 0, 0, 0 }, { 1, 1, 1 }}));
		tests.add(new Matrice(new double[][]  {{0, 2, 1}, {3, 5, 2},{1, 4, 6}}));
		tests.add(new Matrice(new double[][]  {{1, 0, 2}, {0, 0, 3},{4, 1, 5}}));
		tests.add(new Matrice(new double[][]  {{6, 6, 7 }, { 6, 6, 7 }, { 6, 6, 7 }}));
		tests.add(new Matrice(new double[][]  {{1, 1, 2 }, { 1, 1, 4 }, { 1, 1, 8 }}));
		tests.add(new Matrice(new double[][]  {{3, 4, 1 }, { 7, 0, 4 }, { 2, 2, 2 } }));
		tests.add(new Matrice(new double[][]  {{0, 2, 1 }, {1 , 3 , 4 }, { 2, 0, 1 } }));	
		//todo
		for (Matrice mat : tests) {
			mat.print();
			System.out.println();
		}
	}

}