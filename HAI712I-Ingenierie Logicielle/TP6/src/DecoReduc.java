public class DecoReduc extends DecorateurCompte{

    public DecoReduc(AbstractCompte compte){
        super(compte);
    }

    public Double prixLocation(Produit p){
        Double reduction = 0.9;
        return super.prixLocation(p)* reduction;
    }
}
