import org.chocosolver.solver.Model;
import org.chocosolver.solver.constraints.nary.alldifferent.AllDifferent;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.constraints.extension.Tuples;


public class planning {

    public static void main(String[] args) {

        // Création du modele
        Model model = new Model("planning");

        // Création des variables
        IntVar t0 = model.intVar("t0", 1, 5);
        IntVar t1 = model.intVar("t1", 1, 5);
        IntVar t2 = model.intVar("t2", 1, 5);
        IntVar t3 = model.intVar("t3", 1, 5);
        IntVar t4 = model.intVar("t4", 1, 5);
        IntVar t5 = model.intVar("t5", 1, 5);
        IntVar t6 = model.intVar("t6", 1, 5);
        IntVar t7 = model.intVar("t7", 1, 5);

        IntVar Ada =  model.intVar("Ada", 1);
        IntVar Tim =  model.intVar("Tim", 2);
        IntVar Linus =  model.intVar("Linus", 3);

        IntVar S1 =  model.intVar("S1", 1);
        IntVar S2 =  model.intVar("S2", 2);
        IntVar S3 =  model.intVar("S3", 3);
        IntVar S4 =  model.intVar("S4", 4);
        IntVar S5 =  model.intVar("S5", 5);

        //C1
        model.arithm(t3, ">", t2).post();
        model.arithm(t3, ">", t1).post();
        model.arithm(t3, ">", t0).post();
        model.arithm(t4, ">", t2).post();
        model.arithm(t4, ">", t1).post();
        model.arithm(t4, ">", t0).post();

        //C2
        model.arithm(t5, ">", t4).post();
        model.arithm(t5, ">", t3).post();
        model.arithm(t5, ">", t2).post();
        model.arithm(t5, ">", t1).post();
        model.arithm(t5, ">", t0).post();

        model.arithm(t6, ">", t4).post();
        model.arithm(t6, ">", t3).post();
        model.arithm(t6, ">", t2).post();
        model.arithm(t6, ">", t1).post();
        model.arithm(t6, ">", t0).post();

        //C3
        model.arithm(t7, ">", t3).post();

        //C4
        IntVar[] servdedie = new IntVar[2];
        servdedie[0] = t0;
        servdedie[1] = t1;

        model.allDifferent(servdedie).post();

        //C5
        int [][] tEq = new int[][] {{1,0,1,2},{2,0,1,2}, {3,0,1,2}}; // représente "vivent sur la même maison"
        Tuples tuplesInterditsC5 = new Tuples(tEq,false);		// création de Tuples de valeurs autorisés

        model.table(new IntVar[]{Ada,t0}, tuplesInterditsC5).post();
        model.notAllEqual(Ada).post();

        //C6
        int [][] tC6 = new int[][] {{1,5},{1,6}, {2,5},{2,6}}; // représente "vivent sur la même maison"
        Tuples tuplesAuroisesC6 = new Tuples(tC6,true);
        //C7


        //C8

        //C10
        int [][] tC10 = new int[][] {{3,1},{3,2}, {1,5},{1,4}}; // représente "vivent sur la même maison"
        Tuples tuplesAuroisesC10 = new Tuples(tC10,true);

        //C11
        model.or()

        // Affichage du réseau de contraintes créé
        System.out.println("*** Réseau Initial ***");
        System.out.println(model);


        // Calcul de la première solution
        if(model.getSolver().solve()) {
            System.out.println("\n\n*** Première solution ***");
            System.out.println(model);
        }


/*
    	// Calcul de toutes les solutions
    	System.out.println("\n\n*** Autres solutions ***");
        while(model.getSolver().solve()) {
            System.out.println("Sol "+ model.getSolver().getSolutionCount()+"\n"+model);
	    }
*/


        // Affichage de l'ensemble des caractéristiques de résolution
        System.out.println("\n\n*** Bilan ***");
        model.getSolver().printStatistics();
    }
}
