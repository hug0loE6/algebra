package geste;

import java.lang.reflect.Array;
import java.util.ArrayList;

import algebre.Matrice;
import algebre.Vecteur;

public class Geste {
	private String nom;
	private ArrayList<Trace> traces; 
	private Trace modele;
	public Matrice covariance;
	private Vecteur esperance;
	private Vecteur weightVector;
	private double bias;
	
	public Geste(String nom, Trace model) {
		this.nom = nom;
		this.modele = model;
		this.traces = new ArrayList<Trace>();
	}
		
	public void init() {
		// initialisation des features de chaque trace
		for (Trace t : traces) {
			t.initFeatures();
		}	
		// calcul de l'espérance (vecteur moyen des features)
		esperance = new Vecteur(13);
		for (int i = 0; i < 13; i++) {
			double sum = 0;
			for (Trace t : traces) {
				sum += t.getFeatureVector().get(i);
			}
			esperance.set(i, sum/traces.size());
		}

		// calcul de la matrice de covariance
		ArrayList<Vecteur> echantillons = new ArrayList<Vecteur>();
		for (Trace t : traces) {
			echantillons.add(t.getFeatureVector());
		}
		covariance = Matrice.covariance(echantillons); 
	}

	public ArrayList<Trace> getTraces() {
		return this.traces;
	}

	public void addTrace(Trace t) {
		traces.add(t);	
	}

	public Trace get(int i) {
		return traces.get(i);
	}

	public void clear() {
		traces.clear();		
	}

	public String getName() {
		return nom;
	}

	public void initEstimators(Matrice inverseEotccm) {
		//todo
	}



}
