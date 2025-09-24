public class FastDictionnary extends AbstractDictionnary{

    public FastDictionnary(){
        super();
    }

    public int indexOf(Object key){
        int size = this.size;
        if(size == 0){return -1;}
        int index = (key.hashCode()) % size;
        if(this.keys[index].equals(key)){ // la clé existe déjà, on veut réécrire
            return index;
        }
        return -1;
    }

    public int newIndexOf(Object key){
        //maintenir aux 3/4 pleins donc augmenter la taille de size += size/4
        //il faut recopier toutes les valeurs pour les mettre dans un tableau plus grand
        int size = this.size;
        if(size == 0){size = 1;}
        Object[] newKeys = new Object[size + size/4];
        Object[] newValues = new Object[size + size/4];
        System.arraycopy(this.keys, 0, newKeys, 0, this.size);
        System.arraycopy(this.values, 0, newValues, 0, this.size);
        this.keys = newKeys;
        this.values = newValues;
        // On n'augmente pas la taille ici, car la taille représente le nombre réel d'éléments dans les tableaux
        // et comme on n'insère pas ici, pas d'incrément de taille
        return this.size;
    }
}