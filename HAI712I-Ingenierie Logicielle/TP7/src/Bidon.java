public class Bidon {
    private int capacite;
    private int litresContenus;

    public Bidon(int capacite) {
        this.capacite = capacite;
        this.litresContenus = 0;
    }

    public void remplir(int litresAjoutes){
        this.litresContenus += litresAjoutes;
    }

    public void vider(){
        this.litresContenus = 0;
    }

    public void transvaserVers(Bidon autreBidon){
        int volNecessaire = autreBidon.getCapacite() - autreBidon.getLitresContenus();
        if(volNecessaire >= this.litresContenus){
            autreBidon.remplir(this.litresContenus);
            this.litresContenus = 0;
        }
        else{
            autreBidon.remplir(volNecessaire);
            this.litresContenus -= volNecessaire;
        }
    }

    public int getCapacite() {
        return capacite;
    }

    public int getLitresContenus() {
        return litresContenus;
    }
}
