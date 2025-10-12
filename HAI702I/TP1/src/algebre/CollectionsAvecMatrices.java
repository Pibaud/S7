package algebre;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CollectionsAvecMatrices {

    public static void main(String[] args) {

        System.out.println("==== ArrayList de matrices ====");

        // Création d'une ArrayList de matrices
        ArrayList<Matrice> matrices = new ArrayList<>();

        Matrice m1 = new Matrice(new double[][]{{1, 2}, {3, 4}});
        Matrice m2 = new Matrice(new double[][]{{5, 6}, {7, 8}});
        Matrice m3 = new Matrice(new double[][]{{2, 0}, {0, 2}});

        matrices.add(m1);
        matrices.add(m2);
        matrices.add(m3);

        System.out.println("Liste de matrices :");
        for (Matrice m : matrices) {
            m.print();
        }

        // Somme de toutes les matrices
        Matrice somme = new Matrice(m1.getDimension());
        for (Matrice m : matrices) {
            somme = somme.somme(m);
        }
        System.out.println("Somme de toutes les matrices :");
        somme.print();

        // Parcours avec index
        System.out.println("Parcours avec index :");
        for (int i = 0; i < matrices.size(); i++) {
            System.out.println("Matrice #" + i + " :");
            matrices.get(i).print();
        }

        System.out.println("\n==== HashMap de matrices ====");

        // Création d'une HashMap qui associe une clé à chaque matrice
        HashMap<String, Matrice> mapMatrices = new HashMap<>();
        mapMatrices.put("Identité", new Matrice(2));
        mapMatrices.put("m1", m1);
        mapMatrices.put("m2", m2);

        // Accès à une matrice par clé
        System.out.println("Matrice 'm1' :");
        mapMatrices.get("m1").print();

        // Parcours des clés
        System.out.println("Parcours des clés :");
        for (String key : mapMatrices.keySet()) {
            System.out.println(key);
        }

        // Parcours des valeurs
        System.out.println("Parcours des matrices :");
        for (Matrice mat : mapMatrices.values()) {
            mat.print();
        }

        // Parcours des entrées clé + valeur
        System.out.println("Parcours des entrées (clé + matrice) :");
        for (Map.Entry<String, Matrice> entry : mapMatrices.entrySet()) {
            System.out.println("Clé : " + entry.getKey());
            entry.getValue().print();
        }

        // Exemple concret : classifier une matrice avec les références dans la HashMap
        Matrice x = new Matrice(new double[][]{{1.1, 0}, {0, 1.0}});
        ArrayList<Matrice> refs = new ArrayList<>(mapMatrices.values());
        int classe = x.classifier(refs);
        System.out.println("Classe prédite pour x (indice dans refs) : " + classe); // selon implementation de classifier
    }
}