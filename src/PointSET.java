package KdTrees;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

public class PointSET {
	private Set<Point2D> setPoints;
	
	// construct an empty set of points
	public PointSET() {
		setPoints = new TreeSet<>();
	}
	
	//throws a java.lang.NullPointerException if any argument is null
	private void checkParams(Object o) {
		if (o == null)
			throw new NullPointerException();
	}
	
	// is the set empty?
	public boolean isEmpty(){
		return setPoints.isEmpty();
	}
	
	// number of points in the set 
	public int size() {
		return setPoints.size();
	}
	
	// add the point to the set (if it is not already in the set)
	public void insert(Point2D p){
		checkParams(p);
		setPoints.add(p);
	}
	
	// does the set contain point p?
	public boolean contains(Point2D p) {
		checkParams(p);
		return setPoints.contains(p);
	}
	
	// draw all points to standard draw 
	public void draw() {
		for (Point2D p: setPoints) {
			p.draw();
		}
	}
	
	// all points that are inside the rectangle 
	public Iterable<Point2D> range(final RectHV rect) {
		checkParams(rect);
		List<Point2D> list = new ArrayList<Point2D>();
		for (Point2D p: setPoints) {
			if (rect.contains(p)) {
				list.add(p);
			}
		}
		return list;
	}
	
	// a nearest neighbor in the set to point p; null if the set is empty
	public Point2D nearest(Point2D p) {
		checkParams(p);
		Point2D pointNearest = null;
		for (Point2D i: setPoints) {
			if (pointNearest == null) {
				pointNearest = i;
			}
			else {
				if (p.distanceTo(i) < p.distanceTo(pointNearest)) {
					pointNearest = i;
				}
			}
						
		}
		return pointNearest;
	}
	
	// unit testing of the methods (optional)
	public static void main(String[] args) {
		System.out.println("dsa".hashCode());
		String[] s = new String[]{"load", "lifo", "leaf", "link"};
		Arrays.sort(s);
		for (String ss: s) 
			System.out.println(ss);
	}
	
}
