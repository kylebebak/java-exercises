import java.awt.Color;

public class Quad {

	private double x; // x center
	private double y; // y center
	private double l; // length of side
	private double hl;
	private double ql;
	private final Color color = StdDraw.GREEN;

	/**
	 * create a new quadrant centered at (xmid, ymid) of the given side length.
	 */
	public Quad(double xmid, double ymid, double length) {
		x = xmid;
		y = ymid;
		l = length;
		hl = l / 2.0;
		ql = hl / 2.0;
	}

	/**
	 * Return true if (x, y) is in the quadrant, and false otherwise.
	 */
	public boolean contains(double x, double y) {
		if (x <= this.x - hl)
			return false;
		if (x > this.x + hl)
			return false;
		if (y <= this.y - hl)
			return false;
		if (y > this.y + hl)
			return false;
		return true;
	}

	/**
	 * Returns the length of a side of the quadrant.
	 */
	public double length() {
		return l;
	}

	/**
	 * These four methods create and return a new Quad representing a
	 * sub-quadrant of the invoking quadrant.
	 */
	public Quad NW() {
		return new Quad(x - ql, y + ql, hl);
	}

	public Quad NE() {
		return new Quad(x + ql, y + ql, hl);
	}

	public Quad SW() {
		return new Quad(x - ql, y - ql, hl);
	}

	public Quad SE() {
		return new Quad(x + ql, y - ql, hl);
	}

	/**
	 * return string representation of the quad
	 */
	public String toString() {
		return new String("xmid : " + x + ", ymid : " + y + ", length : " + l);
	}

	/**
	 * draw the quad using StdDraw
	 */
	public void draw() {
		StdDraw.setPenColor(color);
		StdDraw.rectangle(x, y, hl, hl);
	}

}
