package main;

import classifieur.Rubine;
import geste.Lexique;
import geste.Trace;

public class Main {

	public static void main(String s[]) {
		Lexique L = new Lexique();
		L.initData();

		Rubine classifieur = new Rubine();
		classifieur.init(L);

		System.out.println("Moyenne des taux de reconnaissance du lexique sur lui même : "+ classifieur.testLexique(L)*100+" %");
	}
}