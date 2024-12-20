import java.util.HashSet;

public class PointSET {
	private HashSet<Point2D> points;

	public PointSET() {
		// construct an empty set of points, using a java HashSet

		// points = new TreeSet<Point2D>(Point2D.X_ORDER);
		/*
		 * i could specify the comparator that the set uses to order the points
		 * by passing one of the various comparators provided by Point2D to the
		 * constructor of the set, but it might be forbidden to use this part of
		 * Point2D's API in the assignment, so here i'm not going to
		 */
		points = new HashSet<Point2D>();
	}

	public boolean isEmpty() {
		// is the set empty?
		return points.isEmpty();
	}

	public int size() {
		// number of points in the set
		return points.size();
	}

	public void insert(Point2D p) {
		// add the point p to the set (if it is not already in the set)
		points.add(p);
	}

	public boolean contains(Point2D p) {
		// does the set contain the point p?
		return points.contains(p);
	}

	public void draw(boolean reset) {
		// draw all of the points to standard draw
		if (reset) {
			StdDraw.clear();
			StdDraw.setPenColor(StdDraw.BLACK);
			StdDraw.setXscale(0, 1);
			StdDraw.setYscale(0, 1);
			StdDraw.setPenRadius();
		}
		for (Point2D point : points)
			point.draw();
	}

	public Iterable<Point2D> range(RectHV rect) {
		// all points in the set that are inside the rectangle
		ResizingArrayStack<Point2D> q = new ResizingArrayStack<Point2D>();

		for (Point2D point : points)
			if (rect.contains(point))
				q.push(point);
		return q;
	}

	public Point2D nearest(Point2D p) {
		// a nearest neighbor in the set to p; null if set is empty
		double minSquaredDist = Double.POSITIVE_INFINITY;
		Point2D nearest = null;
		for (Point2D point : points)
			if (p.distanceSquaredTo(point) < minSquaredDist) {
				minSquaredDist = p.distanceSquaredTo(point);
				nearest = point;
			}
		return nearest;
	}

}
