import org.chocosolver.solver.Model;
import org.chocosolver.solver.constraints.nary.alldifferent.AllDifferent;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.constraints.extension.Tuples;

public class NReines {
    public static void main(String[] args) {
        int n = 4;
        // Création du modele
        Model model = new Model("NReines");

        IntVar[] Reines = model.intVarArray("Ri", n, 1, n); // crée un tableau de 5 variables entières de domaine [1,n]

        model.allDifferent(Reines).post(); // toutes sur des colonnes différentes

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) { // <-- j commence à i + 1
                // On compare la reine i avec toutes les reines suivantes

                // Puisque j > i, Math.abs(i - j) est simplement (j - i)
                model.distance(Reines[i], Reines[j], "!=", j - i).post();
            }
        }

        System.out.println("*** Réseau Initial ***");
        System.out.println(model);


        // Calcul de la première solution
        if(model.getSolver().solve()) {
            System.out.println("\n\n*** Première solution ***");
            System.out.println(model);
        }
    }
}