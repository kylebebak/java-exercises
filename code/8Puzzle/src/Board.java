public class Board {

	private int N;
	private int[][] blocks;

	public Board(int[][] blocks) {
		// construct a board from an N-by-N array of blocks
		// (where blocks[i][j] = block in row i, column j)
		N = blocks.length;
		this.blocks = new int[N][N];
		for (int i = 0; i < N; i++)
			for (int j = 0; j < N; j++)
				this.blocks[i][j] = blocks[i][j];
	}

	public int dimension() {
		// board dimension N
		return N;
	}
	
	public int blockNumber(int r, int c) {
		return blocks[r][c];
	}

	public int hamming() {
		// number of blocks out of place
		int hammingPriority = 0;
		for (int i = 0; i < N; i++)
			for (int j = 0; j < N; j++)
				if (blocks[i][j] != 0 && blocks[i][j] != boardIndex(i, j))
					hammingPriority++;
		return hammingPriority;
	}

	public int manhattan() {
		// sum of Manhattan distances between blocks and goal
		int manhattanPriority = 0;
		int[] rc = new int[2];
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (blocks[i][j] == 0)
					continue;
				rc = blockIndices(blocks[i][j]);
				manhattanPriority += abs(rc[0] - i) + abs(rc[1] - j);
			}
		}
		return manhattanPriority;
	}

	public boolean isGoal() {
		// is this board the goal board?
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (blocks[i][j] == 0)
					continue;
				if (blocks[i][j] != boardIndex(i, j))
					return false;
			}
		}
		return true;
	}

	public Board twin() {
		// a board obtained by exchanging two adjacent blocks in the same row
		// neither of the two blocks exchanged can be a zero (the empty space)
		for (int i = 0; i < N; i++)
			if (!containsSpace(i))
				return switchTwoBlocks(i, 0, i, 1);
		return null;
	}

	public boolean equals(Object x) {
		// does this board equal x?
		if (x == this)
			return true;
		if (x == null)
			return false;
		if (x.getClass() != this.getClass())
			return false;
		Board that = (Board) x;
		if (that.N != N) // make sure boards have the same dimension before
							// comparing their entries
			return false;
		for (int i = 0; i < N; i++)
			for (int j = 0; j < N; j++)
				if (this.blocks[i][j] != that.blocks[i][j])
					return false;
		return true;
	}

	public Iterable<Board> neighbors() {
		// all neighboring boards
		ResizingArrayStack<Board> q = new ResizingArrayStack<Board>();

		int[] rc = findSpace();
		int r = rc[0];
		int c = rc[1];

		if (r != N - 1)
			q.push(switchTwoBlocks(r, c, r + 1, c));
		if (r != 0)
			q.push(switchTwoBlocks(r, c, r - 1, c));
		if (c != N - 1)
			q.push(switchTwoBlocks(r, c, r, c + 1));
		if (c != 0)
			q.push(switchTwoBlocks(r, c, r, c - 1));

		return q;
	}

	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append(N + "\n");
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++)
				s.append(String.format("%2d ", blocks[i][j]));
			s.append("\n");
		}
		return s.toString();
	}

	/********************
	 * helper functions
	 * 
	 ********************/

	private int abs(int in) {
		// returns the absolute value of an integer
		if (in >= 0)
			return in;
		else
			return -in;
	}

	private int[] blockIndices(int index) {
		// takes an index 1 through N * N and returns the
		// corresponding pair of 0 through N - 1, 0 through N - 1
		// row-column indices on the board
		index--;
		int[] rc = new int[2];
		rc[0] = index / N;
		rc[1] = index % N;
		return rc;
	}

	private int boardIndex(int r, int c) {
		// takes a pair of indices 0 through N - 1 for board position and
		// returns the corresponding index in the board array
		return N * r + c + 1;
	}

	private boolean containsSpace(int row) {
		// does the given row contain the blank space?
		for (int j = 0; j < N; j++)
			if (blocks[row][j] == 0)
				return true;
		return false;
	}

	private int[] findSpace() {
		// search board for the blank space and return its indices
		int[] rc = new int[2];
		for (int i = 0; i < N; i++)
			for (int j = 0; j < N; j++)
				if (blocks[i][j] == 0) {
					rc[0] = i;
					rc[1] = j;
					return rc;
				}
		return null;
	}

	private Board switchTwoBlocks(int r0, int c0, int r1, int c1) {
		int[][] b = new int[N][N];
		for (int i = 0; i < N; i++)
			for (int j = 0; j < N; j++)
				b[i][j] = blocks[i][j];
		b[r0][c0] = blocks[r1][c1];
		b[r1][c1] = blocks[r0][c0];
		Board board = new Board(b);
		return board;
	}
}
