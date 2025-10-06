public class FastDictionnary extends AbstractDictionnary<Object, Object> {

    public FastDictionnary(){
        super(Object.class, Object.class);
    }

    public boolean mustGrow() {
        if (keys.length == 0) return true;
        return (double) size / keys.length >= 0.75;
    }

    public void grow() {
        int newCapacity = (keys.length == 0) ? 4 : keys.length * 2;
        Object[] oldKeys = this.keys;
        Object[] oldValues = this.values;
        this.keys = new Object[newCapacity];
        this.values = new Object[newCapacity];
        for (int i = 0; i < oldKeys.length; i++) {
            if (oldKeys[i] != null) {
                int newIndex = Math.abs(oldKeys[i].hashCode()) % newCapacity;
                while (this.keys[newIndex] != null) {
                    newIndex = (newIndex + 1) % newCapacity;
                }
                this.keys[newIndex] = oldKeys[i];
                this.values[newIndex] = oldValues[i];
            }
        }
    }


    public int indexOf(Object key){
        for(int i = 0; i < keys.length; i++){
            if(keys[i] != null && keys[i].equals(key)){
                return i;
            }
        }
        return -1;
    }

    public int newIndexOf(Object key){
        if(this.mustGrow()){this.grow();}
        int len = this.keys.length;
        int newIndex = Math.abs(key.hashCode()) % len;
        while(this.keys[newIndex] != null){
            System.out.println("CONFLIT avec "+this.keys[newIndex]);
            newIndex = (newIndex + 1) % len;
        }
        return newIndex;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("FastDictionnary contents:\n");
        for (int i = 0; i < keys.length; i++) {
            sb.append("[").append(i).append("] : ");
            if (keys[i] != null) {
                sb.append(keys[i]).append(" = ").append(values[i]);
            } else {
                sb.append("null");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

}