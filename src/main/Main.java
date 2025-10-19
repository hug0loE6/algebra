package main;

import classifieur.Rubine;
import geste.Geste;
import geste.Lexique;
import geste.Trace;

public class Main {

	public static void main(String s[]) {
		Lexique l = new Lexique();
		l.initData();
		System.out.println("Lexique size: "+l.size());
		System.out.println("First gesture name: "+l.get(0).getName());

		Rubine r = new Rubine();
		r.init(l);
		
		Geste g1 = l.get(7);
		Trace t = g1.get(2);
		Geste g2 = r.recognize(t);
		System.out.println("Trace recognized as gesture: "+g2.getName() + " (expected: "+g1.getName()+")");
		


		System.out.println(r.testLexique(l)*100 +"% des gestes on été reconnues");
	}
}
