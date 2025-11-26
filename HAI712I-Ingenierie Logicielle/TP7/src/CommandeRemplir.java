public class CommandeRemplir extends AbstractCommande {
    private int litresAjoutes;

    public CommandeRemplir(Bidon bidon, int litresAjoutes) {
        super(bidon);
        this.litresAjoutes = litresAjoutes;
    }

    @Override
    public void faire() {
        setVolumeDeplace(getBidon().remplir(litresAjoutes));
    }

    @Override
    public void defaire() {

    }

    @Override
    public String toString() {
        return "Remplir-Bidon-"+ getBidon().getIndex() +"-avec-" + getVolumeDeplace() + "-litres";
    }
}
