public class OrderedDictionnary extends AbstractDictionnary{

    public OrderedDictionnary(){
        super();
    }

    public int indexOf(Object key){
        for(int i = 0; i < size; i++){
            if(keys[i].equals(key)){
                return i;
            }
        }
        return -1;
    }

    public int newIndexOf(Object key){
        //il faut recopier toutes les valeurs pour les mettre dans un tableau plus grand
        Object[] newKeys = new Object[this.size + 1];
        Object[] newValues = new Object[this.size + 1];
        System.arraycopy(this.keys, 0, newKeys, 0, this.size);
        System.arraycopy(this.values, 0, newValues, 0, this.size);
        this.keys = newKeys;
        this.values = newValues;
        int index = this.size;
        // On n'augmente pas la taille ici, car la taille représente le nombre réel d'éléments dans les tableaux
        // et comme on n'insère pas ici, pas d'incrément de taille
        return index;
    }
    /*
        Une affectation polymorphique en Java (ou dans d’autres langages orientés objet) consiste à affecter à une variable 
        d’un type parent (classe ou interface) une instance d’une classe fille (ou d’une classe qui implémente l’interface).

        Exemple :
            AbstractDictionnary dico = new OrderedDictionnary();    

        Ici, dico est de type AbstractDictionnary, mais référence un objet de type OrderedDictionnary.
        Cela permet d’utiliser le polymorphisme : on peut appeler les méthodes de AbstractDictionnary sur dico, et si elles 
        sont redéfinies dans OrderedDictionnary, c’est la version de la classe fille qui sera utilisée à l’exécution.
     */
}