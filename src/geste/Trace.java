package geste;
import java.awt.Point;
import java.io.File;
import java.util.ArrayList;

import algebre.Vecteur;
import classifieur.Featured;
import io.ReadWritePoint;

public class Trace implements Featured{
	private ArrayList<StampedCoord> points;
	private Vecteur features;
	boolean model;

	public Trace(boolean model) {
		points = new ArrayList<StampedCoord>();
		this.model = model;
	}

	public Trace(boolean model, String fileName) {
		this(model);
		File f = new File(fileName);
		ReadWritePoint rwp = new ReadWritePoint(f);
		points = rwp.read();
	}

	public void add(Point p, long timeStamp) {
		add(new StampedCoord(p.x, p.y, timeStamp));
	}

	public void add(StampedCoord p) {
		points.add(p);
	}


	
	public void initFeatures() {
		features = new Vecteur(13);
		double distance = points.get(0).distance(points.get(2));
		//f1
		if (distance == 0) {
			features.set(0, 0);
		}
		else{
			features.set(0, (points.get(2).getX() - points.get(0).getX())/(distance));
		}
		//f2
		if (distance == 0) {
			features.set(1, 0);
		}
		else{
			features.set(1, (points.get(2).getY() - points.get(0).getY())/(distance));
		}
		//calcule bounding box
		int xmin = points.get(0).getX(), xmax = points.get(0).getX();
		int ymin = points.get(0).getY(), ymax = points.get(0).getY();
		for (int i = 1; i < points.size(); i++) {
			if (points.get(i).getX() < xmin) xmin = points.get(i).getX();
			if (points.get(i).getX() > xmax) xmax = points.get(i).getX();
			if (points.get(i).getY() < ymin) ymin = points.get(i).getY();
			if (points.get(i).getY() > ymax) ymax = points.get(i).getY();
		}
		StampedCoord max = new StampedCoord(xmax, ymax);
		StampedCoord min = new StampedCoord(xmin, ymin);
		//f3
		features.set(2, max.distance(min));
		//f4
		double denominateur = max.getX() - min.getX();
		double numerateur = max.getY() - min.getY();
		if (denominateur == 0) {
				if (numerateur > 0) features.set(3, Math.PI/2);
				else if (numerateur < 0) features.set(3, -Math.PI/2);
				else features.set(3, 0);
		}
		else {
		features.set(3, Math.atan((double)(numerateur)/(denominateur)));
		}
		//f5
		features.set(4, points.get(0).distance(points.get(points.size()-1)));
		//f6
		if (features.get(4) == 0) {
			features.set(5, 0);
		}
		else{
			features.set(5, (points.get(points.size()-1).getX() - points.get(0).getX())/features.get(4));
		}
		//f7
		if (features.get(4) == 0) {
			features.set(6, 0);
		}
		else{
			features.set(6, (points.get(points.size()-1).getY() - points.get(0).getY())/features.get(4));
		}
		//delta x et delta y
		int deltapx[] = new int[points.size()-1];
		int deltapy[] = new int[points.size()-1];
		for (int i = 0; i < points.size()-1; i++) {
			deltapx[i] = points.get(i+1).getX() - points.get(i).getX();
			deltapy[i] = points.get(i+1).getY() - points.get(i).getY();
		}
		//f8
		int sumx = 0;
		for (int i = 0; i < points.size()-1; i++){
			sumx += Math.sqrt(deltapx[i]*deltapx[i] + deltapy[i]*deltapy[i]);
		}
		features.set(7, sumx);
		//theta p
		double theta[] = new double[points.size()-1];
		for (int i = 1; i < points.size()-1; i++) {
			denominateur = deltapx[i]*deltapx[i-1] + deltapy[i]*deltapy[i-1];
			numerateur = deltapx[i]*deltapy[i-1] - deltapx[i-1]*deltapy[i];
			if (denominateur == 0) {
				if (numerateur > 0) theta[i] = Math.PI/2;
				else if (numerateur < 0) theta[i] = -Math.PI/2;
				else theta[i] = 0;
				continue;
			}
			else {
			theta[i] = Math.atan((double) (numerateur) / (denominateur));
			}
		}
		//f9
		double sumtheta = 0;
		for (int i = 1; i < points.size()-1; i++) {
			sumtheta += theta[i];
		}
		features.set(8, sumtheta);
		//f10
		double sumabstheta = 0;
		for (int i = 1; i < points.size()-1; i++) {
			sumabstheta += Math.abs(theta[i]);
		}
		features.set(9, sumabstheta);
		//f11
		double squaresumtheta = 0;
		for (int i = 1; i < points.size()-1; i++) {
			squaresumtheta += theta[i]*theta[i];
		}
		features.set(10, squaresumtheta);
		//delta timestamp
		long deltaT[] = new long[points.size()-1];
		for (int i = 0; i < points.size()-1; i++) {
			deltaT[i] = points.get(i+1).getTimeStamp() - points.get(i).getTimeStamp();
		}
		//f12
		double maxSpeed = 0;
		for (int i = 0; i < points.size()-1; i++) {
			if (deltaT[i] == 0) continue;
			double res = (double) ((deltapx[i]*deltapx[i] + deltapy[i]*deltapy[i]) / deltaT[i]*deltaT[i]);
			if (res > maxSpeed) maxSpeed = res;
		}
		
		features.set(11, maxSpeed);
		//f13
		features.set(12, points.get(points.size()-1).getTimeStamp() - points.get(0).getTimeStamp());
	}


	public Vecteur getFeatureVector() {
		return new Vecteur(features);
	}

	public int size() {
		return points.size();
	}

}
