package main;

import classifieur.Rubine;
import geste.Geste;
import geste.Lexique;

public class Main {

	public static void main(String s[]) {
		Lexique l = new Lexique();
		l.initData();
		System.out.println("Lexique size: "+l.size());
		System.out.println("First gesture name: "+l.get(0).getName());

		Rubine r = new Rubine();
		r.init(l);
		
	}
}
