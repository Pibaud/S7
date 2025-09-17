import IDictionnary;

public abstract class AbstractDictionnary{
    protected Object[] keys;
    protected Object[] values;
    protected int size;

    public AbstractDictionnary(){
        this.keys = new ArrayList<Object>();
        this.values = new ArrayList<Object>();
    }

    public abstract int indexOf(Object key);

    public abstract int newIndexOf(Object key);

    public int size(){return size;}

    public Object get(Object key){
        return values[indexOf(key)];
    }

    public IDictionnary put(Object key, Object value){
        int res = newIndexOf(key);
        if(res == -1){
            values[indexOf(key)] = value;
        }
        keys[res-1] = key;
        values[newIndexOf(key)] = value;
        return this
    }

    public boolean isEmpty(){return size == 0;}

    public int containsKey(Object key){
        return indexOf(key);
    }
}