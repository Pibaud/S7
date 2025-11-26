import java.util.ArrayList;

public class Partie{
    private int nbBidons;
    int[] capacitesBidons;
    int volumeCible;
    ArrayList<Bidon> bidons = new ArrayList<Bidon>();
    ArrayList<AbstractCommande> historique = new  ArrayList<AbstractCommande>();

    public Partie(int nbBidons,  int[] capacitesBidons,  int volumeCible) {
        this.nbBidons = nbBidons;
        this.capacitesBidons = capacitesBidons;
        this.volumeCible = volumeCible;
        for (int i = 0; i < nbBidons; i++) {
            this.bidons.add(new Bidon(capacitesBidons[i]));
        }
    }

    public void jouer(){
        Bidon b1 = new Bidon(200);
        AbstractCommande remplirb1 = new CommandeRemplir(b1, 100);
        remplirb1.faire();
        historique.add(remplirb1);
        remplirb1.defaire();
        historique.removeLast();
    }
}
