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
		ArrayList<Geste> listGeste = l.getGestes();
		for (Geste g : listGeste) {
				g.init();		
		}
		ArrayList<Matrice> covariances = new ArrayList<Matrice>();
		for (Geste g : listGeste) {
			covariances.add(g.getCovariance());
		}
		eotccm = Matrice.esperance(covariances);
		inverseEotccm = eotccm.inverse();

		//Ajout de l'initialisaion des estimateurs
		for (Geste g : listGeste) {
			g.initEstimators(inverseEotccm);
		}
	}

	@Override
	//le lexique passé en paramètre doit être initialisé avant l'appel à test
	public double[] test(Lexique lexicon) {
		//todo
		return null; //a changer
	}



	@Override
	public Geste recognize(Trace t) {
		double max = Double.NEGATIVE_INFINITY;
		int indexMax = -1;
		for (int i = 0; i < lexicon.size(); i++) {
			Geste g = lexicon.get(i);
			double res = g.getWeightVector().produitScalaire(t.getFeatureVector()) + g.getBias();
			if (res > max) {
				max = res;
				indexMax = i;
			}
		}
		// Ici je ne comprend pas comment gérer le cas où aucun geste n'est reconnu car on prend toujours le max
		return lexicon.get(indexMax);
	}


	public double squaredMahalanobis(Vecteur t, Vecteur g) { // t = un tracé, g = espérence geste
		Vecteur diff = t.minus(g);
		int n = diff.getDimension();

		// On convertit les vecteur en tableau car la classe Matrice ne gère que les matrices carrées,
		// du coup on fait le produit matriciel sans utilser des méthodes
		double[] diffVec = new double[n];
		for (int i = 0; i < n; i++) {
			diffVec[i] = diff.get(i);
		}

		//res = (g - g_)T * M-1 
		double[] diffTimesInvEotccm = new double[n];
		for (int i = 0; i < n; i++) {
			diffTimesInvEotccm[i] = 0;
			for (int j = 0; j < n; j++) {
				diffTimesInvEotccm[i] += diffVec[i] * inverseEotccm.get(i, j);
			}
		}	

		// res * (g - g_)
		double resultatVec = 0;
		for (int i = 0; i < n; i++) {
			resultatVec += diffTimesInvEotccm[i] * diffVec[i];
		}
		
		// D(g)²
		return resultatVec;
	}

	@Override
	public double testGeste(Geste g) {
		Geste match = null;
		for (Geste gp : lexicon.getGestes()) {
			if (gp.getName().equals(g.getName())) {
				match = gp;
				break;
			}
		}
		if (match == null) {
			System.out.println("Geste " + g.getName() + " non trouvé dans le lexique");
			return -1;
		}
		else{
			int i = 0; //nb tracés reconnus
			int j = 0; //nb de tracés
			for (Trace t : g.getTraces()) {
				j++;
				double sqMaha = squaredMahalanobis(t.getFeatureVector(), match.getEsperance());
				if (!(sqMaha > 0.5*13)){
					i++;
				}
			}
			return (double)i / j;
		}
	}

	@Override
	public double testLexique(Lexique lexicon) {
		double i = 0; //somme des pourcentages de reconnaissance
		int j = 0; // nb de gestes
		for (Geste g : lexicon.getGestes()) {
			j++;
			i += testGeste(g);
		}
		return i/j; 
	}


}
