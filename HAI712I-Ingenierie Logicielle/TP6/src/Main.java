public class Main {
    public static void main(String[] args) {
        System.out.println("========================================\nEXERCICE 1\n========================================\n");

        Produit lgv = new Produit("La Grande Vadrouille", 10.0);
        Client cl = new Client("Dupont");
        Compte cmt = new Compte(cl);

        Compte cmt2 = new DecoReduc(cmt);
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
        cmt = new DecoReduc (cmt);

        //Dupont achete un forfait r´eduction.
        cmt = new DecoReduc (cmt);
        System.out.println("r´eduction lgv : " + cmt.prixLocation(lgv));

        //Dupont achete en plus un forfait seuil, le seuil est `a 2
        cmt = new DecoSeuil(cmt);
        System.out.println("Seuil1+Reduction lgv: " + cmt.prixLocation(lgv));
        System.out.println("Seuil2+Reduction lgv: " + cmt.prixLocation(lgv));
        System.out.println("Seuil3+Reduction lgv: " + cmt.prixLocation(lgv)); //rend 0

        //Dupont avec ses 2 forfaits loue un produit sold´e
        System.out.println("Seuil1+Reduction+Solde rocky: " + cmt.prixLocation(r4));
        System.out.println("Seuil2+Reduction+Solde rocky: " + cmt.prixLocation(r4));
        System.out.println("Seuil3+Reduction+Solde rocky: " + cmt.prixLocation(r4));
    }
}