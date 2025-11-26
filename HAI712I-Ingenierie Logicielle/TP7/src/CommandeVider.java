public class CommandeVider extends AbstractCommande {

    public CommandeVider(Bidon bidon) {
        super(bidon);
    }

    @Override
    public void faire() {
        setVolumeDeplace(getBidon().vider());
    }

    @Override
    public void defaire() {
    }

    @Override
    public String toString() {
        return "Vider-Bidon-"+ getBidon().getIndex() +"-avec-" + getVolumeDeplace() + "-litres";
    }
}
