/*************************************************************************
 * Compilation: javac RangeSearchVisualizer.java Execution: java
 * RangeSearchVisualizer input.txt Dependencies: PointSET.java KdTree.java
 * Point2D.java RectHV.java StdDraw.java In.java
 * 
 * Read points from a file (specified as a command-line arugment) and draw to
 * standard draw. Also draw all of the points in the rectangle the user selects
 * by dragging the mouse.
 * 
 * The range search results using the brute-force algorithm are drawn in red;
 * the results using the kd-tree algorithms are drawn in blue.
 * 
 *************************************************************************/

public class RangeSearchVisualizerKdFast {

	public static void main(String[] args) {

		String filename = args[0];
		In in = new In(filename);
		
		// turn on animation mode 
		StdDraw.show(0);
		
		// initialize the data structures with N points from standard input
		PointSET brute = new PointSET();
		KdTree kdtree = new KdTree();
		while (!in.isEmpty()) {
			double x = in.readDouble();
			double y = in.readDouble();
			Point2D p = new Point2D(x, y);
			kdtree.insert(p);
			brute.insert(p);
		}

		double x0 = 0.0, y0 = 0.0; // initial endpoint of rectangle
		double x1 = 0.0, y1 = 0.0; // current location of mouse
		boolean isDragging = false; // is the user dragging a rectangle

		// draw the points
		StdDraw.clear();
		StdDraw.setPenColor(StdDraw.BLACK);
		StdDraw.setPenRadius(.01);
		brute.draw(false);
		
		// display points for the first time
		StdDraw.show(0);

		while (true) {

			RectHV rect = new RectHV(Math.min(x0, x1), Math.min(y0, y1),
					Math.max(x0, x1), Math.max(y0, y1));

			// user starts to drag a rectangle
			if (StdDraw.mousePressed() && !isDragging) {
				x0 = StdDraw.mouseX();
				y0 = StdDraw.mouseY();
				isDragging = true;
				continue;
			}

			// user is dragging a rectangle
			else if (StdDraw.mousePressed() && isDragging) {
				x1 = StdDraw.mouseX();
				y1 = StdDraw.mouseY();
				StdOut.println("here");
				// draw the rectangle
				StdDraw.setPenColor(StdDraw.BLACK);
				StdDraw.setPenRadius();
				rect.draw();
				StdDraw.show(0);
				continue;
			}

			// mouse no longer pressed
			else if (!StdDraw.mousePressed() && isDragging) {
				isDragging = false;

				// draw the points
				StdDraw.clear();
				StdDraw.setPenColor(StdDraw.BLACK);
				StdDraw.setPenRadius(.01);
				brute.draw(false);

				// draw the range search results for kd-tree in blue
				StdDraw.setPenRadius(.0035);
				StdDraw.setPenColor(StdDraw.RED);
				for (Point2D p : kdtree.range(rect))
					p.draw();
				
				// redraw points only when a new query rect is called
				StdDraw.show(0);
			}
		}
	}
}