import java.awt.Color;

public class Body {
	private double px, py; // position
	private double vx, vy; // velocity
	private double fx, fy; // force
	private double mass; // mass
	private Color color; // color

	private final double G = 6.67e-11; // gravitational constant
	private final double EPS = 3E4; // softening parameter

	/**
	 *  create and initialize a new Body
	 * @param px
	 * @param py
	 * @param vx
	 * @param vy
	 * @param mass
	 * @param color
	 */
	public Body(double px, double py, double vx, double vy, double mass,
			Color color) {
		this.px = px;
		this.py = py;
		this.vx = vx;
		this.vy = vy;
		this.mass = mass;
		this.color = color;
	}
	
	/**
	 * Copy constructor
	 */
	public Body(Body b) {
		px = b.px;
		py = b.py;
		vx = b.vx;
		vy = b.vy;
		mass = b.mass;
		color = b.color;
	}

	public boolean in(Quad q) {
		return q.contains(px, py);
	}

	/**
	 * return a new Body that represents the center-of-mass of the the invoking
	 * body and b, using the center-of-mass formula.
	 * <p>
	 * the velocity of this aggregate body is set arbitrarily, because the
	 * position of aggregate bodies is never updated in the simulation. rather,
	 * the BhTree is rebuilt and aggregate bodies are recomputed for each time
	 * quantum dt in the NBody simulation
	 */
	public Body plus(Body b) {
		double totalMass = mass + b.mass;
		return new Body((px * mass + b.px * b.mass) / totalMass,
				(py * mass + b.py * b.mass) / totalMass, 0, 0, totalMass, color);
	}

	/**
	 * update the velocity and position of body using leapfrog integration
	 */
	public void update(double dt) {
		vx += dt * fx / mass;
		vy += dt * fy / mass;
		px += dt * vx;
		py += dt * vy;
	}

	/**
	 * return the Euclidean distance between the invoking Body and b
	 */
	public double distanceTo(Body b) {
		double dx = px - b.px;
		double dy = py - b.py;
		return Math.sqrt(dx * dx + dy * dy);
	}

	/**
	 * reset the force of the invoking Body to 0
	 */
	public void resetForce() {
		fx = 0.0;
		fy = 0.0;
	}

	/**
	 * compute the net force acting between the invoking body a and b, and add
	 * to the net force acting on the invoking Body
	 */
	public void addForce(Body b) {
		Body a = this;
		double dx = b.px - a.px;
		double dy = b.py - a.py;
		double dist = Math.sqrt(dx * dx + dy * dy);
		double F = (G * a.mass * b.mass) / (dist * dist + EPS * EPS);
		a.fx += F * dx / dist;
		a.fy += F * dy / dist;
	}

	/**
	 * draw the invoking Body to standard draw
	 */
	public void draw() {
		StdDraw.setPenColor(color);
		StdDraw.point(px, py);
		StdDraw.circle(px, py, 20);
	}

	/**
	 * convert to string representation formatted nicely
	 */
	public String toString() {
		return String.format("%10.3E %10.3E %10.3E %10.3E %10.3E", px, py, vx,
				vy, mass);
	}

}
