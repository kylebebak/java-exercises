import java.util.Arrays;

public class Fast {

	private static int N; // number of points, first line in input text file
	private static Point[] p;

	private static void outputSegment(Point p0, Point p1, Point p2, Point p3) {
		/*
		 * helper function that outputs segment as list of points to console,
		 * and also draws line segment on canvas. this function sorts p0 - p3
		 * (to achieve this the function itself calls another helper function,
		 * sortByPoint) so that it knows which points are lexicographically
		 * smallest and largest, and then will draw a line segment from the
		 * smallest to the the largest so that the line segment includes all 4
		 * points. this takes constant worst case time for each line segment
		 * drawn regardless of number of points N
		 */

		Point[] p = new Point[4];
		p[0] = p0;
		p[1] = p1;
		p[2] = p2;
		p[3] = p3;
		sortByPoint(p);

		p[0].drawTo(p[3]);
		StdOut.println(p[0].toString() + " -> " + p[1].toString() + " -> "
				+ p[2].toString() + " -> " + p[3].toString());

	}

	private static void sortByPoint(Point[] a) {
		/*
		 * insertion sort on array of Point objects using Point.compareTo as
		 * comparator
		 */
		int N = a.length;
		for (int i = 0; i < N; i++)
			for (int j = i; j > 0 && less(a[j], a[j - 1]); j--)
				exch(a, j, j - 1);
	}

	private static boolean less(Point v, Point w) {
		return v.compareTo(w) < 0;
	}

	private static void exch(Point[] a, int i, int j) {
		Point swap = a[i];
		a[i] = a[j];
		a[j] = swap;
	}

	public static void main(String[] args) {

		// rescale coordinates and turn on animation mode
		StdDraw.setXscale(0, 32768);
		StdDraw.setYscale(0, 32768);
		StdDraw.show(0);

		// read in the input
		String filename = args[0];
		In in = new In(filename);
		N = in.readInt();
		p = new Point[N]; // length N array with all of the input points

		// initialize Point objects from input and populate point array p
		for (int i = 0; i < N; i++) {
			int x = in.readInt();
			int y = in.readInt();
			Point pt = new Point(x, y);
			pt.draw();
			p[i] = pt;
		}
		
		Point[] pc;
		for (int i = 0; i < N; i++) {

			pc = p; // make a copy of p which will be sorted

			Arrays.sort(pc, i + 1, N, p[i].SLOPE_ORDER);

			for (int j = i + 1; j < N - 2; j++)
				if (p[i].slopeTo(p[j]) == p[i].slopeTo(p[j + 1]))
					if (p[i].slopeTo(p[j + 1]) == p[i].slopeTo(p[j + 2]))
						outputSegment(pc[i], pc[j], pc[j + 1], pc[j + 2]);
		}

		// display to screen all at once
		StdDraw.show(0);

	}
}
