public abstract class AbstractCommande {
    private int volumeDeplace; //servira pour faire les defaire
    private Bidon bidon;

    public AbstractCommande(Bidon bidon) {
        this.bidon = bidon;
    }

    public abstract void faire();
    public abstract void defaire();

    public Bidon getBidon() {
        return bidon;
    }

    public void setVolumeDeplace(int volume) {
        volumeDeplace = volume;
    }

    public int getVolumeDeplace() {
        return volumeDeplace;
    }

    public abstract String toString();
}
