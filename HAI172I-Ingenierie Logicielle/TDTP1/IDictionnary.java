interface IDictionnary {
    public Object get(Object key);
    public IDictionnary put(Object key, Object value);
    public boolean isEmpty();
    public boolean containsKey(Object key);
    public int size();
}