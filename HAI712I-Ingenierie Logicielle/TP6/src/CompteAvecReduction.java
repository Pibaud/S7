public class CompteAvecReduction extends Compte {
    private Double reduction = 0.9;

    public CompteAvecReduction(Client client) {
        super(client);
    }

    @Override
    public Double prixLocation(Produit p) {
        return reduction*super.prixLocation(p);
    }
}
