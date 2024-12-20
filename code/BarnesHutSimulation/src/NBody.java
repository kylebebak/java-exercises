import java.awt.Color;

public class NBody {

	public static void main(String[] args) {
		final double dt = .1; // time quantum
		int N = StdIn.readInt(); // number of particles
		double radius = StdIn.readDouble(); // radius of universe
		final double radiusMultiplier = 2.0; // make universe larger so bodies don't
										// disappear from BHTree

		// turn on animation mode and rescale coordinate system
		StdDraw.show(0);
		StdDraw.setXscale(-radius, +radius);
		StdDraw.setYscale(-radius, +radius);

		// read in and initialize bodies
		Body[] bodies = new Body[N]; // array of N bodies
		for (int i = 0; i < N; i++) {
			double px = StdIn.readDouble();
			double py = StdIn.readDouble();
			double vx = StdIn.readDouble();
			double vy = StdIn.readDouble();
			double mass = StdIn.readDouble();
			int red = StdIn.readInt();
			int green = StdIn.readInt();
			int blue = StdIn.readInt();
			Color color = new Color(red, green, blue);
			bodies[i] = new Body(px, py, vx, vy, mass, color);
		}
		
		// simulate the universe
		for (double t = 0.0; true; t = t + dt) {
			
			// root of barnes-hut tree
			BHTree bht = new BHTree(new Quad(0, 0, 2 * radiusMultiplier * radius));

			// reset forces and build barnes-hut tree
			for (int i = 0; i < N; i++) {
				bodies[i].resetForce();
				bht.insert(bodies[i]);
			}
			
			if (t < dt)
				StdOut.println(bht.toString());

			// update forces after tree is built, requires a second pass
			for (int i = 0; i < N; i++)
				bht.updateForce(bodies[i]);

			// update body position and velocity, third pass
			for (int i = 0; i < N; i++)
				bodies[i].update(dt);

			// draw the bodies
			StdDraw.clear(StdDraw.BLACK);
			for (int i = 0; i < N; i++)
				bodies[i].draw();
//			bht.draw();
			StdDraw.show(10);

		}
	}
}
