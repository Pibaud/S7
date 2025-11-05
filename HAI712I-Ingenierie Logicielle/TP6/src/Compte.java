public class Compte {
    private Client client;

    public Compte(Client client) {
        this.client = client;
    }

    public Double prixLocation(Produit p){
        return p.getPrix();
    }

    public Client getClient() {
        return client;
    }
}
