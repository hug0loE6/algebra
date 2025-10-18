package classifieur;

import java.util.ArrayList;

import algebre.Matrice;
import algebre.Vecteur;
import geste.Geste;
import geste.Lexique;
import geste.Trace;

public class Rubine implements Recognizer{
	private Lexique lexicon;
	private Matrice eotccm; // estimate of the common covariance matrix
	private Matrice inverseEotccm; // inverse of eotccm
	
	
	public Rubine() {		
	}

	public void init(Lexique l) {
		// init du lexique et calcul matrice covariance commune et son inverse
		lexicon = l;
		ArrayList<Geste> a = l.getGestes();
		for (Geste g : a) {
			g.init();
		}
		ArrayList<Matrice> covariances = new ArrayList<Matrice>();
		for (Geste g : a) {
			covariances.add(g.getCovariance());
		}
		eotccm = Matrice.esperance(covariances);
		inverseEotccm = eotccm.inverse();
	}

	@Override
	//le lexique passé en paramètre doit être initialisé avant l'appel à test
	public double[] test(Lexique lexicon) {
		//todo
		return null; //a changer
	}



	@Override
	public Geste recognize(Trace t) {
		//todo
		return null; //a changer...
	}


	public double squaredMahalanobis(Vecteur t, Vecteur g) {
		//todo
		return 0;// a changer
	}

	@Override
	public double testGeste(Geste g) {
		//todo
		return 0; //a changer
	}

	@Override
	public void testLexique(Lexique lexicon) {
		//todo
	}


}
