package algebre;

import java.util.ArrayList;

public class TestMatrice {
    public static void main(String[] args) {
        // Test inverse()
        double[][] data = {
                { 2, 0, 1 },
                { 1, 1, 0 },
                { 0, 1, 1 }
        };
        Matrice mat = new Matrice(data);
        System.out.println("Matrice originale:");
        mat.print();
        Matrice inv = mat.inverse();
        System.out.println("Inverse:");
        if (inv != null)
            inv.print();
        else
            System.out.println("Inverse non définie");

        // Test mult(Matrice)
        double[][] data2 = {
                { 1, 2, 3 },
                { 0, 1, 4 },
                { 5, 6, 0 }
        };
        System.out.println("Deuxième matrice:");
        Matrice mat2 = new Matrice(data2);
        mat2.print();
        System.out.println("Produit mat1 * mat2:");
        Matrice prod = mat.mult(mat2);
        prod.print();

        // Test mult(Vecteur)
        double[] vectData = { 1, 2, 3 };
        Vecteur v = new Vecteur(vectData);
        System.out.println("Vecteur:");
        System.out.println(v);
        System.out.println("mat1:");
        System.out.println(mat);
        System.out.println("Produit vecteur * mat1:");
        Vecteur vprod = mat.mult(v);
        System.out.println(vprod);

        // Test covariance(ArrayList<Vecteur>)
        ArrayList<Vecteur> vecteurs = new ArrayList<>();
        vecteurs.add(new Vecteur(new double[] {1, 5, 2}));
        vecteurs.add(new Vecteur(new double[] {2, 3, 3}));
        vecteurs.add(new Vecteur(new double[] {3, 1, 4}));
        System.out.println("Matrice de covariance:");
        Matrice cov = Matrice.covariance(vecteurs);
        System.out.println("Covariance:");
        System.out.println(cov);

        //test espérance
        ArrayList<Matrice> matrices = new ArrayList<>();
        matrices.add(new Matrice(new double[][] {
                { 1, 2, 3 },
                { 4, 5, 6 },
                { 7, 8, 9 }
        }));
        matrices.add(new Matrice(new double[][] {
                { 9, 8, 7 },
                { 6, 5, 4 },
                { 3, 2, 1 }
        }));
        matrices.add(new Matrice(new double[][] {
                { 2, 4, 6 },
                { 8, 10, 12 },
                { 14, 16, 18 }
        }));
        Matrice esperance = Matrice.esperance(matrices);
        System.out.println("Esperance");
        esperance.print();
    }
}
