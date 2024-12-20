
public class Path {

	private boolean[][] marked;
	private int r;
	private int c;
	private int R;
	private int C;
	private java.util.Stack<int[]> vertices;
	
	/**
	 * Construct a new path starting in top-left corner
	 */
	public Path(int R, int C) {
		this.R = R;
		this.C = C;
		marked = new boolean[R][C];
		marked[0][0] = true;
		vertices = new java.util.Stack<int[]>();
		vertices.push(new int[] {0, 0});
		r = 0;
		c = 0;
	}
	
	/**
	 * COPY CONSTRUCTOR
	 */ 
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Path(Path p) {
		R = p.getHeight();
		C = p.getWidth();
		marked = new boolean[R][C];
		for (int r = 0; r < R; r++)
			for (int c = 0; c < C; c++)
				marked[r][c] = p.marked[r][c];
		vertices = (java.util.Stack) p.vertices.clone();
		r = p.getRow();
		c = p.getColumn();
	}
	
	public int getHeight() {
		return R;
	}

	public int getWidth() {
		return C;
	}

	public int getRow() {
		return r;
	}

	public int getColumn() {
		return c;
	}
	
	/**
	 * Path has no legal moves remaining
	 */ 
	public boolean isStuck() {
		if (isLegal(r - 1, c))
			return false;
		if (isLegal(r + 1, c))
			return false;
		if (isLegal(r, c - 1))
			return false;
		if (isLegal(r, c + 1))
			return false;

		return true;
	}

	public boolean isAtGoal() {
		return (r == R - 1 && c == C - 1);
	}
	
	/**
	 * Move head of path to new r, c if move is legal. <br/> Returns
	 * true if move is legal, false if move is not.
	 */ 
	public boolean moveLeft() {
		if (isLegal(r, c - 1)) {
			marked[r][c - 1] = true;
			c--;
			vertices.push(new int[] {r, c});
			return true;
		}
		return false;
	}

	public boolean moveRight() {
		if (isLegal(r, c + 1)) {
			marked[r][c + 1] = true;
			c++;
			vertices.push(new int[] {r, c});
			return true;
		}
		return false;
	}

	public boolean moveUp() {
		if (isLegal(r - 1, c)) {
			marked[r - 1][c] = true;
			r--;
			vertices.push(new int[] {r, c});
			return true;
		}
		return false;
	}
	
	public boolean moveDown() {
		if (isLegal(r + 1, c)) {
			marked[r + 1][c] = true;
			r++;
			vertices.push(new int[] {r, c});
			return true;
		}
		return false;
	}
	
	public boolean isLegal(int r, int c) {
		return (r >= 0 && r < R && c >= 0 && c < C && !marked[r][c]);
	}
	
	/**
	 * @OVERRIDE this prints out all vertices visited by path
	 */ 
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int[] a : vertices) {
			sb.append("(" + a[0] + ", ");
			sb.append(a[1] + ")" + "  --> ");
		}
		return sb.toString();
	}
	
	public Iterable<int[]> vertexTo() {
		return vertices;
	}
}
