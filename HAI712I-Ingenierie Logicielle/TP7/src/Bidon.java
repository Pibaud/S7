public class Bidon {
    private int capacite;
    private int litresContenus;
    private int index;

    public Bidon(int capacite) {
        this.capacite = capacite;
        this.litresContenus = 0;
    }

    public int remplir(int litresAjoutes){
        this.litresContenus += litresAjoutes;
        return litresAjoutes;
    }

    public int vider(){
        int volumeInitial = this.litresContenus;
        this.litresContenus = 0;
        return volumeInitial;
    }

    public int transvaserVers(Bidon autreBidon){
        int volNecessaire = autreBidon.getCapacite() - autreBidon.getLitresContenus();
        if(volNecessaire >= this.litresContenus){
            autreBidon.remplir(this.litresContenus);
            this.litresContenus = 0;
        }
        else{
            autreBidon.remplir(volNecessaire);
            this.litresContenus -= volNecessaire;
        }
        return volNecessaire;
    }

    public int getCapacite() {
        return capacite;
    }

    public int getLitresContenus() {
        return litresContenus;
    }

    public int getIndex() {return index;}
}
