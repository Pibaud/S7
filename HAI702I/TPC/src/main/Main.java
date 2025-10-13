package main;

import classifieur.Recognizer;
import classifieur.Rubine;
import geste.Lexique;

public class Main {

	public static void main(String[] args) {
		Lexique l = new Lexique();
		l.initData();
		Recognizer r = new Rubine();
		r.init(l);
		r.test(l);	
	}
}
