public class UtilisationDictionnaire {
    static IDictionnary dic;

    public static void main(String[] args) {
        dic = new OrderedDictionnary();
        dic.put("Lavoisier", "Chimiste Français");
        System.out.println("taille : " + dic.size());
        System.out.println("Lavoisier : " + dic.get("Lavoisier"));
        System.out.println("isEmpty = " + dic.isEmpty());
        System.out.println("containsKey 'Lavoisier' = " + dic.containsKey("Lavoisier"));
    }
}   