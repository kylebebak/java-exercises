import java.awt.Color;

/**
 * Particle that moves in a straight line and collides with walls and other
 * particles.
 * <p>
 * Particle is moving in a 1x1 square whose corners are at <b>(0, 0), (0, 1),
 * (1, 0) and (1, 1)</b>
 */
public class P4rticle {
	// 1337 spelling because "Particle" is a class in stdlib

	private static final double epsilon = .000000000001;
	// avoids problems with floating point arithmetic
	private double x;
	private double y;
	private double vx;
	private double vy;

	private double r;
	private double m;

	private int cc; // number of collisions particle has been in

	private Color color;

	public P4rticle(double x, double y, double vx, double vy, double r,
			double m, int R, int G, int B) {
		this.x = x;
		this.y = y;
		this.vx = vx;
		this.vy = vy;

		this.r = r;
		this.m = m;

		cc = 0;

		color = new Color(R, G, B);
	}

	/**
	 * Return the amount of time until particle collides with a vertical wall.
	 * If it never hits a wall, return -1
	 */
	public double collidesX() {
		if (vx > 0)
			return Math.max((1 - r - x) / vx, epsilon);
		if (vx < 0)
			return Math.max((r - x) / vx, epsilon);
		return -1;
	}

	/**
	 * Return the amount of time until particle collides with a horizontal wall.
	 * If it never hits a wall, return -1
	 */
	public double collidesY() {
		if (vy > 0)
			return Math.max((1 - r - y) / vy, epsilon);
		if (vy < 0)
			return Math.max((r - y) / vy, epsilon);
		return -1;
	}

	/**
	 * Return the amount of time until the invoking particle collides with
	 * particle b. If these particles never collide, return -1
	 */
	public double collides(P4rticle b) {
		if (this == b)
			return -1;

		double dx = b.x - x;
		double dy = b.y - y;
		double dvx = b.vx - vx;
		double dvy = b.vy - vy;

		double drdv = dx * dvx + dy * dvy;
		double drdr = dx * dx + dy * dy;
		double dvdv = dvx * dvx + dvy * dvy;

		if (drdv >= 0)
			return -1;

		double d = drdv * drdv - dvdv
				* (drdr - (r * r + 2 * r * b.r + b.r * b.r));
		if (d < 0)
			return -1;

		return Math.max(-(drdv + Math.sqrt(d)) / dvdv, epsilon);
	}

	/**
	 * Update the particle's velocity and collision counter to simulate it
	 * bouncing off a vertical wall
	 */
	public void bounceX() {
		vx = -vx;
		cc++;
	}

	/**
	 * Update the particle's velocity and collision counter to simulate it
	 * bouncing off a horizontal wall
	 */
	public void bounceY() {
		vy = -vy;
		cc++;
	}

	/**
	 * Update both particles' velocities and collision counters to simulate them
	 * bouncing off each other
	 */
	public void bounce(P4rticle b) {
		double dx = b.x - x;
		double dy = b.y - y;
		double drdv = dx * (b.vx - vx) + dy * (b.vy - vy);
		double sigma = r + b.r;

		double j = 2 * m * b.m * drdv / (sigma * (m + b.m));
		double jx = j * dx / sigma;
		double jy = j * dy / sigma;
		vx += jx / m;
		vy += jy / m;
		b.vx -= jx / b.m;
		b.vy -= jy / b.m;

		cc++;
		b.cc++;
	}

	/**
	 * Update particle's position for straight line motion during a time
	 * interval dt
	 */
	public void updatePosition(double dt) {
		x += vx * dt;
		y += vy * dt;
	}

	/**
	 * Draw the particle as a circle with radius "r"
	 */
	public void draw() {
		StdDraw.setPenColor(color);
		StdDraw.filledCircle(x, y, r);
	}

	/**
	 * Return the total number of collisions involving this particle
	 */
	public int getCollisionCount() {
		return cc;
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public double getR() {
		return r;
	}
}
