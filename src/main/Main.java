package main;

import java.util.Random;


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
		
		Random rng = new Random();
		int z=0;
		int j=0;
		for(int i = 0; i < 2500; i++){
			j++;
			Geste g1 = l.get(rng.nextInt(l.getGestes().size()));
			Trace t = g1.get(rng.nextInt(g1.getTraces().size()));
			Geste g2 = r.recognize(t);
			if (g1.getName().equals(g2.getName())){
				z++;
			}
		}
		System.out.println((double) z/j);
		
		


		System.out.println(r.testLexique(l)*100 +"% des gestes on été reconnues");
	}
}
