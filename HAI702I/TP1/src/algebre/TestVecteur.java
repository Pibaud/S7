package algebre;

public class TestVecteur {
    public static void main(String[] args) {
        Vecteur v1 = new Vecteur(new double[]{1, 2, 3});
        Vecteur v2 = new Vecteur(new double[]{4, 5, 6});
        Vecteur v3 = new Vecteur(new double[]{-1, 0, 1});

        System.out.println("Vecteurs :");
        System.out.println("v1 = " + v1);
        System.out.println("v2 = " + v2);
        System.out.println("v3 = " + v3);
        System.out.println();

        System.out.println("Produit scalaire :");
        System.out.println("v1 . v2 = " + v1.produitScalaire(v2)); // doit être 1*4 + 2*5 + 3*6 = 32
        System.out.println("v1 . v3 = " + v1.produitScalaire(v3)); // doit être 1*(-1) + 2*0 + 3*1 = 2
        System.out.println("v2 . v3 = " + v2.produitScalaire(v3)); // doit être 4*(-1) + 5*0 + 6*1 = 2
        System.out.println();

        System.out.println("Norme carrée :");
        System.out.println("v1.normeCarre() = " + v1.normeCarre()); // doit être 1^2 + 2^2 + 3^2 = 14
        System.out.println("v2.normeCarre() = " + v2.normeCarre()); // doit être 4^2 + 5^2 + 6^2 = 77
        System.out.println("v3.normeCarre() = " + v3.normeCarre()); // doit être (-1)^2 + 0^2 + 1^2 = 2
        System.out.println();

        System.out.println("Norme :");
        System.out.println("v1.norme() = " + v1.norme()); // doit être sqrt(14)
        System.out.println("v2.norme() = " + v2.norme()); // doit être sqrt(77)
        System.out.println("v3.norme() = " + v3.norme()); // doit être sqrt(2)
    }
}
