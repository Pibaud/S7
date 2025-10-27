package main;

import java.text.DecimalFormat;

import classifieur.Rubine;
import geste.Lexique;

public class Main {

	public static void main(String s[]) {
		Lexique trainLex = new Lexique();
		trainLex.initData("train");

		Lexique testLex = new Lexique();
		testLex.initData("test");

		Rubine classifieur = new Rubine();
		classifieur.init(trainLex);

		System.out.println("Moyenne des taux de reconnaissance sur train : " + classifieur.testLexique(trainLex) * 100 + " %");
		System.out.println("Moyenne des taux de reconnaissance sur test  : " + classifieur.testLexique(testLex) * 100 + " %");

		System.out.println("-------Pourcentage de reconnaissance par geste--------");
		double[] tauxTrain = classifieur.test(trainLex);
		DecimalFormat df = new DecimalFormat("0.00%");
		double[] tauxTest = classifieur.test(testLex);
		int n = Math.max(tauxTrain.length, tauxTest.length);
		System.out.printf("%9s | %8s | %8s%n", "gesture", "train", "test");
		System.out.println("-------------------------------");
		for (int i = 0; i < n; i++) {
			System.out.printf("%9s | %8s | %8s%n", trainLex.getGestes().get(i).getName(), df.format(tauxTrain[i]), df.format(tauxTest[i]));
		}
	}
}