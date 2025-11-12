public class Main {
    public static void main(String[] args) {
        int[] capacitesBidons = {200, 100, 50};

        Bidon b1 = new Bidon(200);

        AbstractCommande remplirb1 = new Remplir(b1);
    }
}