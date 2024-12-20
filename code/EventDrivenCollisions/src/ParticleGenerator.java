public class ParticleGenerator {

	private static final double epsilon = .000000000005;
	private static final double INFINITY = 10000000000.0; // wall particle
															// masses
	private static final int[] wRGB = { 75, 75, 75 }; // wall RGB values
	private static final int[] lRGB = { 255, 0, 0 }; // particles on left
	private static final int[] rRGB = { 0, 0, 255 }; // particles on right

	/**
	 * Generate an array of n <b>non-overlapping</b> circular particles in the
	 * square region bounded by (0, 0) and (1, 1)
	 */
	public static P4rticle[] generateParticles(int n, double v, double rMean,
			double rWidth, int R, int G, int B) {
		return generateParticlesInRect(0, 0, 1, 1, n, v, rMean, rWidth, R, G, B);
	}

	/**
	 * This function generates an array of <b>nl + nr + (nw - ns)</b> particles.
	 * The first nw - ns are immobile super massive particles that form a
	 * vertical wall nw circles high in the middle of the canvas. There are ns
	 * circles missing from this wall, allowing for diffusion to occur.
	 * <p>
	 * Then, nl particles are instantiated on the left side of the wall, and nr
	 * particles are instantiated on the right side of the wall. These particles
	 * are colored differently and are easy to observe as they diffuse from one
	 * side of the wall to the other.
	 * <p>
	 * This method calls both buildVerticalWall and generateParticlesInRect as
	 * helper methods. It guarantees that none of the moving particles or wall
	 * particles will have initial positions that overlap.
	 */
	public static P4rticle[] generateDiffusionExperiment(int nw, int ns,
			int nl, double vl, double rlMean, double rlWidth, int nr,
			double vr, double rrMean, double rrWidth, boolean slit) {

		// generate particles on left, particles on right, and wall particles
		P4rticle[] left = generateParticlesInRect(0, 0, .5 - .5 / (double) nw,
				1, nl, vl, rlMean, rlWidth, lRGB[0], lRGB[1], lRGB[2]);
		P4rticle[] right = generateParticlesInRect(.5 + .5 / (double) nw, 0, 1,
				1, nr, vr, rrMean, rrWidth, rRGB[0], rRGB[1], rRGB[2]);
		P4rticle[] wall;
		if (slit)
			wall = buildWallWithSlit(nw, ns);
		else
			wall = buildPorousWall(nw, ns);

		P4rticle[] experiment = new P4rticle[nl + nr + nw - ns];

		// copy particles from wall, left and right into experiment
		System.arraycopy(left, 0, experiment, 0, left.length);
		System.arraycopy(right, 0, experiment, left.length, right.length);
		System.arraycopy(wall, 0, experiment, left.length + right.length,
				wall.length);

		return experiment;
	}

	/**
	 * Parameters : nw is number of circles in wall, ns is number of circles
	 * missing from the middle of the wall that form the diffusion slit.
	 * <p>
	 * If ns is even, exclude from wall circles from indices <b>[nw / 2 - ns /
	 * 2, nw / 2 + ns / 2)</b>
	 * <p>
	 * If ns is odd, exclude from wall circles from indices <b>[nw / 2 - ns / 2,
	 * nw / 2 + ns / 2]</b>
	 */
	public static P4rticle[] buildWallWithSlit(int nw, int ns) {
		if (ns > nw)
			throw new IllegalArgumentException(
					"Number of particles removed from wall must not be greater than number of particles in wall");
		if (ns == nw)
			return new P4rticle[0];

		double spacing = 1.0 / (double) nw;
		double diameter = spacing - epsilon;
		// diameter slightly smaller than spacing so circles don't touch
		double[] y = new double[nw];
		y[0] = spacing / 2.0;
		for (int i = 1; i < nw; i++)
			y[i] = y[i - 1] + spacing;

		double[] wall = new double[nw - ns];
		// compute first half of wall's y coordinates
		int ml = nw / 2 - ns / 2;
		int mr = nw / 2 + ns / 2;
		int i;
		for (i = 0; i < ml; i++)
			wall[i] = y[i];

		// compute second half of wall's y coordinates
		if (ns % 2 == 0)
			wall[i++] = y[mr];
		for (int j = mr + 1; j < nw; i++, j++)
			wall[i] = y[j];

		P4rticle[] wallParticles = new P4rticle[nw - ns];
		for (i = 0; i < nw - ns; i++)
			wallParticles[i] = new P4rticle(.5, wall[i], 0, 0, diameter / 2.0,
					INFINITY, wRGB[0], wRGB[1], wRGB[2]);
		return wallParticles;
	}

	/**
	 * Parameters : nw is number of circles in wall, ns is number of circles
	 * missing at regular intervals from the wall (the wall is uniformly porous)
	 */
	public static P4rticle[] buildPorousWall(int nw, int ns) {
		if (ns > nw)
			throw new IllegalArgumentException(
					"Number of particles removed from wall must not be greater than number of particles in wall");
		if (ns == nw)
			return new P4rticle[0];

		double spacing = 1.0 / (double) nw;
		double diameter = spacing - epsilon;
		// diameter slightly smaller than spacing so circles don't touch
		double[] y = new double[nw];
		y[0] = spacing / 2.0;
		for (int i = 1; i < nw; i++)
			y[i] = y[i - 1] + spacing;

		int[] wallIndex = new int[nw - ns];
		double indexSpacing = (double) nw / (double) ns;

		int i = 0;
		for (int j = 0; j < nw; j++)
			if (Math.round(i * indexSpacing) == j)
				i++;
			else
				wallIndex[j - i] = j;

		P4rticle[] wallParticles = new P4rticle[nw - ns];
		for (int j = 0; j < nw - ns; j++)
			wallParticles[j] = new P4rticle(.5, y[wallIndex[j]], 0, 0,
					diameter / 2.0, INFINITY, wRGB[0], wRGB[1], wRGB[2]);
		return wallParticles;
	}

	/**
	 * Generate n <b>non-overlapping</b> circular particles in the rectangular
	 * region bounded by (rx0, ry0) and (rx1, ry1)
	 */
	private static P4rticle[] generateParticlesInRect(double rx0, double ry0,
			double rx1, double ry1, int n, double v, double rMean,
			double rWidth, int R, int G, int B) {
		P4rticle[] particles = new P4rticle[n];

		double x = 0;
		double y = 0;
		double r = 0;

		for (int i = 0; i < n; i++) {
			boolean fits = false;

			newparticle: // uses labeling instead of externalizing inner loop
			while (!fits) {

				r = Math.max(rMean / 2.0, StdRandom.gaussian(rMean, rWidth));
				x = StdRandom.uniform(rx0 + r, rx1 - r);
				y = StdRandom.uniform(ry0 + r, ry1 - r);

				for (int j = 0; j < i; j++) {
					if (overlap(x, y, r, particles[j].getX(),
							particles[j].getY(), particles[j].getR()))
						continue newparticle;
				}
				fits = true;
			}
			particles[i] = new P4rticle(x, y, StdRandom.uniform(-v, v),
					StdRandom.uniform(-v, v), r, r * r, R, G, B);
		}

		return particles;
	}

	/**
	 * Helper function determines whether input parameters belong to overlapping
	 * circles
	 */
	private static boolean overlap(double x1, double y1, double r1, double x2,
			double y2, double r2) {
		double dx = x2 - x1;
		double dy = y2 - y1;
		return Math.sqrt(dx * dx + dy * dy) <= r1 + r2;
	}
}
