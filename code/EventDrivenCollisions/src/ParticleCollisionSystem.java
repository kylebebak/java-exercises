import java.util.PriorityQueue;

public class ParticleCollisionSystem {

	private static PriorityQueue<Event> pq = new PriorityQueue<Event>();
	// private static HashSet<ParticleKB> particles = new HashSet<ParticleKB>();
	private static P4rticle[] particles;
	private static double t = 0; // absolute time stored in event instances
	private static double dt; // change in time from previous processed event to
								// current event
	private static double delay; // dt between calls to redraw
	private static final int SIZE = 750; // canvas size in pixels

	static {
		initialize();
	}

	private static void initialize() {
		StdDraw.setCanvasSize(SIZE, SIZE);
		StdDraw.setXscale();
		StdDraw.setYscale();
		StdDraw.setPenRadius();
		StdDraw.show(0);
		pq.add(new Event(delay, null, null));
	}

	private static void redraw() {
		StdDraw.clear();
		for (P4rticle p : particles)
			p.draw();
		StdDraw.show(0);
		pq.add(new Event(t + delay, null, null));
	}

	/**
	 * Helper function updates particle positions after each event processed
	 */
	private static void updatePositions(double dt) {
		for (P4rticle p : particles)
			p.updatePosition(dt);
	}

	/**
	 * Helper function generates new events involving "P" after an event
	 * containing "P" is processed
	 */
	private static void generateEvents(P4rticle P) {
		double rt; // relative time returned by particle methods

		rt = P.collidesX();
		if (rt > 0)
			pq.add(new Event(t + rt, P, null));
		rt = P.collidesY();
		if (rt > 0)
			pq.add(new Event(t + rt, null, P));
		for (P4rticle p : particles) {
			rt = P.collides(p);
			if (rt > 0)
				pq.add(new Event(t + rt, P, p));
		}
	}

	/**
	 * This client of Particle and Event simulates particles in box, and runs as
	 * long as there are events in the queue
	 */
	public static void main(String[] args) {
		P4rticle a;
		P4rticle b;
		Event impending;
		int n = 0;
		
		// variables for diffusion simulation
		int nl = 0;
		int nr = 0;
		boolean display = false;

		// initialize particles array from standard input or from a call to
		// ParticleGenerator
		if (args.length == 1) {
			delay = Double.parseDouble(args[0]);
			n = Integer.parseInt(StdIn.readLine());
			particles = new P4rticle[n];

			int i = 0;
			String[] vals;
			while (!StdIn.isEmpty()) {
				vals = StdIn.readLine().trim().split("\\s+");

				particles[i++] = new P4rticle(Double.parseDouble(vals[0]),
						Double.parseDouble(vals[1]),
						Double.parseDouble(vals[2]),
						Double.parseDouble(vals[3]),
						Double.parseDouble(vals[4]),
						Double.parseDouble(vals[5]), Integer.parseInt(vals[6]),
						Integer.parseInt(vals[7]), Integer.parseInt(vals[8]));
			}

		} else if (args.length == 5) {
			delay = Double.parseDouble(args[0]);
			n = Integer.parseInt(args[1]);
			double v = Double.parseDouble(args[2]);
			double rMean = Double.parseDouble(args[3]);
			double rWidth = Double.parseDouble(args[4]);

			particles = ParticleGenerator.generateParticles(n, v, rMean,
					rWidth, 255, 0, 0);

		} else if (args.length == 12) {
			delay = Double.parseDouble(args[0]);
			int nw = Integer.parseInt(args[1]);
			int ns = Integer.parseInt(args[2]);
			nl = Integer.parseInt(args[3]);
			double vl = Double.parseDouble(args[4]);
			double rlMean = Double.parseDouble(args[5]);
			double rlWidth = Double.parseDouble(args[6]);
			nr = Integer.parseInt(args[7]);
			double vr = Double.parseDouble(args[8]);
			double rrMean = Double.parseDouble(args[9]);
			double rrWidth = Double.parseDouble(args[10]);
			boolean slit = Boolean.parseBoolean(args[11]);
			n = nw - ns + nl + nr; // total number of particles
			display = true; // flag for printing diffusion statistics to
							// standard output

			particles = ParticleGenerator.generateDiffusionExperiment(nw, ns,
					nl, vl, rlMean, rlWidth, nr, vr, rrMean, rrWidth, slit);
		}

		StdOut.println(n + " particles");
		// generate events for newly instantiated particles and begin
		for (P4rticle p : particles)
			generateEvents(p);

		while (true) {

			impending = pq.poll();
			// discard event without processing if it's no longer valid
			if (!impending.isStillValid())
				continue;

			a = impending.getParticleA();
			b = impending.getParticleB();

			dt = impending.getTime() - t;
			t = impending.getTime();
			// update positions before adding new events to queue
			updatePositions(dt);

			// redraw event if both particles are null
			if (a == null && b == null) {
				redraw();
				
				if (display) {
					int diffused = 0;
					for (int i = 0; i < nl; i++)
						if (particles[i].getX() > .5)
							diffused++;
					StdOut.println("L -- > R : " + diffused);
					
					diffused = 0;
					for (int i = nl; i < nl + nr; i++)
						if (particles[i].getX() < .5)
							diffused++;
					StdOut.println("R -- > L : " + diffused);
				}
				
				continue;
			}

			// A null means B collides with horizontal wall
			if (a == null) {
				b.bounceY();
				generateEvents(b);
				continue;
			}

			// B null means A collides with vertical wall
			if (b == null) {
				a.bounceX();
				generateEvents(a);
				continue;
			}

			// A and B collide with each other
			if (a != null && b != null) {
				a.bounce(b);
				generateEvents(a);
				generateEvents(b);
				continue;
			}
		}
	}
}
