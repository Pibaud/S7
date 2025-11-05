public class Main {
    public static void main(String[] args) {
        System.out.println("========================================\nEXERCICE 1\n========================================\n");

        Produit lgv = new Produit("La Grande Vadrouille", 10.0);
        Client cl = new Client("Dupont");
        Compte cmt = new Compte(cl);

        Compte cmt2 = new CompteAvecReduction(cl);
        System.out.println("CompteReduction : " + cmt2.prixLocation(lgv));

        Compte cmt3 = new CompteAvecSeuil(cl);
        System.out.println("CompteSeuil : " + cmt3.prixLocation(lgv));
        System.out.println("CompteSeuil : " + cmt3.prixLocation(lgv));
        System.out.println("CompteSeuil : " + cmt3.prixLocation(lgv));

        Produit r4 = new ProduitSolde("RockyIV", 10.0);
        System.out.println("CompteNormal+ProduitSoldé : " + cmt.prixLocation(r4));
        System.out.println("CompteReduction+ProduitSoldé : " + cmt2.prixLocation(r4));

        System.out.println("========================================\nEXERCICE 2\n========================================\n");

        System.out.println("basique lgv : " + cmt.prixLocation(lgv));

        //Dupont achete un forfait r´eduction
        cmt = new ForfaitReduction (cmt);
    }
}