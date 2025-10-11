package algebre;

import java.util.*;

/**
 * Interface générique pour un classifieur de symboles
 */
public interface IClassifieur {
    
    /**
     * Entraîne le classifieur sur un ensemble de données
     * @param donnees Liste d'exemples d'entraînement
     */
    void entrainer(ArrayList<Exemple> donnees);
    
    /**
     * Prédit le label d'un tracé inconnu
     * @param trace Matrice à classifier
     * @return Le label prédit
     */
    String predire(Matrice trace);
    
    /**
     * Prédit les probabilités pour chaque classe
     * @param trace Matrice à classifier
     * @return Map des labels avec leurs probabilités
     */
    Map<String, Double> predireProbabilites(Matrice trace);
    
    /**
     * Évalue les performances sur un ensemble de test
     * @param donneesTest Données de test
     * @return Taux de réussite (entre 0 et 1)
     */
    double evaluer(ArrayList<Exemple> donneesTest);
}
