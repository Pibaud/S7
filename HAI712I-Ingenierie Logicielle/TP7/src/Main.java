public class Main {
    public static void main(String[] args) {
        int[] capacitesBidons = {200, 100, 50};

        Partie p1 = new Partie(3, capacitesBidons, 150);

        p1.jouer();
    }
}