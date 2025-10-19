package geste;

import java.util.ArrayList;

import algebre.Matrice;
import algebre.Vecteur;
import classifieur.Estimable;

public class Geste implements Estimable {
	private String nom;
	private ArrayList<Trace> traces; 
	private Trace modele;
	private Matrice covariance;
	private Vecteur esperance;
	private Vecteur weightVector;
	private double bias;
	
	public Geste(String nom, Trace model) {
		this.nom = nom;
		this.modele = model;
		this.traces = new ArrayList<Trace>();
	}
		
	public void init() {
		// initialisation des features de chaque tracé
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

		// calcul de la covariance
		ArrayList<Vecteur> echantillons = new ArrayList<Vecteur>();
		for (Trace t : traces) {
			echantillons.add(t.getFeatureVector());
		}
		covariance = Matrice.covariance(echantillons); 
	}

	public ArrayList<Trace> getTraces() {
		return this.traces;
	}
	
	public Vecteur getEsperance() {
		return this.esperance;
	}
	public Matrice getCovariance() {
		return this.covariance;
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
		weightVector = inverseEotccm.mult(esperance); //ak
		bias = -0.5 * weightVector.produitScalaire(esperance); //bk
	}

	public Matrice getCovMatrix() {
		return this.covariance;
	}


	public double getBias() {
		return this.bias;
	}

	public Vecteur getWeightVector() {
		return this.weightVector;
	}



}
