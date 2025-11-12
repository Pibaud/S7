import java.util.ArrayList;

public class Partie{
    private int nbBidons;
    int[] capacitesBidons;
    int volumeCible;
    ArrayList<Bidon> bidons = new ArrayList<Bidon>();
    ArrayList<AbstractCommande> historique;

    public Partie(int nbBidons,  int[] capacitesBidons,  int volumeCible) {
        this.nbBidons = nbBidons;
        this.capacitesBidons = capacitesBidons;
        this.volumeCible = volumeCible;
        for (int i = 0; i < nbBidons; i++) {
            this.bidons.add(new Bidon(capacitesBidons[i]));
        }
    }

    public int getNbBidons() {
        return nbBidons;
    }

    public int[] getCapacitesBidons() {
        return capacitesBidons;
    }
}
