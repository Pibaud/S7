import java.io.*;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
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
        // définitions des constantes pour calculer la dureté
        System.out.println("DEBUT DES LECTURES DES DIFFERENTS FICHIERS");
        HashMap<Double, Double> fonctionPourcentage =  new HashMap<>(); // clés = dureté, valeurs = % de réseau ayant ue solution
        HashMap<Double, Double> nbNoeudsExplores = new HashMap<>(); // clés = dureté, valeurs = %nb de noeuds explorés
        Path racine = Paths.get(".");
        // faire une boucle qui trouve tous les /*.txt
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(racine, "*.txt")) {
            for (Path file : stream) {
                String ficName = String.valueOf(file.getFileName());
                System.out.println("Lecture de : " + ficName);
                //récupérer le nombre de tuples interdits en faisant nbTuples interdits = nbTuplesPossibles - cspxxx.txt
                //dureté = nbTuplesInterdits/nbTuplesPossibles
                String data = ficName.substring(3, ficName.length() - 4); // pour enlever préfixe et suffixe et avoir les xxx

                String[] parametres = data.split("-");

                int nbVars = Integer.parseInt(parametres[0]);
                int tailleDomaine = Integer.parseInt(parametres[1]);
                int nbConstraints = Integer.parseInt(parametres[2]);
                int nbTuplesAutorises = Integer.parseInt(parametres[3]);
                int nbRes = Integer.parseInt(parametres[4]);
                int nbTuplesPossibles = (int) Math.pow(tailleDomaine, 2);

                int nbTuplesInterdits = nbTuplesPossibles - nbTuplesAutorises;
                double durete = (double) nbTuplesInterdits / nbTuplesPossibles;
                double densite = (double) (2 * nbConstraints) / (nbVars * (nbVars - 1));

                System.out.println("Dureté : "+durete + " | Densité : " + densite);

                int totalResFound = 0;
                int totalTimeouts = 0;
                long totalNodesSum = 0;

                BufferedReader readFile = new BufferedReader(new FileReader(ficName));
                for(int nb=1 ; nb<=nbRes; nb++) {
                    Model model=lireReseau(readFile);
                    System.out.println("Réseau lu "+nb+" :\n"+model+"\n\n");
                    // Calcul de la première solution
                    Solver solver = model.getSolver();
                    solver.limitTime("10s");

                    ArrayList<Long> nodeCounts = new ArrayList<>();
                    boolean thisNetworkIsSolvable = false;
                    boolean thisNetworkTimedOut = false;

                    // boucler 5 fois pour faire une moyenne consolidée du nb de noeuds
                    for (int i = 0; i < 5; i++) {
                        solver.reset(); // réinitialise le solveur sinon pas de changement entre les itérations

                        if(solver.solve()) {
                            thisNetworkIsSolvable = true;
                            nodeCounts.add(solver.getNodeCount());
                        } else if (solver.isStopCriterionMet()) {
                            thisNetworkTimedOut = true;
                            System.out.println("TIMEOUT, on l'exclut.");
                            break; // inutile de continuer
                        } else {
                            // si insolvable
                            thisNetworkIsSolvable = false;
                            nodeCounts.add(solver.getNodeCount());
                        }
                    }

                    if (thisNetworkTimedOut) {
                        totalTimeouts++;
                    } else if (nodeCounts.size() >= 3) {
                        if (thisNetworkIsSolvable) {
                            totalResFound++;
                        }
                        Collections.sort(nodeCounts);
                        nodeCounts.remove(0); // sans le min
                        nodeCounts.remove(nodeCounts.size() - 1); // sans le max

                        long sumOfMiddleRuns = 0;
                        for(long count : nodeCounts) {
                            sumOfMiddleRuns += count;
                        }
                        totalNodesSum += (sumOfMiddleRuns / nodeCounts.size()); // Ajoute la moyenne robuste au total

                    } else if (!nodeCounts.isEmpty()) {
                        // Pas assez de runs pour la moyenne robuste (cas < 3), on fait une moyenne simple
                        if (thisNetworkIsSolvable) totalResFound++;
                        long sum = 0;
                        for(long count : nodeCounts) { sum += count; }
                        totalNodesSum += (sum / nodeCounts.size());
                    }
                }
                System.out.println("Nombre de timeouts (non pris en compte dans la moyenne) : "+totalTimeouts);
                if(totalTimeouts > 0.7*nbRes) {
                    System.out.println("TROP DE T/O IL FAUT AUGMENTER LE TIMER LIMITE");
                    return;
                }

                int nbResValides = nbRes - totalTimeouts;
                double pourcentage = 0.0;
                double moyNoeuds = 0.0;

                if (nbResValides > 0) {
                    pourcentage = ((double) totalResFound / nbResValides) * 100.0;
                    moyNoeuds = (double) totalNodesSum / nbResValides;
                }

                fonctionPourcentage.put(durete, pourcentage);
                nbNoeudsExplores.put(durete, moyNoeuds);
            }
        } catch (IOException e) {
            System.err.println("Erreur lors de la lecture du répertoire : " + e.getMessage());
        }
        exporterEnCsv(fonctionPourcentage, nbNoeudsExplores);
    }

    public static void exporterEnCsv(HashMap<Double, Double> dataPourcentage, HashMap<Double, Double> dataNoeuds) {
        String nomFichier = "resultats_complets.csv";
        ArrayList<Double> duretesTriees = new ArrayList<>(dataPourcentage.keySet());

        // si jamais les duretés ne sont pas triées
        Collections.sort(duretesTriees);
        try (PrintWriter writer = new PrintWriter(new FileWriter(nomFichier))) {

            writer.println("dureté;% solvables;noeuds_moyens");
            for (Double durete : duretesTriees) {
                Double pourcentage = dataPourcentage.get(durete);
                Double noeuds = dataNoeuds.get(durete);

                writer.println(durete + ";" + pourcentage + ";" + noeuds);
            }

            System.out.println("\n--- Export CSV complet terminé ---");
            System.out.println("Fichier généré : " + nomFichier);
            System.out.println("Ouvrez ce fichier pour tracer les 2 courbes.");

        } catch (IOException e) {
            // Gérer les erreurs d'écriture de fichier
            System.err.println("Erreur critique lors de l'écriture du fichier CSV : " + e.getMessage());
            e.printStackTrace();
        }
    }
}