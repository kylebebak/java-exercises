/*************************************************************************
 * Compilation: javac NearestNeighborVisualizer.java Execution: java
 * NearestNeighborVisualizer input.txt Dependencies: PointSET.java KdTree.java
 * Point2D.java In.java StdDraw.java
 * 
 * Read points from a file (specified as a command-line argument) and draw to
 * standard draw. Highlight the closest point to the mouse.
 * 
 * The nearest neighbor according to the brute-force algorithm is drawn in red;
 * the nearest neighbor using the kd-tree algorithm is drawn in blue.
 * 
 *************************************************************************/

public class NearestNeighborVisualizerKdFast {
	
	private static final double POINT_SIZE = .011;
	private static final double QUERY_SIZE = .01;

	public static void main(String[] args) {
		String filename = args[0];
		In in = new In(filename);
		
		StdDraw.setCanvasSize(1300, 750);
		StdDraw.show(0);

		// initialize the two data structures with point from standard input
		PointSET brute = new PointSET();
		KdTree kdtree = new KdTree();
		while (!in.isEmpty()) {
			double x = in.readDouble();
			double y = in.readDouble();
			Point2D p = new Point2D(x, y);
			kdtree.insert(p);
			brute.insert(p);
		}

		// draw point set for the first time
		StdDraw.setPenColor(StdDraw.BLACK);
		StdDraw.setPenRadius(POINT_SIZE);
		brute.draw(false);

		Point2D nearest = new Point2D(Double.NEGATIVE_INFINITY,
				Double.NEGATIVE_INFINITY);
		while (true) {

			// the location (x, y) of the mouse
			double x = StdDraw.mouseX();
			double y = StdDraw.mouseY();
			Point2D query = new Point2D(x, y);

			// redraw only the point that was colored blue in the last frame
			StdDraw.setPenColor(StdDraw.BLACK);
			StdDraw.setPenRadius(POINT_SIZE);
			nearest.draw();

			// draw in blue the nearest neighbor (using kd-tree algorithm)
			StdDraw.setPenColor(StdDraw.RED);
			StdDraw.setPenRadius(QUERY_SIZE);
			nearest = kdtree.nearest(query);
			nearest.draw();
			StdDraw.show(0);
		}
	}
}