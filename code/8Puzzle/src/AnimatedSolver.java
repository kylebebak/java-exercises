import java.awt.Color;
import java.awt.Font;

public class AnimatedSolver {

	private final static int delay = 300;

	private MinPQ<SearchNode> qMain = new MinPQ<SearchNode>();
	private MinPQ<SearchNode> qTwin = new MinPQ<SearchNode>();
	private SearchNode lastNode, lastNodeCopy; // solution node
	private Boolean isSolvable;

	public AnimatedSolver(Board initial) {
		// find a solution to the initial board (using the A* algorithm)
		qMain.insert(new SearchNode(initial, null, 0));
		qTwin.insert(new SearchNode(initial.twin(), null, 0));

		/*
		 * StdOut.println(); StdOut.println("initial manhattan = " +
		 * initial.manhattan()); StdOut.println("initial hamming = " +
		 * initial.hamming()); StdOut.println();
		 * 
		 * StdOut.println("initial"); StdOut.println(initial); StdOut.println();
		 */

		while (true) {

			SearchNode processed = qMain.delMin();
			SearchNode processedTwin = qTwin.delMin();

			if (processed.board.isGoal()) {
				isSolvable = true;
				lastNode = processed;
				lastNodeCopy = processed;
				break;
			}
			if (processedTwin.board.isGoal()) {
				isSolvable = false;
				break;
			}

			if (processed.previous == null) {
				for (Board b : processed.board.neighbors())
					qMain.insert(new SearchNode(b, processed,
							processed.moves + 1));
				for (Board b : processedTwin.board.neighbors())
					qTwin.insert(new SearchNode(b, processedTwin,
							processed.moves + 1));
				continue;
			}

			for (Board b : processed.board.neighbors())
				if (!b.equals(processed.previous.board))
					qMain.insert(new SearchNode(b, processed,
							processed.moves + 1));
			for (Board b : processedTwin.board.neighbors())
				if (!b.equals(processedTwin.previous.board))
					qTwin.insert(new SearchNode(b, processedTwin,
							processed.moves + 1));
		}

	}

	public boolean isSolvable() {
		// is the initial board solvable?
		return isSolvable;
	}

	public int moves() {
		// min number of moves to solve initial board; -1 if no solution
		if (!isSolvable)
			return -1;
		return lastNode.moves;
	}

	public Iterable<Board> solution() {
		// sequence of boards in a shortest solution; null if no solution
		ResizingArrayStack<Board> st = new ResizingArrayStack<Board>();
		if (!isSolvable)
			return null;
		while (true) {
			st.push(lastNodeCopy.board);
			lastNodeCopy = lastNodeCopy.previous;
			if (lastNodeCopy == null) {
				lastNodeCopy = lastNode;
				return st;
			}
		}
	}

	private static void drawBoard(Board board) {
		int N = board.dimension();
		StdDraw.clear();
		StdDraw.setPenColor(StdDraw.BLACK);
		StdDraw.setXscale(0, N);
		StdDraw.setYscale(0, N);
		StdDraw.filledSquare(N / 2.0, N / 2.0, N / 2.0);

		// draw N-by-N grid with numbers on blocks
		for (int r = 0; r < N; r++) {
			for (int c = 0; c < N; c++) {
				int blockNumber = board.blockNumber(r, c);

				if (blockNumber == 0)
					continue;

				/*
				 * StdDraw.setPenColor(StdDraw.WHITE); StdDraw.filledSquare(c +
				 * 0.5, N - r - 0.5, 0.45);
				 * 
				 * StdDraw.setFont(new Font("SansSerif", Font.PLAIN, 12));
				 * StdDraw.setPenColor(StdDraw.BLACK);
				 */
				float hue = (float) .5 + (blockNumber - 1) / (float) (3 * N * N);
				StdDraw.setPenColor(Color.getHSBColor(hue, 1, 1));
				StdDraw.filledSquare(c + 0.5, N - r - 0.5, 0.45);

				StdDraw.setFont(new Font("SansSerif", Font.PLAIN, 12));
				StdDraw.setPenColor(StdDraw.BLACK);

				StdDraw.text(c + 0.5, N - r - 0.5, "" + blockNumber);
				// "" converts int blockNumber to a string with concatenation

			}
		}
	}

	public static void main(String[] args) {
		// create initial board from file
		In in = new In(args[0]);
		int N = in.readInt();
		int[][] blocks = new int[N][N];
		for (int i = 0; i < N; i++)
			for (int j = 0; j < N; j++)
				blocks[i][j] = in.readInt();
		Board initial = new Board(blocks);

		// solve the puzzle
		Solver solver = new Solver(initial);

		// print solution to standard output
		if (!solver.isSolvable())
			StdOut.println("No solution possible");
		else {
			// turn on animation mode
			StdDraw.show(0);
			for (Board board : solver.solution()) {
				drawBoard(board);
				StdDraw.show(delay);
			}
		}
	}

	private class SearchNode implements Comparable<SearchNode> {
		/*
		 * nested class that contains a pointer to the previous search node that
		 * generated this search node, a reference to the board of the search
		 * node, and the number N of search nodes dequeued from MinPQ before
		 * this one
		 */
		private Board board;
		private SearchNode previous; // instance of previous search node
		private int moves; // number of search nodes that have been dequeued
							// from
							// MinPQ

		// before this one

		private SearchNode(Board board, SearchNode previous, int moves) {
			this.board = board;
			this.previous = previous;
			this.moves = moves;
		}

		public int compareTo(SearchNode that) {
			if (this.board.manhattan() + this.moves < that.board.manhattan()
					+ that.moves)
				return -1;
			if (this.board.manhattan() + this.moves > that.board.manhattan()
					+ that.moves)
				return 1;
			return 0;
		}
	}
}
