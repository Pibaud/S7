public interface IDictionnary<K, V> {
    int size();
    boolean isEmpty();
    boolean containsKey(K key);
    V get(K key);
    IDictionnary<K, V> put(K key, V value);
}