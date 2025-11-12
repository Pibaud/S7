public abstract class DecorateurCompte {
    private AbstractCompte decore;

    public DecorateurCompte(AbstractCompte decore){
        this.decore = decore;
    }

    public Double prixLocation(Produit p){
        return decore.prixLocation(p);
    }
}
