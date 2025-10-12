package algebre;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ClassifieurQDA {

    // Stocke le vecteur moyen pour chaque classe
    private Map<String, Vecteur> moyennesParClasse;
    
    // Stocke l'inverse de la matrice de covariance pour chaque classe
    private Map<String, Matrice> covInverseesParClasse;

    public ClassifieurQDA() {
        moyennesParClasse = new HashMap<>();
        covInverseesParClasse = new HashMap<>();
    }

    /**
     * Entraîne le classifieur sur un ensemble de données.
     * @param donneesEntrainement Une map où la clé est le nom de la classe et la valeur est une liste de vecteurs.
     */
    public void entrainer(Map<String, ArrayList<Vecteur>> donneesEntrainement) {
        System.out.println("--- Début de l'entraînement ---");
        
        for (Map.Entry<String, ArrayList<Vecteur>> entry : donneesEntrainement.entrySet()) {
            String nomClasse = entry.getKey();
            ArrayList<Vecteur> vecteurs = entry.getValue();

            if (vecteurs.isEmpty() || vecteurs.get(0).getDimension() < 1) {
                System.out.println("Classe '" + nomClasse + "' ignorée car les données sont invalides.");
                continue;
            }

            // 1. Calculer le vecteur moyen
            Vecteur moyenne = Vecteur.esperance(vecteurs);
            moyennesParClasse.put(nomClasse, moyenne);
            System.out.println("Moyenne pour la classe '" + nomClasse + "': " + moyenne);

            // 2. Calculer la matrice de covariance
            // Note: La méthode covariance dans Matrice semble avoir une erreur de conception.
            // On la recrée ici de manière plus standard.
            Matrice covariance = calculerCovariance(vecteurs, moyenne);
            System.out.println("Matrice de covariance pour la classe '" + nomClasse + "':\n" + covariance);

            // 3. Inverser la matrice de covariance
            try {
                Matrice covInversee = covariance.inverse();
                covInverseesParClasse.put(nomClasse, covInversee);
                System.out.println("Inverse de la covariance pour la classe '" + nomClasse + "' calculée.");
            } catch (ArithmeticException e) {
                System.err.println("Erreur: La matrice de covariance pour la classe '" + nomClasse + "' n'est pas inversible.");
                // Gérer l'erreur, par exemple en ignorant cette classe ou en utilisant une autre technique.
            }
             System.out.println("---------------------------------");
        }
        System.out.println("--- Entraînement terminé ---\n");
    }

    /**
     * Prédit la classe d'un nouveau vecteur.
     * @param vecteur Le vecteur à classifier.
     * @return Le nom de la classe la plus probable.
     */
    public String classifier(Vecteur vecteur) {
        double distanceMin = Double.POSITIVE_INFINITY;
        String meilleureClasse = null;

        for (String nomClasse : moyennesParClasse.keySet()) {
            Vecteur moyenne = moyennesParClasse.get(nomClasse);
            Matrice covInversee = covInverseesParClasse.get(nomClasse);

            if (moyenne == null || covInversee == null) {
                continue; // Si une classe n'a pas pu être modélisée correctement
            }

            // Calcul de la distance de Mahalanobis au carré: (x - mu)^T * Sigma^-1 * (x - mu)
            
            // 1. Calculer (x - mu)
            double[] diffCoords = new double[vecteur.getDimension()];
            for (int i = 0; i < vecteur.getDimension(); i++) {
                diffCoords[i] = vecteur.get(i) - moyenne.get(i);
            }
            Vecteur diff = new Vecteur(diffCoords);

            // 2. Calculer Sigma^-1 * (x - mu)
            Vecteur temp = covInversee.mult(diff);

            // 3. Calculer (x - mu)^T * (résultat précédent)
            double distance = diff.produitScalaire(temp);
            
            System.out.printf("Distance à la classe '%s': %.4f\n", nomClasse, distance);

            if (distance < distanceMin) {
                distanceMin = distance;
                meilleureClasse = nomClasse;
            }
        }
        return meilleureClasse;
    }

    /**
     * Une implémentation standard de la matrice de covariance.
     */
    private Matrice calculerCovariance(ArrayList<Vecteur> vecteurs, Vecteur moyenne) {
        int dimension = moyenne.getDimension();
        int n = vecteurs.size();
        Matrice covariance = new Matrice(dimension); // Initialisée à zéro

        // Initialiser la matrice à zéro
        for(int i=0; i<dimension; i++) {
            for(int j=0; j<dimension; j++) {
                covariance.set(i, j, 0.0);
            }
        }

        for (Vecteur v : vecteurs) {
            Vecteur diff = new Vecteur(dimension);
            for (int i = 0; i < dimension; i++) {
                diff.set(i, v.get(i) - moyenne.get(i));
            }

            // Ajoute le produit extérieur (diff * diff^T) à la matrice
            for (int i = 0; i < dimension; i++) {
                for (int j = 0; j < dimension; j++) {
                    covariance.set(i, j, covariance.get(i, j) + diff.get(i) * diff.get(j));
                }
            }
        }

        // Diviser par (n-1) pour obtenir l'estimateur non biaisé
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                covariance.set(i, j, covariance.get(i, j) / (n - 1));
            }
        }
        return covariance;
    }
    
    // --- Main pour tester le classifieur ---
    public static void main(String[] args) {
        // 1. Créer des données d'entraînement synthétiques
        Map<String, ArrayList<Vecteur>> donnees = new HashMap<>();

        // Classe A : Centrée autour de (2, 2)
        ArrayList<Vecteur> classeA = new ArrayList<>();
        classeA.add(new Vecteur(new double[]{2.1, 2.2}));
        classeA.add(new Vecteur(new double[]{1.9, 2.0}));
        classeA.add(new Vecteur(new double[]{2.3, 2.4}));
        classeA.add(new Vecteur(new double[]{1.8, 1.9}));
        donnees.put("Classe A", classeA);
        
        // Classe B : Centrée autour de (8, 8) avec plus de variance
        ArrayList<Vecteur> classeB = new ArrayList<>();
        classeB.add(new Vecteur(new double[]{8.5, 7.5}));
        classeB.add(new Vecteur(new double[]{7.5, 8.5}));
        classeB.add(new Vecteur(new double[]{9.0, 9.0}));
        classeB.add(new Vecteur(new double[]{7.0, 7.0}));
        donnees.put("Classe B", classeB);
        
        // 2. Créer et entraîner le classifieur
        ClassifieurQDA monClassifieur = new ClassifieurQDA();
        monClassifieur.entrainer(donnees);
        
        // 3. Classifier de nouveaux points
        System.out.println("--- Début de la classification ---");
        Vecteur pointProcheDeA = new Vecteur(new double[]{2.5, 2.5});
        String resultat1 = monClassifieur.classifier(pointProcheDeA);
        System.out.println("Le point " + pointProcheDeA + " est classifié comme : " + resultat1 + "\n");

        Vecteur pointProcheDeB = new Vecteur(new double[]{8.1, 7.9});
        String resultat2 = monClassifieur.classifier(pointProcheDeB);
        System.out.println("Le point " + pointProcheDeB + " est classifié comme : " + resultat2 + "\n");
        
        Vecteur pointIntermediaire = new Vecteur(new double[]{5.0, 5.0});
        String resultat3 = monClassifieur.classifier(pointIntermediaire);
        System.out.println("Le point " + pointIntermediaire + " est classifié comme : " + resultat3 + "\n");
    }
}