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
		//todo
	}


	public Vecteur getFeatureVector() {
		return new Vecteur(features);
	}

	public int size() {
		return points.size();
	}

}
