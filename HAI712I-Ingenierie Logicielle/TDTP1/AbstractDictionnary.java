public abstract class AbstractDictionnary<K, V> implements IDictionnary<K, V> {
    protected K[] keys;
    protected V[] values;
    protected int size;

    @SuppressWarnings("unchecked")
    public AbstractDictionnary(Class<K> keyClass, Class<V> valueClass){
        this.keys = (K[]) java.lang.reflect.Array.newInstance(keyClass, 10); // taille initiale arbitraire
        this.values = (V[]) java.lang.reflect.Array.newInstance(valueClass, 10);
        this.size = 0;
    }

    public abstract int indexOf(K key);

    public abstract int newIndexOf(K key);

    public int size(){return size;}

    public V get(K key){
        return values[indexOf(key)];
    }

    public IDictionnary<K, V> put(K key, V value){
        int index = indexOf(key);
        System.out.println("put: " + key + " = " + value);
        if(index == -1){ // la clé n'est pas dans le dictionnaire
            index = newIndexOf(key);
            System.out.println("index après newIndexOf: " + index);
            keys[index] = key;
            values[index] = value;
            this.size ++;
            return this;
        }
        System.out.println("existe déjà");
        values[index] = value;
        return this;    
    }

    public boolean isEmpty(){return size == 0;}

    public boolean containsKey(K key){
        return indexOf(key) != -1;
    }
}