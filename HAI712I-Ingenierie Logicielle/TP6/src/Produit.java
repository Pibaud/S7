public class Produit {
    private String nom;
    private Double prix;

    public Produit(String nom, Double prix) {
        this.nom = nom;
        this.prix = prix;
    }

    public String getNom() {
        return nom;
    }

    public Double getPrix() {
        return prix;
    }
}