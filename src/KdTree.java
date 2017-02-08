package KdTrees;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {
	private int size;
	private Node root;
	private double minDistance;
	
	// construct an empty BST of points 
	public KdTree() {
		this.root = null;
		this.size = 0;
	}
	
	
    //throws a java.lang.NullPointerException if any argument is null
	private void checkParams(Object o) {
		if (o == null)
			throw new NullPointerException();
	}
	
	// is the BST empty? 
	public boolean isEmpty() {
		return root == null;
	}
	
	// number of points in the BST 
	public int size() {
		return size;
	}
	
	//creates node in BST
	private Node createNode(Node parent, Point2D p, boolean isLeft) {
		Node newNode = new Node(p);
		if (parent == null) {
			newNode.isEven = true;
			newNode.rect = new RectHV(0, 0, 1, 1);
			size++;			
			return newNode;
		}
		newNode.isEven = !parent.isEven;
		if (isLeft) {
			parent.left = newNode;
			if (parent.isEven) {
				newNode.rect = new RectHV(parent.rect.xmin(), parent.rect.ymin(), 
						                  parent.point.x(), parent.rect.ymax());
			}
			else {
				newNode.rect = new RectHV(parent.rect.xmin(), parent.rect.ymin(), 
										  parent.rect.xmax(), parent.point.y());
			}
		}
		else {
			parent.right = newNode;
            if (parent.isEven) {
            	newNode.rect = new RectHV(parent.point.x(), parent.rect.ymin(), 
		                  parent.rect.xmax(), parent.rect.ymax());
			}
			else {
				newNode.rect = new RectHV(parent.rect.xmin(), parent.point.y(),
						                  parent.rect.xmax(), parent.rect.ymax());
			}
		}
		size++;
		return newNode;
	}
	
	//is to be inserted left?
	private boolean isLeftPass(Point2D p, Node n) {
		return (n.isEven == true && p.x() < n.point.x())
				|| (n.isEven == false && p.y() < n.point.y());
	}
	
	// add the point to the BST (if it is not already in the set)
	public void insert(Point2D p) {
		checkParams(p);
		if (contains(p)) return;
		Node x = root;
		boolean isLeftChild = false;
		if (isEmpty()) {
			root = createNode(null, p, isLeftChild);
			return;
		}
		while(x != null) {
			boolean isLeftPass = isLeftPass(p, x);
			if (x.point.equals(p)) return;
			if (x.left != null && isLeftPass) x = x.left;
			else if (x.left == null && isLeftPass) {
				isLeftChild = true;
				break;
			}
			else if (x.right != null && !isLeftPass) x = x.right;
			else if (x.right == null && !isLeftPass) {
				isLeftChild = false;
				break;
			}
		}
		createNode(x, p, isLeftChild);
	}
	
	// does the BST contain point p? 
	public boolean contains(Point2D p) {
		checkParams(p);
		Node x = root;
		while(x != null) {
			if (x.point.equals(p)) return true;
			if (isLeftPass(p, x)) x = x.left;
			else  x = x.right;
		}
		return false;
	}
	
	// draw all points to standard draw 
	public void draw() {
		draw(root);		
	}
	
	// draw all points recursively
	private void draw(Node x) {
		if (x == null) return;
		StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        x.point.draw();
        if (x.isEven) {
        	StdDraw.setPenColor(StdDraw.RED);
        	StdDraw.setPenRadius(0.001);
        	StdDraw.line(x.point.x(), x.rect.ymin(), x.point.x(), x.rect.ymax());
        }
        else{
        	StdDraw.setPenColor(StdDraw.BLUE);
        	StdDraw.setPenRadius(0.001);
        	StdDraw.line(x.rect.xmin(), x.point.y(), x.rect.xmax(), x.point.y());
        }
        draw(x.left);
        draw(x.right);
	}
	
	// all points that are inside the rectangle 
	public Iterable<Point2D> range(RectHV rect) {
		checkParams(rect);
		Queue<Point2D> q = new Queue<Point2D>();
	    intersections(root, q, rect);
	    return q;
	}
	
	//populates queue with nodes mutual rectangle intersections
	private void intersections(Node x, Queue<Point2D> q, RectHV rect) {
		if (x == null || !rect.intersects(x.rect)) return;
		intersections(x.left, q, rect);
		if (rect.contains(x.point))
			q.enqueue(x.point);
		intersections(x.right, q, rect);
	}
	
	// a nearest neighbor in the BST to point p; null if the BST is empty 
	public Point2D nearest(Point2D p) {
		checkParams(p);
		if (isEmpty()) return null;
		minDistance = root.point.distanceTo(p);
		return nearest(root, root.point, p); 
	}
	
	private Point2D nearest(Node x, Point2D champion, Point2D queryPoint) {
		if (x == null || x.rect.distanceTo(queryPoint) > minDistance) {
			return champion;
		}
		double newDistance = x.point.distanceTo(queryPoint);
		if (newDistance < minDistance) {
			minDistance = newDistance;
			champion = x.point;
		}
		if (isLeftPass(queryPoint, x) && x.left != null) {
			champion = nearest(x.left, champion, queryPoint);
			champion = nearest(x.right, champion, queryPoint);
		}
		else {
			champion = nearest(x.right, champion, queryPoint);
			champion = nearest(x.left, champion, queryPoint);
		}
		return champion;
	}
	
	public static void main(String[] args) {
		
	}
	
	//BST for point
    private static class Node {
    	private Point2D point;
    	private Node left;
    	private Node right;
    	private boolean isEven;
    	private RectHV rect;
    	
    	public Node(Point2D p) {
    		point = p;
    	}
    }
}