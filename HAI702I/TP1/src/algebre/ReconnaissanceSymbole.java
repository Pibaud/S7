package algebre;

import java.util.ArrayList;

/**
 * CORRECTION DE L'EXAMEN - Reconnaissance de Symboles
 * (NE PAS REGARDER AVANT D'AVOIR FAIT L'EXAMEN !)
 */
public class ReconnaissanceSymbole {
    
    // ========== PARTIE 1 : RECONNAISSANCE SIMPLE ==========
    
    /**
     * Q1.1 : Trouve le symbole le plus proche du tracé donné
     */
    public static String symbolePlusProche(Matrice trace, 
                                          ArrayList<Matrice> references,
                                          ArrayList<String> nomsSymboles) {
        double distanceMin = Double.MAX_VALUE;
        String meilleurSymbole = null;
        
        for (int i = 0; i < references.size(); i++) {
            double distance = trace.distanceFrobenius(references.get(i));
            if (distance < distanceMin) {
                distanceMin = distance;
                meilleurSymbole = nomsSymboles.get(i);
            }
        }
        
        return meilleurSymbole;
    }
    
    // ========== PARTIE 2 : ANALYSE DES DISTANCES ==========
    
    /**
     * Q2.1 : Affiche toutes les distances triées par ordre croissant
     */
    public static void afficherDistances(Matrice trace,
                                        ArrayList<Matrice> references,
                                        ArrayList<String> nomsSymboles) {
        // Calculer toutes les distances
        ArrayList<Double> distances = new ArrayList<>();
        ArrayList<String> noms = new ArrayList<>();
        
        for (int i = 0; i < references.size(); i++) {
            double dist = trace.distanceFrobenius(references.get(i));
            distances.add(dist);
            noms.add(nomsSymboles.get(i));
        }
        
        // Tri par sélection simple (tri par ordre croissant)
        for (int i = 0; i < distances.size() - 1; i++) {
            int indexMin = i;
            for (int j = i + 1; j < distances.size(); j++) {
                if (distances.get(j) < distances.get(indexMin)) {
                    indexMin = j;
                }
            }
            // Échanger
            if (indexMin != i) {
                Double tempDist = distances.get(i);
                distances.set(i, distances.get(indexMin));
                distances.set(indexMin, tempDist);
                
                String tempNom = noms.get(i);
                noms.set(i, noms.get(indexMin));
                noms.set(indexMin, tempNom);
            }
        }
        
        // Affichage
        for (int i = 0; i < noms.size(); i++) {
            System.out.printf("  %s : %.3f\n", noms.get(i), distances.get(i));
        }
    }
    
    // ========== PARTIE 3 : AMÉLIORATION AVEC MOYENNAGE ==========
    
    /**
     * Q3.1 & Q3.2 : Calcule et compare avec un symbole moyen
     */
    public static void comparerAvecMoyenne() {
        System.out.println("\n========== PARTIE 3 : MOYENNAGE ==========");
        
        // Calculer PLUS moyen
        ArrayList<Matrice> exemplesPLUS = new ArrayList<>();
        exemplesPLUS.add(DonneesExamen.getSymbolePLUS());
        exemplesPLUS.add(DonneesExamen.getSymbolePLUS_v2());
        
        Matrice plusMoyen = Matrice.esperance(exemplesPLUS);
        
        System.out.println("Matrice PLUS moyen calculée :");
        plusMoyen.print();
        
        // Comparer les distances pour Tracé 1
        Matrice trace1 = DonneesExamen.getTrace1();
        double distPLUS = trace1.distanceFrobenius(DonneesExamen.getSymbolePLUS());
        double distPLUSmoyen = trace1.distanceFrobenius(plusMoyen);
        
        System.out.println("\nComparaison pour Tracé 1 :");
        System.out.printf("  Distance avec PLUS original : %.3f\n", distPLUS);
        System.out.printf("  Distance avec PLUS moyen    : %.3f\n", distPLUSmoyen);
        
        if (distPLUSmoyen < distPLUS) {
            System.out.println("➔ La moyenne améliore la reconnaissance !");
        } else {
            System.out.println("➔ La moyenne n'améliore pas ici.");
        }
    }
    
    // ========== PARTIE 4 : NORMALISATION (BONUS) ==========
    
    /**
     * Q4.1 : Compare avec et sans normalisation
     */
    public static void comparerAvecNormalisation() {
        System.out.println("\n========== PARTIE 4 : NORMALISATION (BONUS) ==========");
        
        Matrice trace1 = DonneesExamen.getTrace1();
        Matrice plus = DonneesExamen.getSymbolePLUS();
        
        // Sans normalisation
        double distSansNorm = trace1.distanceFrobenius(plus);
        
        // Avec normalisation
        Matrice trace1Norm = trace1.normaliser();
        Matrice plusNorm = plus.normaliser();
        double distAvecNorm = trace1Norm.distanceFrobenius(plusNorm);
        
        System.out.printf("Distance sans normalisation : %.3f\n", distSansNorm);
        System.out.printf("Distance avec normalisation : %.3f\n", distAvecNorm);
    }
    
    // ========== MAIN PRINCIPAL ==========
    
    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════════╗");
        System.out.println("║   RECONNAISSANCE DE SYMBOLES MATHÉMATIQUES   ║");
        System.out.println("╚══════════════════════════════════════════════╝\n");
        
        // Chargement des données
        ArrayList<Matrice> references = DonneesExamen.getSymbolesReference();
        ArrayList<String> noms = DonneesExamen.getNomsSymboles();
        ArrayList<Matrice> traces = DonneesExamen.getTracesAReconnaitre();
        
        // ========== PARTIE 1 : Q1.2 ==========
        System.out.println("========== PARTIE 1 : RECONNAISSANCE ==========");
        for (int i = 0; i < traces.size(); i++) {
            String resultat = symbolePlusProche(traces.get(i), references, noms);
            System.out.println("Tracé " + (i+1) + " reconnu comme : " + resultat);
        }
        
        // ========== PARTIE 2 : Q2.2 ==========
        System.out.println("\n========== PARTIE 2 : ANALYSE DES DISTANCES ==========");
        for (int i = 0; i < traces.size(); i++) {
            System.out.println("\nDistances pour Tracé " + (i+1) + " :");
            afficherDistances(traces.get(i), references, noms);
        }
        
        // ========== PARTIE 3 ==========
        comparerAvecMoyenne();
        
        // ========== PARTIE 4 (BONUS) ==========
        comparerAvecNormalisation();
        
        System.out.println("\n╔═══════════════════════════════════════════════════════╗");
        System.out.println("║                  FIN DU PROGRAMME                     ║");
        System.out.println("╚═══════════════════════════════════════════════════════╝");
    }
}
