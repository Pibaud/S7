public class FastDictionnary extends AbstractDictionnary{

    public FastDictionnary(){
        super();
    }

    public boolean mustGrow(){
        double placesLibres = this.keys.length;
        double placesPrises = this.size;
        return placesLibres/placesPrises < 3./4;
    }

    public void grow(){
        System.out.println("MUST GROW");
        System.out.println("previous length : " + this.size);
        int len = this.keys.length;
        if(size == 0){size = 1;}
        Object[] newKeys = new Object[size + size/4];
        Object[] newValues = new Object[size + size/4];
        System.arraycopy(this.keys, 0, newKeys, 0, this.size);
        System.arraycopy(this.values, 0, newValues, 0, this.size);
        this.keys = newKeys;
        this.values = newValues;
        System.out.println("new size : " + this.size);
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
        //maintenir aux 3/4 pleins donc augmenter la taille de size += size/4
        //il faut recopier toutes les valeurs pour les mettre dans un tableau plus grand
        if(this.mustGrow()){this.grow();}
        int len = this.keys.length;
        if(len == 0){len = 1;}
        int newIndex = (key.hashCode())%len;
        while(this.keys[newIndex] != null){
            System.out.println("CONFLIT");
            newIndex++;
        }
        return newIndex;
    }
}