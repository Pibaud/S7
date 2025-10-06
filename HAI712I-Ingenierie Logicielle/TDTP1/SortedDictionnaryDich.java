public class SortedDictionnaryDich<K extends Comparable<K>, V> extends AbstractDictionnary<K, V> {

    public SortedDictionnaryDich(Class<K> keyClass, Class<V> valueClass) {
        super(keyClass, valueClass);
    }

    public int indexOf(K key) {
        int left = 0;
        int right = size - 1;
        while (left <= right) {
            int mid = (left + right) / 2;
            if (keys[mid] == null)
                break;
            int cmp = keys[mid].compareTo(key);
            if (cmp == 0)
                return mid;
            if (cmp < 0)
                left = mid + 1;
            else
                right = mid - 1;
        }
        return -1;
    }

    @SuppressWarnings("unchecked")
    public int newIndexOf(K key) {
        // il faut recopier toutes les valeurs pour les mettre dans un tableau plus
        // grand
        K[] newKeys = (K[]) java.lang.reflect.Array.newInstance(key.getClass(), this.size + 1);
        V[] newValues = (V[]) java.lang.reflect.Array.newInstance(Object.class, this.size + 1);
        System.arraycopy(this.keys, 0, newKeys, 0, this.size);
        System.arraycopy(this.values, 0, newValues, 0, this.size);
        this.keys = newKeys;
        this.values = newValues;

        // trouver l'index où insérer la nouvelle clé
        int left = 0;
        int right = size - 1;
        while (left <= right) {
            int mid = (left + right) / 2;
            int cmp = keys[mid].compareTo(key);
            if (cmp < 0) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        int insertIndex = left;

        // décaler les autres
        for (int offsetIndex = keys.length - 1; offsetIndex > insertIndex; --offsetIndex) {
            if (offsetIndex == insertIndex) {
                break;
            }
            keys[offsetIndex] = keys[offsetIndex - 1];
            values[offsetIndex] = values[offsetIndex - 1];
        }

        return insertIndex;
    }

}
