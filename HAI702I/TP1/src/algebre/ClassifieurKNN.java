package algebre;

import java.util.*;

/**
 * Classifieur k-NN (k plus proches voisins)
 * Algorithme simple et efficace pour la reconnaissance de symboles
 */
public class ClassifieurKNN implements IClassifieur {
    
    private ArrayList<Exemple> donneesEntrainement;
    private int k; // Nombre de voisins à considérer
    private boolean normaliser; // Normaliser les matrices avant comparaison
    
    /**
     * Constructeur avec k=1 (1 plus proche voisin)
     */
    public ClassifieurKNN() {
        this(1, false);
    }
    
    /**
     * Constructeur avec paramètres
     * @param k Nombre de voisins à considérer
     * @param normaliser Si true, normalise les matrices avant comparaison
     */
    public ClassifieurKNN(int k, boolean normaliser) {
        this.k = k;
        this.normaliser = normaliser;
        this.donneesEntrainement = new ArrayList<>();
    }
    
    @Override
    public void entrainer(ArrayList<Exemple> donnees) {
        this.donneesEntrainement = new ArrayList<>(donnees);
        System.out.println("Entraînement terminé avec " + donnees.size() + " exemples.");
    }
    
    @Override
    public String predire(Matrice trace) {
        if (donneesEntrainement.isEmpty()) {
            throw new IllegalStateException("Le classifieur n'a pas été entraîné !");
        }
        
        // Normaliser si nécessaire
        Matrice tracePretraitee = normaliser ? trace.normaliser() : trace;
        
        // Calculer les distances à tous les exemples d'entraînement
        ArrayList<VoisinDistance> voisins = new ArrayList<>();
        
        for (Exemple ex : donneesEntrainement) {
            Matrice refPretraitee = normaliser ? ex.getTrace().normaliser() : ex.getTrace();
            double distance = tracePretraitee.distanceFrobenius(refPretraitee);
            voisins.add(new VoisinDistance(ex.getLabel(), distance));
        }
        
        // Trier par distance croissante
        Collections.sort(voisins);
        
        // Vote majoritaire parmi les k plus proches voisins
        return voteMajoritaire(voisins.subList(0, Math.min(k, voisins.size())));
    }
    
    @Override
    public Map<String, Double> predireProbabilites(Matrice trace) {
        if (donneesEntrainement.isEmpty()) {
            throw new IllegalStateException("Le classifieur n'a pas été entraîné !");
        }
        
        // Normaliser si nécessaire
        Matrice tracePretraitee = normaliser ? trace.normaliser() : trace;
        
        // Calculer les distances à tous les exemples
        ArrayList<VoisinDistance> voisins = new ArrayList<>();
        
        for (Exemple ex : donneesEntrainement) {
            Matrice refPretraitee = normaliser ? ex.getTrace().normaliser() : ex.getTrace();
            double distance = tracePretraitee.distanceFrobenius(refPretraitee);
            voisins.add(new VoisinDistance(ex.getLabel(), distance));
        }
        
        // Trier par distance croissante
        Collections.sort(voisins);
        
        // Calculer les probabilités basées sur les k plus proches voisins
        return calculerProbabilites(voisins.subList(0, Math.min(k, voisins.size())));
    }
    
    @Override
    public double evaluer(ArrayList<Exemple> donneesTest) {
        if (donneesTest.isEmpty()) return 0.0;
        
        int correct = 0;
        for (Exemple ex : donneesTest) {
            String prediction = predire(ex.getTrace());
            if (prediction.equals(ex.getLabel())) {
                correct++;
            }
        }
        
        return (double) correct / donneesTest.size();
    }
    
    /**
     * Vote majoritaire parmi les voisins
     */
    private String voteMajoritaire(List<VoisinDistance> voisins) {
        Map<String, Integer> votes = new HashMap<>();
        
        for (VoisinDistance v : voisins) {
            votes.put(v.label, votes.getOrDefault(v.label, 0) + 1);
        }
        
        // Trouver le label avec le plus de votes
        String meilleur = null;
        int maxVotes = 0;
        
        for (Map.Entry<String, Integer> entry : votes.entrySet()) {
            if (entry.getValue() > maxVotes) {
                maxVotes = entry.getValue();
                meilleur = entry.getKey();
            }
        }
        
        return meilleur;
    }
    
    /**
     * Calcule les probabilités basées sur les distances inverses
     */
    private Map<String, Double> calculerProbabilites(List<VoisinDistance> voisins) {
        Map<String, Double> scores = new HashMap<>();
        double sommeInverses = 0.0;
        
        // Calculer les scores inversés (plus la distance est petite, plus le score est grand)
        for (VoisinDistance v : voisins) {
            double score = 1.0 / (v.distance + 0.001); // +0.001 pour éviter division par 0
            scores.put(v.label, scores.getOrDefault(v.label, 0.0) + score);
            sommeInverses += score;
        }
        
        // Normaliser pour obtenir des probabilités (somme = 1)
        Map<String, Double> probabilites = new HashMap<>();
        for (Map.Entry<String, Double> entry : scores.entrySet()) {
            probabilites.put(entry.getKey(), entry.getValue() / sommeInverses);
        }
        
        return probabilites;
    }
    
    /**
     * Classe interne pour stocker un voisin avec sa distance
     */
    private static class VoisinDistance implements Comparable<VoisinDistance> {
        String label;
        double distance;
        
        VoisinDistance(String label, double distance) {
            this.label = label;
            this.distance = distance;
        }
        
        @Override
        public int compareTo(VoisinDistance autre) {
            return Double.compare(this.distance, autre.distance);
        }
    }
    
    // Getters
    public int getK() { return k; }
    public void setK(int k) { this.k = k; }
    public boolean isNormaliser() { return normaliser; }
    public void setNormaliser(boolean normaliser) { this.normaliser = normaliser; }
}
