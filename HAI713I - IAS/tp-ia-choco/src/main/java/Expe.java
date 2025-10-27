import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.constraints.extension.Tuples;
import org.chocosolver.solver.variables.IntVar;

public class Expe {

	private static Model lireReseau(BufferedReader in) throws Exception{
			Model model = new Model("Expe");
			int nbVariables = Integer.parseInt(in.readLine());				// le nombre de variables
			int tailleDom = Integer.parseInt(in.readLine());				// la valeur max des domaines
			IntVar []var = model.intVarArray("x",nbVariables,0,tailleDom-1); 	
			int nbConstraints = Integer.parseInt(in.readLine());			// le nombre de contraintes binaires		
			for(int k=1;k<=nbConstraints;k++) { 
				String chaine[] = in.readLine().split(";");
				IntVar portee[] = new IntVar[]{var[Integer.parseInt(chaine[0])],var[Integer.parseInt(chaine[1])]}; 
				int nbTuples = Integer.parseInt(in.readLine());				// le nombre de tuples		
				Tuples tuples = new Tuples(new int[][]{},true);
				for(int nb=1;nb<=nbTuples;nb++) { 
					chaine = in.readLine().split(";");
					int t[] = new int[]{Integer.parseInt(chaine[0]), Integer.parseInt(chaine[1])};
					tuples.add(t);
				}
				model.table(portee,tuples).post();	
			}
			in.readLine();
			return model;
	}	
		
			
	public static void main(String[] args) throws Exception{
        System.out.println("DEBUT DES LECTURES DES DIFFERENTS FICHIERS");
        HashMap<String, Integer> resSol =  new HashMap<>();
        Path racine = Paths.get("../../../");
        System.out.println("PATH: "+racine);
        // faire une boucle qui trouve tous les /*.txt
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(racine, "*.txt")) {
            for (Path file : stream) {
                String ficName = String.valueOf(file.getFileName());
                System.out.println("Lecture de : " + ficName);
                resSol.put(ficName,0);
                int nbRes=3;
                BufferedReader readFile = new BufferedReader(new FileReader(ficName));
                for(int nb=1 ; nb<=nbRes; nb++) {
                    Model model=lireReseau(readFile);
                    if(model==null) {
                        System.out.println("Problème de lecture de fichier !\n");
                        return;
                    }
                    System.out.println("Réseau lu "+nb+" :\n"+model+"\n\n");
                    // Calcul de la première solution
                    if(model.getSolver().solve()) {
                        System.out.println("\n\n*** Première solution ***");
                        System.out.println(model);
                        resSol.put(ficName,resSol.get(ficName)+1);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Erreur lors de la lecture du répertoire : " + e.getMessage());
        }
		return;	
	}
}