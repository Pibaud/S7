public class CompteAvecSeuil extends Compte{
    private int compteur = 0;

    public CompteAvecSeuil(Client client) {
        super(client);
    }

    @Override
    public Double prixLocation(Produit p) {
        this.compteur += 1;
        if(compteur == 2){
            return 0.0;
        }
        return super.prixLocation(p);
    }
}
