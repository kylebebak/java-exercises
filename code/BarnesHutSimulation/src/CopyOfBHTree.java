public class CopyOfBHTree {

	private Body body; // body or aggregate body stored in this node
	private Quad quad; // square region that the tree represents
	private CopyOfBHTree NW; // tree representing northwest quadrant
	private CopyOfBHTree NE; // tree representing northeast quadrant
	private CopyOfBHTree SW; // tree representing southwest quadrant
	private CopyOfBHTree SE; // tree representing southeast quadrant
	private final double theta = .5;

	/**
	 * create a Barnes-Hut tree with no bodies, representing the given quadrant.
	 */
	public CopyOfBHTree(Quad q) {
		quad = q;
	}

	/**
	 * add the body b to the invoking Barnes-Hut tree.
	 */
	public void insert(Body b) {
		// if node doesn't contain a body, put the new body b here
		if (body == null) {
			body = b;
			return;
		}

		/*
		 * if node is an internal node, update the CM and total mass of node.
		 * Recursively insert the body b in the appropriate quadrant
		 */
		if (!isExternal()) {
			body = body.plus(b);

			if (b.in(NW.quad))
				NW.insert(b);
			else if (b.in(NE.quad))
				NE.insert(b);
			else if (b.in(SW.quad))
				SW.insert(b);
			else if (b.in(SE.quad))
				SE.insert(b);
			return;
		}

		/*
		 * If node is an external node containing a body then you have two
		 * bodies in the same region. Subdivide the region by creating four
		 * children. Then, recursively insert bodies into the appropriate
		 * quadrant(s). Since bodies may still end up in the same quadrant,
		 * there may be several subdivisions during insertion. Finally, update
		 * the CM and total mass of node
		 */

		NW = new CopyOfBHTree(quad.NW());
		NE = new CopyOfBHTree(quad.NE());
		SW = new CopyOfBHTree(quad.SW());
		SE = new CopyOfBHTree(quad.SE());

		if (body.in(NW.quad))
			NW.insert(body);
		else if (body.in(NE.quad))
			NE.insert(body);
		else if (body.in(SW.quad))
			SW.insert(body);
		else if (body.in(SE.quad))
			SE.insert(body);

		if (b.in(NW.quad))
			NW.insert(b);
		else if (b.in(NE.quad))
			NE.insert(b);
		else if (b.in(SW.quad))
			SW.insert(b);
		else if (b.in(SE.quad))
			SE.insert(b);

		body = body.plus(b);
	}

	/**
	 * approximate the net force acting on body b from all the bodies in the
	 * invoking Barnes-Hut tree, and update b's force accordingly
	 */
	public void updateForce(Body b) {

		if (body == null)
			return;

		// if node is external, compute force induced on b by node's body
		if (isExternal()) {
			if (body != b)
				b.addForce(body);
			return;
		}

		// if b is sufficiently far away from this quadrant, approximate all
		// bodies inside quadrant as one body
		if (quad.length() / b.distanceTo(body) < theta) {
			b.addForce(body);
			return;
		}

		// if b is too close to quadrant, recursively update force on b by
		// calculating forces induced by 4 sub-quadrants
		NW.updateForce(b);
		NE.updateForce(b);
		SW.updateForce(b);
		SE.updateForce(b);
	}

	/**
	 * Does this BHTree represent an internal node?
	 */
	private boolean isExternal() {
		if (NW == null)
			return true;
		// any BHTree has either 4 instantiated BHTree children or none at all
		return false;
	}

	/**
	 * return string representation of the Barnes-Hut tree.
	 */
	public String toString() {
		return null;
	}

	/**
	 * draw the Barnes-Hut tree using StdDraw
	 */
	public static void draw(CopyOfBHTree bht) {
		if (bht.body == null)
			return;
		
		if (!bht.isExternal()) {
			draw(bht.NW);
			draw(bht.NE);
			draw(bht.SW);
			draw(bht.SE);
		}
		
		if (bht.isExternal())
			bht.quad.draw();
	}

}
