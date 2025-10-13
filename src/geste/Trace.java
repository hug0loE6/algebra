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
		//f1
		features.set(0, (points.get(2).getX() - points.get(0).getX())/(points.get(0).distance(points.get(2))));
		//f2
		features.set(1, (points.get(2).getY() - points.get(0).getY())/(points.get(0).distance(points.get(2))));
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
		features.set(3, Math.atan((double)(max.getY() - min.getY())/(max.getX() - min.getX())));
		//f5
		features.set(4, points.get(0).distance(points.get(points.size()-1)));
		//f6
		features.set(5, (points.get(points.size()-1).getX() - points.get(0).getX())/features.get(4));
		//f7
		features.set(6, (points.get(points.size()-1).getY() - points.get(0).getY())/features.get(4));
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
		//teta p
		double teta[] = new double[points.size()-2];
		for (int i = 1; i < points.size()-1; i++) {
			teta[i] = Math.atan((double) 
			(deltapx[i]*deltapy[i-1] - deltapx[i-1]*deltapy[i]) /
			(deltapx[i]*deltapx[i-1] + deltapy[i]*deltapy[i-1]));
		}
		//f9
		double sumteta = 0;
		for (int i = 1; i < points.size()-1; i++) {
			sumteta += teta[i];
		}
		features.set(8, sumteta);
		//f10
		double sumabsteta = 0;
		for (int i = 1; i < points.size()-1; i++) {
			sumabsteta += Math.abs(teta[i]);
		}
		features.set(9, sumabsteta);
		//f11
		double squaresumteta = 0;
		for (int i = 1; i < points.size()-1; i++) {
			squaresumteta += teta[i]*teta[i];
		}
		features.set(10, squaresumteta);
		//delta timestamp
		long deltaT[] = new long[points.size()-1];
		for (int i = 0; i < points.size()-1; i++) {
			deltaT[i] = points.get(i+1).getTimeStamp() - points.get(i).getTimeStamp();
		}
		//f12
		double maxSpeed = 0;
		for (int i = 0; i < points.size()-1; i++) {
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
