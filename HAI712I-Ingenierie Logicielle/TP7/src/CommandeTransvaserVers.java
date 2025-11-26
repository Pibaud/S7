public class CommandeTransvaserVers extends AbstractCommande {
    private Bidon autreBidon;

    public CommandeTransvaserVers(Bidon bidon) {
        super(bidon);
        this.autreBidon = bidon;
    }

    @Override
    public void faire() {
        setVolumeDeplace(getBidon().transvaserVers(autreBidon));
    }

    @Override
    public void defaire() {
    }

    @Override
    public String toString() {
        return "Transvaser-Bidon-"+ getBidon().getIndex() +"-vers-Bidon-"+ autreBidon.getIndex() +"-avec-" + getVolumeDeplace() + "-litres";
    }
}
