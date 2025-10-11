package algebre;

/**
 * Représente un exemple d'entraînement pour le classifieur
 * (une matrice avec son label associé)
 */
public class Exemple {
    private Matrice trace;
    private String label;
    
    public Exemple(Matrice trace, String label) {
        this.trace = trace;
        this.label = label;
    }
    
    public Matrice getTrace() {
        return trace;
    }
    
    public String getLabel() {
        return label;
    }
    
    @Override
    public String toString() {
        return "Exemple[label=" + label + ", dimension=" + trace.getDimension() + "]";
    }
}
