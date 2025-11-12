public class DecoSeuil extends DecorateurCompte{
    private Double seuil = 2.0;

    public DecoSeuil(AbstractCompte decore){
        super(decore);
    }

    public Double prixLocation(Produit p){
        this.seuil -= 1;
        if(this.seuil == 0){
            this.seuil = 2.0;
            return 0.0;
        }
        return super.prixLocation(p);
    }
}
