public class UtilisationDictionnaire {
    static SortedDictionnarySeq<String, Object> dic;

    public static void main(String[] args) {
/*
        System.out.println("===== Ordered Dictionnary =====");
        dic = new OrderedDictionnary();
        dic.put("Lavoisier", "Chimiste Français");
        System.out.println("taille : " + dic.size());
        System.out.println("Lavoisier : " + dic.get("Lavoisier"));
        System.out.println("isEmpty = " + dic.isEmpty());
        System.out.println("containsKey 'Lavoisier' = " + dic.containsKey("Lavoisier"));


        System.out.println("===== Fast Dictionnary =====");
        dic = new FastDictionnary();
        dic.put("Lavoisier", "Chimiste Français");
        System.out.println(dic.toString());
        dic.put("Einstein", "Physicien");
        System.out.println(dic.toString());
        dic.put("Euler", "Mathématicien");
        System.out.println(dic.toString());
        dic.put("Newton", "Physicien et Mathématicien");
        System.out.println(dic.toString());
        dic.put("Curie", "Physicienne et Chimiste");
        System.out.println(dic.toString());
        dic.put("Galilée", "Astronome et Physicien");
        System.out.println(dic.toString());
        dic.put("Pascal", "Mathématicien et Philosophe");
        System.out.println(dic.toString());
        dic.put("Darwin", "Naturaliste");
        System.out.println(dic.toString());
        dic.put("Descartes", "Philosophe et Mathématicien");
        System.out.println(dic.toString());
        dic.put("Bohr", "Physicien");
        System.out.println(dic.toString());


 */

        System.out.println("===== Sorted Dictionnary =====");
    dic = new SortedDictionnarySeq<>(String.class, Object.class);
        // Insertion simple
        dic.put("B", "b");
        dic.put("A", "a");
        dic.put("C", "c");

        // Affichage tableau interne
        System.out.println("Après insertion B, A, C:");
        System.out.println(dic);

        // Tester ordre attendu : A, B, C
        System.out.println("Index de A : " + dic.indexOf("A")); // doit être 0
        System.out.println("Index de B : " + dic.indexOf("B")); // doit être 1
        System.out.println("Index de C : " + dic.indexOf("C")); // doit être 2

        // Cas insertion au début
        dic.put("0", "zero");
        System.out.println("Après insertion 0:");
        System.out.println(dic);

        // Cas insertion au milieu
        dic.put("BB", "bb");
        System.out.println("Après insertion BB:");
        System.out.println(dic);

        // Cas insertion à la fin
        dic.put("Z", "z");
        System.out.println("Après insertion Z:");
        System.out.println(dic);

        // Vérification size
        System.out.println("Taille finale : " + dic.size());
    }
}   