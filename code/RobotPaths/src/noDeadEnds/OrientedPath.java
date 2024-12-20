package noDeadEnds;
public class OrientedPath {

	private boolean[][] marked;
	private int r;
	private int c;
	private int N;

	private int turnCounter; // decrement by one for each left turn, increment
								// by one for each right turn
	private int direction; // direction of previous movement on path. 0 = right,
							// 1 = down, 2 = left, 3 = up

	public OrientedPath(int N) {
		this.N = N;
		marked = new boolean[N][N];
		marked[0][0] = true;
		int r = 0;
		int c = 0;
		turnCounter = 0;
		direction = 0;
	}

	public OrientedPath(OrientedPath p) {
		N = p.getSize();
		marked = new boolean[N][N];
		for (int r = 0; r < N; r++)
			for (int c = 0; c < N; c++)
				marked[r][c] = p.isMarked(r, c);
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

	public boolean isMarked(int r, int c) {
		return marked[r][c];
	}

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
		return (r == N - 1 && c == N - 1);
	}
	
	public int getDirection() {
		return direction;
	}
	
	public int getTurnCounter() {
		return turnCounter;
	}

	// 0 = right, 1 = down, 2 = left, 3 = up. left turn --, right turn ++
	public boolean moveRight() {
		if (isLegal(r, c + 1)) {
			marked[r][c + 1] = true;
			c++;
			if (direction == 1)
				turnCounter--;
			else if (direction == 3)
				turnCounter++;
			direction = 0;
			return true;
		}
		return false;
	}

	public boolean moveDown() {
		if (isLegal(r + 1, c)) {
			marked[r + 1][c] = true;
			r++;
			if (direction == 0)
				turnCounter++;
			else if (direction == 2)
				turnCounter--;
			direction = 1;
			return true;
		}
		return false;
	}

	public boolean moveLeft() {
		if (isLegal(r, c - 1)) {
			marked[r][c - 1] = true;
			c--;
			if (direction == 1)
				turnCounter++;
			else if (direction == 3)
				turnCounter--;
			direction = 2;
			return true;
		}
		return false;
	}

	public boolean moveUp() {
		if (isLegal(r - 1, c)) {
			marked[r - 1][c] = true;
			r--;
			if (direction == 0)
				turnCounter--;
			else if (direction == 2)
				turnCounter++;
			direction = 3;
			return true;
		}
		return false;
	}

	public boolean isLegal(int r, int c) {
		return (r >= 0 && r < N && c >= 0 && c < N && !marked[r][c]);
	}

}
