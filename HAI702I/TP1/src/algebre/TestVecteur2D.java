package algebre;

public class TestVecteur2D {
    public static void main(String[] args) {
        Vecteur2D v1 = new Vecteur2D(1, 0); // angle 0°
        Vecteur2D v2 = new Vecteur2D(0, 1); // angle 90°
        Vecteur2D v3 = new Vecteur2D(1, 1); // angle 45°
        Vecteur2D v4 = new Vecteur2D(-1, 0); // angle 180°
        Vecteur2D v5 = new Vecteur2D(0, -1); // angle -90°

        System.out.println("vecteurs :");
        System.out.println();
        System.out.println("v1 = " + v1);
        System.out.println("v2 = " + v2);
        System.out.println("v3 = " + v3);
        System.out.println("v4 = " + v4);
        System.out.println("v5 = " + v5);
        System.out.println();

        System.out.println("sinus");
        System.out.println();

        System.out.println("v1.sinus() = " + v1.sinus()); // doit être 0
        System.out.println("v2.sinus() = " + v2.sinus()); // doit être 1
        System.out.println("v3.sinus() = " + v3.sinus()); // doit être ~0.707
        System.out.println("v4.sinus() = " + v4.sinus()); // doit être 0
        System.out.println("v5.sinus() = " + v5.sinus()); // doit être -1
        System.out.println();

        System.out.println("cosinus");
        System.out.println();

        System.out.println("v1.cosinus() = " + v1.cosinus()); // doit être 1
        System.out.println("v2.cosinus() = " + v2.cosinus()); // doit être 0
        System.out.println("v3.cosinus() = " + v3.cosinus()); // doit être ~0.707
        System.out.println("v4.cosinus() = " + v4.cosinus()); // doit être -1
        System.out.println("v5.cosinus() = " + v5.cosinus()); // doit être 0
        System.out.println();
        System.out.println("tangente");
        System.out.println();
        System.out.println("v1.tangente() = " + v1.tangente()); // doit être 0
        System.out.println("v2.tangente() = " + v2.tangente()); // NaN (vertical)
        System.out.println("v3.tangente() = " + v3.tangente()); // doit être ~1
        System.out.println("v4.tangente() = " + v4.tangente()); // doit être 0
        System.out.println("v5.tangente() = " + v5.tangente()); // NaN (vertical)
        System.out.println();
        System.out.println("angle (en radians)");
        System.out.println();
        System.out.println("v1.angle() = " + v1.angle()); // doit être 0
        System.out.println("v2.angle() = " + v2.angle()); // doit être pi/2
        System.out.println("v3.angle() = " + v3.angle()); // doit être ~0.785
        System.out.println("v4.angle() = " + v4.angle()); // doit être pi
        System.out.println("v5.angle() = " + v5.angle()); // doit être -pi/2
        System.out.println();
        System.out.println("Tests avec deux vecteurs");
        System.out.println();
        System.out.println("v1.cosinus(v2) = " + v1.cosinus(v2)); // doit être 0
        System.out.println("v1.sinus(v2) = " + v1.sinus(v2)); // doit être 1
        System.out.println("v1.angle(v2) = " + v1.angle(v2)); // doit être pi/2
        System.out.println("v3.cosinus(v4) = " + v3.cosinus(v4)); // doit être ~-0.707
        System.out.println("v3.sinus(v4) = " + v3.sinus(v4)); // doit être ~0.707
        System.out.println("v3.angle(v4) = " + v3.angle(v4)); // doit être ~2.356
    }
}
