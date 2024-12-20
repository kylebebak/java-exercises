/**
 * @author kylebebak<br>
 * <br>
 * 
 *         a search data type for boggle word search. this class keeps instance
 *         variables for vertices that have been visited, the current vertex (r,
 *         c), and the current string word it has constructed
 */
public class BogglePath {

	private boolean[][] marked;
	private int r;
	private int c;
	private int N;
	private String word;
	private java.util.Stack<int[]> vertices;

	/**
	 * Construct a new N x N path starting at row r, column c
	 */
	public BogglePath(int N, int r, int c) {
		this.N = N;
		this.r = r;
		this.c = c;
		word = new String();
		marked = new boolean[N][N];
		marked[r][c] = true;
		vertices = new java.util.Stack<int[]>();
		vertices.push(new int[] { r, c });
	}

	/**
	 * COPY CONSTRUCTOR
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public BogglePath(BogglePath p) {
		N = p.getSize();
		marked = new boolean[N][N];
		for (int r = 0; r < N; r++)
			for (int c = 0; c < N; c++)
				marked[r][c] = p.marked[r][c];
		vertices = (java.util.Stack) p.vertices.clone();
		word = new String(p.word);
		r = p.getRow();
		c = p.getColumn();
	}

	public int getSize() {
		return N;
	}

	public int getRow() {
		return r;
	}

	public int getColumn() {
		return c;
	}

	public String getWord() {
		return word;
	}

	public void addChar(char toAdd) {
		word += toAdd;
	}

	/**
	 * 
	 * @param r
	 * @param c
	 * @return is a move to the new r and c legal?
	 */
	private boolean isLegal(int r, int c) {
		return (r >= 0 && r < N && c >= 0 && c < N && !marked[r][c]);
	}

	/**
	 * Move head of path to new r, c if move is legal. <br>
	 * Returns true if move is legal, false if move is not. <br>
	 * First are helper functions for horizontal and vertical moves
	 */
	public boolean moveRight() {
		if (isLegal(r, c + 1)) {
			marked[r][c + 1] = true;
			c++;
			vertices.push(new int[] { r, c });
			return true;
		}
		return false;
	}

	public boolean moveLeft() {
		if (isLegal(r, c - 1)) {
			marked[r][c - 1] = true;
			c--;
			vertices.push(new int[] { r, c });
			return true;
		}
		return false;
	}

	public boolean moveDown() {
		if (isLegal(r + 1, c)) {
			marked[r + 1][c] = true;
			r++;
			vertices.push(new int[] { r, c });
			return true;
		}
		return false;
	}

	public boolean moveUp() {
		if (isLegal(r - 1, c)) {
			marked[r - 1][c] = true;
			r--;
			vertices.push(new int[] { r, c });
			return true;
		}
		return false;
	}

	/**
	 * helper functions for diagonal moves
	 */
	public boolean moveRightDown() {
		if (isLegal(r + 1, c + 1)) {
			marked[r + 1][c + 1] = true;
			r++;
			c++;
			vertices.push(new int[] { r, c });
			return true;
		}
		return false;
	}

	public boolean moveRightUp() {
		if (isLegal(r - 1, c + 1)) {
			marked[r - 1][c + 1] = true;
			r--;
			c++;
			vertices.push(new int[] { r, c });
			return true;
		}
		return false;
	}

	public boolean moveLeftDown() {
		if (isLegal(r + 1, c - 1)) {
			marked[r + 1][c - 1] = true;
			r++;
			c--;
			vertices.push(new int[] { r, c });
			return true;
		}
		return false;
	}

	public boolean moveLeftUp() {
		if (isLegal(r - 1, c - 1)) {
			marked[r - 1][c - 1] = true;
			r--;
			c--;
			vertices.push(new int[] { r, c });
			return true;
		}
		return false;
	}

	/**
	 * @OVERRIDE this prints out all vertices visited by search
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int[] a : vertices) {
			sb.append("(" + a[0] + ", ");
			sb.append(a[1] + ")" + "  --> ");
		}
		return sb.toString();
	}

	public Iterable<int[]> vertices() {
		return vertices;
	}
}
