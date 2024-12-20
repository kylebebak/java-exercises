/*************************************************************************
 * Compilation: javac Brute.java Execution: java Brute input.txt Dependencies:
 * Point.java, In.java, StdDraw.java example run from terminal: java Brute
 * ../collinear/rs1423.txt
 * 
 *************************************************************************/

public class Brute {

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

		double slope01, slope02, slope03; // slopes between points

		for (int i = 0; i < N; i++) {

			for (int j = i + 1; j < N; j++) {

				slope01 = p[i].slopeTo(p[j]);
				if (p[i].compareTo(p[j]) == 0)
					continue;

				for (int k = j + 1; k < N; k++) {

					slope02 = p[i].slopeTo(p[k]);
					if (slope01 != slope02 || p[j].compareTo(p[k]) == 0)
						continue;

					for (int l = k + 1; l < N; l++) {

						slope03 = p[i].slopeTo(p[l]);
						if (slope01 != slope03 || p[k].compareTo(p[l]) == 0)
							continue;
						outputSegment(p[i], p[j], p[k], p[l]);

					}
				}
			}
		}

		// display to screen all at once
		StdDraw.show(0);

	}

}
