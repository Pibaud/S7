public abstract class AbstractDictionnary implements IDictionnary {
    protected Object[] keys;
    protected Object[] values;
    protected int size;

    public AbstractDictionnary(){
        this.keys = new Object[0];
        this.values = new Object[0];
        this.size = 0;
    }

    public abstract int indexOf(Object key);

    public abstract int newIndexOf(Object key);

    public int size(){return size;}

    public Object get(Object key){
        return values[indexOf(key)];
    }

    public IDictionnary put(Object key, Object value){
        int index = indexOf(key);
        System.out.println("put: " + key + " = " + value);
        System.out.println("index: " + index);
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

    public boolean containsKey(Object key){
        return indexOf(key) != -1;
    }
}