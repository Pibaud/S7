package algebre;

import java.util.ArrayList;

/**
 * Données pour l'examen de reconnaissance de symboles
 * Contient les matrices de référence et les tracés à reconnaître
 */
public class DonneesExamen {
    
    // ========== SYMBOLES DE RÉFÉRENCE ==========
    
    /**
     * Symbole PLUS (+)
     */
    public static Matrice getSymbolePLUS() {
        double[][] data = {
            {0.0, 0.0, 1.0, 0.0, 0.0},
            {0.0, 0.0, 1.0, 0.0, 0.0},
            {1.0, 1.0, 1.0, 1.0, 1.0},
            {0.0, 0.0, 1.0, 0.0, 0.0},
            {0.0, 0.0, 1.0, 0.0, 0.0}
        };
        return new Matrice(data);
    }
    
    /**
     * Symbole CROIX (×)
     */
    public static Matrice getSymboleCROIX() {
        double[][] data = {
            {1.0, 0.0, 0.0, 0.0, 1.0},
            {0.0, 1.0, 0.0, 1.0, 0.0},
            {0.0, 0.0, 1.0, 0.0, 0.0},
            {0.0, 1.0, 0.0, 1.0, 0.0},
            {1.0, 0.0, 0.0, 0.0, 1.0}
        };
        return new Matrice(data);
    }
    
    /**
     * Symbole CARRE (□)
     */
    public static Matrice getSymboleCARRE() {
        double[][] data = {
            {1.0, 1.0, 1.0, 1.0, 1.0},
            {1.0, 0.0, 0.0, 0.0, 1.0},
            {1.0, 0.0, 0.0, 0.0, 1.0},
            {1.0, 0.0, 0.0, 0.0, 1.0},
            {1.0, 1.0, 1.0, 1.0, 1.0}
        };
        return new Matrice(data);
    }
    
    // ========== EXEMPLES SUPPLÉMENTAIRES (pour partie 3) ==========
    
    /**
     * Version bruitée du symbole PLUS
     */
    public static Matrice getSymbolePLUS_v2() {
        double[][] data = {
            {0.1, 0.0, 0.9, 0.0, 0.1},
            {0.0, 0.1, 1.0, 0.0, 0.0},
            {0.9, 1.0, 1.0, 1.0, 0.9},
            {0.0, 0.0, 0.9, 0.1, 0.0},
            {0.0, 0.0, 1.0, 0.0, 0.0}
        };
        return new Matrice(data);
    }
    
    // ========== TRACÉS À RECONNAÎTRE ==========
    
    /**
     * Tracé 1 : ressemble à un PLUS bruité
     */
    public static Matrice getTrace1() {
        double[][] data = {
            {0.0, 0.0, 0.9, 0.0, 0.0},
            {0.0, 0.0, 0.8, 0.1, 0.0},
            {0.9, 1.0, 1.0, 0.9, 0.8},
            {0.0, 0.1, 0.9, 0.0, 0.0},
            {0.0, 0.0, 1.0, 0.0, 0.0}
        };
        return new Matrice(data);
    }
    
    /**
     * Tracé 2 : ressemble à une CROIX bruitée
     */
    public static Matrice getTrace2() {
        double[][] data = {
            {0.9, 0.1, 0.0, 0.0, 0.8},
            {0.1, 0.9, 0.0, 0.8, 0.1},
            {0.0, 0.0, 0.9, 0.1, 0.0},
            {0.0, 0.8, 0.1, 1.0, 0.0},
            {1.0, 0.0, 0.0, 0.0, 0.9}
        };
        return new Matrice(data);
    }
    
    /**
     * Tracé 3 : ressemble à un CARRÉ bruité
     */
    public static Matrice getTrace3() {
        double[][] data = {
            {1.0, 0.9, 1.0, 0.9, 1.0},
            {0.9, 0.1, 0.0, 0.1, 1.0},
            {1.0, 0.0, 0.0, 0.0, 0.9},
            {0.9, 0.0, 0.1, 0.0, 1.0},
            {1.0, 1.0, 0.9, 1.0, 1.0}
        };
        return new Matrice(data);
    }
    
    // ========== MÉTHODES UTILITAIRES ==========
    
    /**
     * Retourne la liste des symboles de référence
     */
    public static ArrayList<Matrice> getSymbolesReference() {
        ArrayList<Matrice> symboles = new ArrayList<>();
        symboles.add(getSymbolePLUS());
        symboles.add(getSymboleCROIX());
        symboles.add(getSymboleCARRE());
        return symboles;
    }
    
    /**
     * Retourne la liste des noms des symboles
     */
    public static ArrayList<String> getNomsSymboles() {
        ArrayList<String> noms = new ArrayList<>();
        noms.add("PLUS");
        noms.add("CROIX");
        noms.add("CARRE");
        return noms;
    }
    
    /**
     * Retourne tous les tracés à reconnaître
     */
    public static ArrayList<Matrice> getTracesAReconnaitre() {
        ArrayList<Matrice> traces = new ArrayList<>();
        traces.add(getTrace1());
        traces.add(getTrace2());
        traces.add(getTrace3());
        return traces;
    }
    
    /**
     * Affiche un symbole de manière visuelle (pour debug)
     */
    public static void afficherSymboleVisuel(Matrice m) {
        System.out.println("┌─────────────┐");
        for (int i = 0; i < m.getDimension(); i++) {
            System.out.print("│ ");
            for (int j = 0; j < m.getDimension(); j++) {
                // Utilisation de la réflexion pour accéder à m[i][j]
                String symbol = "  "; // Par défaut : blanc
                System.out.print(symbol);
            }
            System.out.println(" │");
        }
        System.out.println("└─────────────┘");
    }
}
