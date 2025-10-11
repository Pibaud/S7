package algebre;

import java.util.*;

/**
 * Programme de test complet du classifieur k-NN
 * Démontre l'utilisation d'un vrai système de classification
 */
public class TestClassifieur {
    
    public static void main(String[] args) {
        System.out.println("╔═══════════════════════════════════════════════════╗");
        System.out.println("║    TEST DU CLASSIFIEUR K-NN POUR SYMBOLES        ║");
        System.out.println("╚═══════════════════════════════════════════════════╝\n");
        
        // ========== 1. CRÉATION DES DONNÉES ==========
        System.out.println("========== 1. CRÉATION DES DONNÉES ==========");
        ArrayList<Exemple> donneesEntrainement = creerDonneesEntrainement();
        ArrayList<Exemple> donneesTest = creerDonneesTest();
        
        System.out.println("✓ " + donneesEntrainement.size() + " exemples d'entraînement");
        System.out.println("✓ " + donneesTest.size() + " exemples de test\n");
        
        // ========== 2. TEST AVEC k=1 SANS NORMALISATION ==========
        System.out.println("========== 2. CLASSIFIEUR k=1 (SANS NORMALISATION) ==========");
        ClassifieurKNN knn1 = new ClassifieurKNN(1, false);
        knn1.entrainer(donneesEntrainement);
        
        // Évaluation
        double tauxReussite1 = knn1.evaluer(donneesTest);
        System.out.printf("Taux de réussite : %.1f%%\n\n", tauxReussite1 * 100);
        
        // Prédictions détaillées
        System.out.println("Prédictions détaillées :");
        for (int i = 0; i < donneesTest.size(); i++) {
            Exemple ex = donneesTest.get(i);
            String prediction = knn1.predire(ex.getTrace());
            String statut = prediction.equals(ex.getLabel()) ? "✓" : "✗";
            System.out.printf("  %s Test %d : prédit=%s, vrai=%s\n", 
                statut, i+1, prediction, ex.getLabel());
        }
        System.out.println();
        
        // ========== 3. TEST AVEC k=1 AVEC NORMALISATION ==========
        System.out.println("========== 3. CLASSIFIEUR k=1 (AVEC NORMALISATION) ==========");
        ClassifieurKNN knn1Norm = new ClassifieurKNN(1, true);
        knn1Norm.entrainer(donneesEntrainement);
        
        double tauxReussite1Norm = knn1Norm.evaluer(donneesTest);
        System.out.printf("Taux de réussite : %.1f%%\n", tauxReussite1Norm * 100);
        
        if (tauxReussite1Norm > tauxReussite1) {
            System.out.println("➔ La normalisation améliore les performances !");
        } else if (tauxReussite1Norm < tauxReussite1) {
            System.out.println("➔ La normalisation dégrade les performances.");
        } else {
            System.out.println("➔ La normalisation n'a pas d'impact ici.");
        }
        System.out.println();
        
        // ========== 4. TEST AVEC k=3 ==========
        System.out.println("========== 4. CLASSIFIEUR k=3 (VOTE MAJORITAIRE) ==========");
        ClassifieurKNN knn3 = new ClassifieurKNN(3, false);
        knn3.entrainer(donneesEntrainement);
        
        double tauxReussite3 = knn3.evaluer(donneesTest);
        System.out.printf("Taux de réussite : %.1f%%\n\n", tauxReussite3 * 100);
        
        // ========== 5. PROBABILITÉS ==========
        System.out.println("========== 5. DISTRIBUTION DE PROBABILITÉS ==========");
        Matrice trace1 = donneesTest.get(0).getTrace();
        Map<String, Double> probs = knn3.predireProbabilites(trace1);
        
        System.out.println("Pour le tracé de test 1 :");
        
        // Trier par probabilité décroissante
        List<Map.Entry<String, Double>> sortedProbs = new ArrayList<>(probs.entrySet());
        sortedProbs.sort((a, b) -> Double.compare(b.getValue(), a.getValue()));
        
        for (Map.Entry<String, Double> entry : sortedProbs) {
            System.out.printf("  %s : %.1f%%\n", entry.getKey(), entry.getValue() * 100);
        }
        System.out.println();
        
        // ========== 6. COMPARAISON DES PARAMÈTRES ==========
        System.out.println("========== 6. COMPARAISON DES PARAMÈTRES ==========");
        System.out.println("┌──────────────────────┬──────────────┐");
        System.out.println("│ Configuration        │ Taux réussite│");
        System.out.println("├──────────────────────┼──────────────┤");
        System.out.printf("│ k=1, sans norm       │    %.1f%%     │\n", tauxReussite1 * 100);
        System.out.printf("│ k=1, avec norm       │    %.1f%%     │\n", tauxReussite1Norm * 100);
        System.out.printf("│ k=3, sans norm       │    %.1f%%     │\n", tauxReussite3 * 100);
        System.out.println("└──────────────────────┴──────────────┘\n");
        
        // ========== 7. MATRICE DE CONFUSION ==========
        System.out.println("========== 7. MATRICE DE CONFUSION (k=1) ==========");
        afficherMatriceConfusion(knn1, donneesTest);
        
        System.out.println("\n╔═══════════════════════════════════════════════════╗");
        System.out.println("║                 FIN DES TESTS                     ║");
        System.out.println("╚═══════════════════════════════════════════════════╝");
    }
    
    /**
     * Crée un ensemble d'entraînement avec plusieurs exemples par symbole
     */
    private static ArrayList<Exemple> creerDonneesEntrainement() {
        ArrayList<Exemple> donnees = new ArrayList<>();
        
        // Symbole PLUS (version parfaite)
        donnees.add(new Exemple(DonneesExamen.getSymbolePLUS(), "PLUS"));
        
        // Symbole PLUS (version bruitée)
        donnees.add(new Exemple(DonneesExamen.getSymbolePLUS_v2(), "PLUS"));
        
        // Symbole CROIX
        donnees.add(new Exemple(DonneesExamen.getSymboleCROIX(), "CROIX"));
        
        // Symbole CARRE
        donnees.add(new Exemple(DonneesExamen.getSymboleCARRE(), "CARRE"));
        
        return donnees;
    }
    
    /**
     * Crée un ensemble de test
     */
    private static ArrayList<Exemple> creerDonneesTest() {
        ArrayList<Exemple> donnees = new ArrayList<>();
        
        // Les tracés à reconnaître avec leurs vrais labels
        donnees.add(new Exemple(DonneesExamen.getTrace1(), "PLUS"));
        donnees.add(new Exemple(DonneesExamen.getTrace2(), "CROIX"));
        donnees.add(new Exemple(DonneesExamen.getTrace3(), "CARRE"));
        
        return donnees;
    }
    
    /**
     * Affiche la matrice de confusion
     */
    private static void afficherMatriceConfusion(ClassifieurKNN classifieur, 
                                                 ArrayList<Exemple> donneesTest) {
        // Récupérer tous les labels uniques
        Set<String> labels = new TreeSet<>();
        for (Exemple ex : donneesTest) {
            labels.add(ex.getLabel());
        }
        
        // Initialiser la matrice
        Map<String, Map<String, Integer>> matrice = new HashMap<>();
        for (String vrai : labels) {
            matrice.put(vrai, new HashMap<>());
            for (String pred : labels) {
                matrice.get(vrai).put(pred, 0);
            }
        }
        
        // Remplir la matrice
        for (Exemple ex : donneesTest) {
            String vrai = ex.getLabel();
            String pred = classifieur.predire(ex.getTrace());
            matrice.get(vrai).put(pred, matrice.get(vrai).get(pred) + 1);
        }
        
        // Affichage
        System.out.println("           Prédit ↓");
        System.out.print("Vrai →   ");
        for (String label : labels) {
            System.out.printf("%-8s", label);
        }
        System.out.println();
        
        for (String vrai : labels) {
            System.out.printf("%-8s ", vrai);
            for (String pred : labels) {
                System.out.printf("%-8d", matrice.get(vrai).get(pred));
            }
            System.out.println();
        }
    }
}
