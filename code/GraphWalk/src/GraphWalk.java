import java.awt.Color;
import java.awt.Font;

public class GraphWalk {

	private int DS; // max digit sum
	private int MI; // max int whose digitSum is <= to DS
	private int dim;
	private Graph g;
	private CCbfs cc;

	public GraphWalk(int DS) {
		// construct a new graph whose vertices are valid so long as the sum of
		// their digits <= DS
		if (DS < 0)
			throw new IllegalArgumentException(
					"max digit sum must be non-negative");
		this.DS = DS;
		MI = 0;
		while (true) {
			if (digitSum(MI) > DS)
				break;
			MI++;
		}
		dim = 2 * MI + 1;
		g = new Graph(dim * dim);
		for (int r = 0; r < dim; r++)
			for (int c = 0; c < dim; c++)
				if (isValid(r, c)) {
					if (c < dim - 1)
						if (isValid(r, c + 1))
							g.addEdge(dim * r + c, dim * r + (c + 1));
					if (r < dim - 1)
						if (isValid(r + 1, c))
							g.addEdge(dim * r + c, dim * (r + 1) + c);
				}
		cc = new CCbfs(g);
	}

	private int digitSum(int n) {
		String N = Integer.toString(n);
		int ds = 0;
		for (int d = 0; d < N.length(); d++)
			ds += Character.getNumericValue(N.charAt(d));
		return ds;
	}

	private boolean isValid(int r, int c) {
		int x = c - MI;
		int y = r - MI;
		x = Math.abs(x);
		y = Math.abs(y);
		return digitSum(x) + digitSum(y) <= DS;
	}

	/**
	 * getters for number of reachable sites, number of connected components
	 */
	public int numberReachable() {
		// reachable vertices from starting point (0, 0)
		return cc.size(MI * dim + MI);
	}

	public int numberReachable(int x, int y) {
		// reachable vertices from starting point (x, y)
		if (Math.abs(x) > MI || Math.abs(y) > MI)
			throw new IllegalArgumentException(
					"vertices must be within graph size : this vertex is trivially unreachable from 0, 0");
		int r = y + MI;
		int c = x + MI;
		return cc.size(r * dim + c);
	}

	public int connectedComponents() {
		// number of distinct CCs
		return cc.count();
	}

	public boolean areConnected(int r0, int c0, int r1, int c1) {
		return cc.areConnected(r0 * dim + c0, r1 * dim + c1);
	}

	public void drawGraph() {
		StdDraw.clear();
		StdDraw.setPenColor(StdDraw.BLACK);
		StdDraw.setXscale(0, dim);
		StdDraw.setYscale(0, dim);
		// this makes the background black
		StdDraw.filledSquare(dim / 2.0, dim / 2.0, dim / 2.0);

		// draw N-by-N grid with numbers on blocks
		for (int r = 0; r < dim; r++) {
			for (int c = 0; c < dim; c++) {
				if (areConnected(r, c, MI + 1, MI + 1))
					StdDraw.setPenColor(StdDraw.WHITE);
				else if (isValid(r, c))
					StdDraw.setPenColor(StdDraw.RED);
				else
					StdDraw.setPenColor(StdDraw.BLACK);
				StdDraw.filledSquare(c + 0.5, r + 0.5, 0.45);
			}
		}
	}

	/**
	 * @param args
	 * <br>
	 *            args[0] is the max digit sum for the graph
	 */
	public static void main(String[] args) {
		// GraphWalk gw = new GraphWalk(Integer.parseInt(args[0]));
		GraphWalk gw = new GraphWalk(19);
		StdOut.println(gw.numberReachable());
		StdOut.println(gw.connectedComponents());

		// turn on animation mode
		StdDraw.show(0);

		gw.drawGraph();
		StdDraw.show(0);
	}

}
