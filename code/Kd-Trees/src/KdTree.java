public class KdTree {

	private int N = 0;
	private Node root;

	private ResizingArrayStack<Point2D> q = new ResizingArrayStack<Point2D>();
	// global variable for range search

	private double minSquaredDist; // global variables for nearest neighbor
									// search
	private Point2D nearest; // global variables for nearest neighbor search

	private static class Node {
		private Point2D p; // the point
		private RectHV rect; // the axis-aligned rectangle corresponding to this
								// node
		private Node lb; // the left/bottom subtree
		private Node rt; // the right/top subtree

		private Node(Point2D p, RectHV rect) {
			this.p = p;
			this.rect = rect;
		}
	}

	public KdTree() {
		// constructor method required as part of API, but nothing
		// needs to be initialized with it
	}

	public boolean isEmpty() {
		// is the set empty?
		return (N == 0);
	}

	public int size() {
		// number of points in the set
		return N;
	}

	/***********************************************************************
	 * insertion, implemented with recursive helper function to keep API clean.
	 * public insert only accepts a Point2D as argument
	 ***********************************************************************/
	public void insert(Point2D p) {
		// add the point p to the set (if it is not already in the set)
		root = insert(root, p, new RectHV(0, 0, 1, 1), 0);
	}

	private Node insert(Node x, Point2D p, RectHV rect, int level) {
		if (x == null) {
			N++; // increment N only when a new node is added to the set
			return new Node(p, rect);
		}
		if (p.equals(x.p))
			return x;
		// if point is already in set, don't add it (no duplicates)

		RectHV newRect;
		int cmp;
		if (level % 2 == 0) {
			if (p.x() < x.p.x()) {
				cmp = -1;
				newRect = new RectHV(rect.xmin(), rect.ymin(), x.p.x(),
						rect.ymax());
			} else {
				cmp = 1;
				newRect = new RectHV(x.p.x(), rect.ymin(), rect.xmax(),
						rect.ymax());
			}
		} else {
			if (p.y() < x.p.y()) {
				cmp = -1;
				newRect = new RectHV(rect.xmin(), rect.ymin(), rect.xmax(),
						x.p.y());
			} else {
				cmp = 1;
				newRect = new RectHV(rect.xmin(), x.p.y(), rect.xmax(),
						rect.ymax());
			}
		}
		if (cmp < 0)
			x.lb = insert(x.lb, p, newRect, level + 1);
		else
			x.rt = insert(x.rt, p, newRect, level + 1);

		return x;
	}

	/***********************************************************************
	 * contains, implemented with recursive helper function to keep API clean.
	 * public contains only accepts a Point2D as argument
	 ***********************************************************************/
	public boolean contains(Point2D p) {
		// does the set contain the point p?
		return contains(root, p, 0);
	}

	private boolean contains(Node x, Point2D p, int level) {
		if (x == null)
			return false;
		if (p.equals(x.p))
			return true;

		int cmp = 0;
		if (level % 2 == 0) {
			if (p.x() < x.p.x())
				cmp = -1;
			else
				cmp = 1;
		} else {
			if (p.y() < x.p.y())
				cmp = -1;
			else
				cmp = 1;
		}
		if (cmp < 0)
			return contains(x.lb, p, level + 1);
		else
			return contains(x.rt, p, level + 1);
	}

	/***********************************************************************
	 * draw, range, and nearest, implemented with recursive helper functions to
	 * keep API clean
	 ***********************************************************************/
	public void draw() {
		// draw all of the points to standard draw
		StdDraw.clear();
		StdDraw.setXscale(0, 1);
		StdDraw.setYscale(0, 1);
		draw(root, 0);
	}

	private void draw(Node x, int level) {
		if (x != null) {
			StdDraw.setPenColor(StdDraw.BLACK);
			StdDraw.setPenRadius(.01);
			StdDraw.point(x.p.x(), x.p.y());
			StdDraw.setPenRadius();
			if (level % 2 == 0) {
				StdDraw.setPenColor(StdDraw.RED);
				StdDraw.line(x.p.x(), x.rect.ymin(), x.p.x(), x.rect.ymax());
			} else {
				StdDraw.setPenColor(StdDraw.BLUE);
				StdDraw.line(x.rect.xmin(), x.p.y(), x.rect.xmax(), x.p.y());
			}
			draw(x.lb, level + 1);
			draw(x.rt, level + 1);
		}
	}

	public Iterable<Point2D> range(RectHV rect) {
		// all points in the set that are inside the rectangle
		q = new ResizingArrayStack<Point2D>();

		range(root, rect);
		return q;
	}

	private void range(Node x, RectHV query) {
		if (x != null) {
			if (query.intersects(x.rect)) {
				if (query.contains(x.p))
					q.push(x.p);
				range(x.lb, query);
				range(x.rt, query);
			}
		}
	}

	public Point2D nearest(Point2D p) {
		// a nearest neighbor in the set to p; null if set is empty
		minSquaredDist = Double.POSITIVE_INFINITY;
		nearest = null;

		nearest(root, p, 0);
		return nearest;
	}

	private void nearest(Node x, Point2D query, int level) {
		if (x != null) {
			if (x.rect.distanceSquaredTo(query) < minSquaredDist) {

				if (query.distanceSquaredTo(x.p) < minSquaredDist) {
					minSquaredDist = query.distanceSquaredTo(x.p);
					nearest = x.p;
				}

				if (x.lb != null && x.rt != null) {
					if (level % 2 == 0) {
						if (x.p.x() > query.x()) {
							nearest(x.lb, query, level + 1);
							nearest(x.rt, query, level + 1);
						} else {
							nearest(x.rt, query, level + 1);
							nearest(x.lb, query, level + 1);
						}
					} else {
						if (x.p.y() > query.y()) {
							nearest(x.lb, query, level + 1);
							nearest(x.rt, query, level + 1);
						} else {
							nearest(x.rt, query, level + 1);
							nearest(x.lb, query, level + 1);
						}
					}
				} else {
					if (x.lb != null)
						nearest(x.lb, query, level + 1);
					if (x.rt != null)
						nearest(x.rt, query, level + 1);
				}

			}
		}
	}
}