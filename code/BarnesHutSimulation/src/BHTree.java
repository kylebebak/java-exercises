public class BHTree {

	private Body body; // body or aggregate body stored in this node
	private Quad quad; // square region that the tree represents
	private BHTree NW; // tree representing northwest quadrant
	private BHTree NE; // tree representing northeast quadrant
	private BHTree SW; // tree representing southwest quadrant
	private BHTree SE; // tree representing southeast quadrant
	private final double theta = .5;

	/**
	 * create a Barnes-Hut tree with no bodies, representing the given quadrant.
	 */
	public BHTree(Quad q) {
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

		NW = new BHTree(quad.NW());
		NE = new BHTree(quad.NE());
		SW = new BHTree(quad.SW());
		SE = new BHTree(quad.SE());

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
		StringBuilder sb = new StringBuilder();
		toString(sb);
		return sb.toString();
	}
	
	private void toString(StringBuilder sb) {
		
		if (!isExternal()) {
			NW.toString(sb);
			NE.toString(sb);
			SW.toString(sb);
			SE.toString(sb);
		}
		
		if (isExternal())
			if (body != null)
				sb.append(body.toString() + "\n");
	}

	/**
	 * draw the Barnes-Hut tree using StdDraw
	 */	
	public void draw() {
	if (body == null)
		return;
	
	if (!isExternal()) {
		NW.draw();
		NE.draw();
		SW.draw();
		SE.draw();
	}
	
	if (isExternal())
		quad.draw();
}

}
