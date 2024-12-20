/*************************************************************************
 * Name: Kyle Bebak
 * Email: kylebebak@gmail.com
 *
 * Compilation:  javac Point.java
 * Execution:
 * Dependencies: StdDraw.java
 *
 * Description: An immutable data type for points in the plane.
 *
 *************************************************************************/

import java.util.Comparator;

public class Point implements Comparable<Point> {

	// compare points by slope
	public final Comparator<Point> SLOPE_ORDER = new SlopeOrder();

	private class SlopeOrder implements Comparator<Point> {
		public int compare(Point v, Point w) {
			double slope01 = slopeTo(v);
			double slope02 = slopeTo(w);
			if (slope01 < slope02)
				return -1;
			if (slope01 == slope02)
				return 0;
			else
				return 1;
		}
	}

	private final int x; // x coordinate
	private final int y; // y coordinate

	// create the point (x, y)
	public Point(int x, int y) {
		/* DO NOT MODIFY */
		this.x = x;
		this.y = y;
	}

	// plot this point to standard drawing
	public void draw() {
		/* DO NOT MODIFY */
		StdDraw.point(x, y);
	}

	// draw line between this point and that point to standard drawing
	public void drawTo(Point that) {
		/* DO NOT MODIFY */
		StdDraw.line(this.x, this.y, that.x, that.y);
	}

	// slope between this point and that point
	public double slopeTo(Point that) {
		/*
		 * return -inf if points are the same, return +inf if they are part of a
		 * vertical line segment, return _positive zero_ if they are part of a
		 * horizontal line segment
		 */
		if (this.compareTo(that) == 0)
			return Double.NEGATIVE_INFINITY;
		if (this.y == that.y)
			return 0.0; // positive zero
		if (this.x == that.x)
			return Double.POSITIVE_INFINITY;
		return ((double) that.y - (double) this.y)
				/ ((double) that.x - (double) this.x);
	}

	// is this point lexicographically smaller than that point?
	// comparing y-coordinates and breaking ties by x-coordinates
	public int compareTo(Point that) {
		/*
		 * return -1 if this point is smaller than that point, 0 if they're the
		 * same point, and +1 if this point is bigger than that point
		 */

		if (this.y == that.y)
			return (this.x - that.x);
		else
			return this.y - that.y;

	}

	// return string representation of this point
	public String toString() {
		/* DO NOT MODIFY */
		return "(" + x + ", " + y + ")";
	}

	// unit test
	public static void main(String[] args) {
		/* YOUR CODE HERE */
	}
}