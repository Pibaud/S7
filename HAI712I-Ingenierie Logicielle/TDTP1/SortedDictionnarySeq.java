/*
REPONSES AUX QUESTIONS
13) Ne résout pas le pb car les méthodes attendent toujours des Object et on ne vérifie pas le type à l'insertion
14) Idem, rien n'assure que les clés soient de type Comparable

POURQUOI LE PARAMETRAGE PAR <K extends Comparable<K>, V> MARCHE (voir partie 6 du cours de Dony):
Déclarer SortedDictionary<K extends Comparable<K>, V> signifie que toute clé K utilisée doit implémenter Comparable<K>
Cependant la classe abstraite doit être paramétrée par AbstractDictionary<K, V> et forcément l'interface qu'elle implémente
IDictionnary<K, V>.
Et donc les autres classes qui extend la classe abstraites mais qui n'ont pas de contrainte devront paramétrer par
<Object, Object>

 */

public class SortedDictionnarySeq<K extends Comparable<K>, V> extends AbstractDictionnary<K, V> {

    public SortedDictionnarySeq(Class<K> keyClass, Class<V> valueClass){
        super(keyClass, valueClass);
    }

    public int indexOf(K key){
        for(int i = 0; i < keys.length; i++){
            if(keys[i] != null && keys[i].equals(key)){
                return i;
            }
        }   
        return -1;
    }

    @SuppressWarnings("unchecked")
    public int newIndexOf(K key){
        //il faut recopier toutes les valeurs pour les mettre dans un tableau plus grand
        K[] newKeys = (K[]) java.lang.reflect.Array.newInstance(key.getClass(), this.size + 1);
        V[] newValues = (V[]) java.lang.reflect.Array.newInstance(Object.class, this.size + 1);
        System.arraycopy(this.keys, 0, newKeys, 0, this.size);
        System.arraycopy(this.values, 0, newValues, 0, this.size);
        this.keys = newKeys;
        this.values = newValues;

        //trouver l'index où insérer la nouvelle clé
        int insertIndex = 0;
        while (insertIndex < keys.length && keys[insertIndex] != null && keys[insertIndex].compareTo(key) < 0) {
            insertIndex++;
        }

        //décaler les autres
        for(int offsetIndex=keys.length-1; offsetIndex > insertIndex;  --offsetIndex){
            if(offsetIndex == insertIndex){
                break;
            }
            keys[offsetIndex] = keys[offsetIndex-1];
            values[offsetIndex] = values[offsetIndex-1];
        }

        return insertIndex;
    }
}
