public class SAP {
	// shortest ancestral path
	private Digraph G;

	// constructor takes a digraph (not necessarily a DAG)
	public SAP(Digraph G) {
		// use copy constructor to make sure SAP doesn't alter the digraph
		// G that is passed to it
		this.G = new Digraph(G);
	}

	/**
	 * length of shortest ancestral path between v and w; -1 if no such path
	 */
	public int length(int v, int w) {
		checkVertex(v);
		checkVertex(w);
		return sap(v, w)[0];
	}

	// a common ancestor of v and w that participates in a shortest ancestral
	// path; -1 if no such path
	public int ancestor(int v, int w) {
		checkVertex(v);
		checkVertex(w);
		return sap(v, w)[1];
	}

	// helper function for finding shortest ancestral path
	private int[] sap(int v, int w) {
		int minTotalDistance = Integer.MAX_VALUE;
		int newTotalDistance;
		int commonAncestor = -1;
		BreadthFirstDirectedPaths bfs_v = new BreadthFirstDirectedPaths(G, v);
		BreadthFirstDirectedPaths bfs_w = new BreadthFirstDirectedPaths(G, w);
		for (int s = 0; s < G.V(); s++) {
			newTotalDistance = bfs_v.distTo(s) + bfs_w.distTo(s);
			if (newTotalDistance >= 0 && newTotalDistance < minTotalDistance) {
				minTotalDistance = newTotalDistance;
				commonAncestor = s;
			}
		}
		if (minTotalDistance == Integer.MAX_VALUE)
			minTotalDistance = -1;
		return new int[] { minTotalDistance, commonAncestor };
	}

	/**
	 * length of shortest ancestral path found between a vertex from a set of
	 * vertices v and another vertex in a set w; -1 if no such path
	 */
	public int length(Iterable<Integer> v, Iterable<Integer> w) {
		for (int i : v)
			checkVertex(i);
		for (int i : w)
			checkVertex(i);
		return sap(v, w)[0];
	}

	// a common ancestor that participates in shortest ancestral path; -1 if no
	// such path
	public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
		for (int i : v)
			checkVertex(i);
		for (int i : w)
			checkVertex(i);
		return sap(v, w)[1];
	}

	// helper function for finding shortest ancestral path between 2 sets of
	// vertices
	private int[] sap(Iterable<Integer> v, Iterable<Integer> w) {
		int minTotalDistance = Integer.MAX_VALUE;
		int newTotalDistance;
		int commonAncestor = -1;
		BreadthFirstDirectedPaths bfs_v = new BreadthFirstDirectedPaths(G, v);
		BreadthFirstDirectedPaths bfs_w = new BreadthFirstDirectedPaths(G, w);
		for (int s = 0; s < G.V(); s++) {
			newTotalDistance = bfs_v.distTo(s) + bfs_w.distTo(s);
			if (newTotalDistance >= 0 && newTotalDistance < minTotalDistance) {
				minTotalDistance = newTotalDistance;
				commonAncestor = s;
			}
		}
		if (minTotalDistance == Integer.MAX_VALUE)
			minTotalDistance = -1;
		return new int[] { minTotalDistance, commonAncestor };
	}

	private void checkVertex(int v) {
		if (v < 0 || v >= G.V())
			throw new java.lang.IndexOutOfBoundsException();
	}

	// for unit testing of this class (such as the one below)
	public static void main(String[] args) {
		In in = new In(args[0]);
		Digraph G = new Digraph(in);
		SAP sap = new SAP(G);
		while (!StdIn.isEmpty()) {
			int v = StdIn.readInt();
			int w = StdIn.readInt();
			int length = sap.length(v, w);
			int ancestor = sap.ancestor(v, w);
			StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
		}
	}

}
