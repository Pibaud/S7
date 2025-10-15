public abstract class ElementStockage{
    private String nom;
    private Directory dossierParent;
    int basicSize;

    public ElementStockage(String nom, Directory dossierParent) {
        this.nom = nom;
        this.dossierParent = dossierParent;
    }

    public abstract int size();

    public abstract String AbsoluteAdress();
}
