public class RandomTour {

	/**
	 * Generate a tour with n random points, using the smallest increase
	 * heuristic or the nearest neighbor heuristic
	 */
	public static Tour random(int n, boolean smallest) {

		Point[] points = new Point[n];

		for (int i = 0; i < n; i++)
			points[i] = new Point(Math.random(), Math.random());

		Tour t = new Tour(points, smallest);

		return t;
	}

	/**
	 * This test client generates a Tour with n random points, where n is an
	 * integer string input from the command line
	 */
	public static void main(String[] args) {

		Tour t = random(Integer.parseInt(args[0]),
				Boolean.parseBoolean(args[1]));
		t.show();
		StdOut.println(t.distance());

		StdDraw.setPenColor(StdDraw.BLACK);

//		StdDraw.show(0); // if this line is edited out, the path is drawn frame by frame

		t.draw();
		StdDraw.show(0);
	}

}
