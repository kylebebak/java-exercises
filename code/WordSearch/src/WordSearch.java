import java.util.TreeSet;

/**
 * 
 * @author kylebebak <br>
 * <br>
 * 
 *         Uses modified version of TST that allows for a default value to be
 *         passed to the constructor. All nodes in the TST that aren't the final
 *         letter in one of the string keys will have this default value. This
 *         makes it possible to pass a string to the TST method contains and
 *         check whether this string is a prefix of some word in the TST<br>
 * <br>
 * 
 *         TST taken from : <br>
 *         http://algs4.cs.princeton.edu/code/
 */

public class WordSearch {

	private char[][] grid;
	private int N;
	private TSTprefix<Integer> tst; // TST containing the dictionary
	private TreeSet<String> words; // where found words are placed
	final private int MWL = 3; // minimum word length

	public WordSearch(int N, int state, In dict) {
		this.N = N;
		grid = GridGenerator.generateGrid(N, state);
		tst = new TSTprefix<Integer>(0);

		while (!dict.isEmpty())
			tst.put(dict.readLine(), 1);

		words = new TreeSet<String>();
		for (int r = 0; r < N; r++)
			for (int c = 0; c < N; c++)
				search(r, c);
	}

	private void search(int r, int c) {
		right(r, c);
		left(r, c);
		up(r, c);
		down(r, c);
		rightDown(r, c);
		rightUp(r, c);
		leftDown(r, c);
		leftUp(r, c);
	}

	/**
	 * horizontal and vertical search helper functions
	 */
	private void right(int r, int c) {
		if (c <= N - MWL) {
			String toMatch = new String();
			for (int i = c; i < N; i++) {
				toMatch += grid[r][i];
				if (!tst.contains(toMatch))
					return;
				if (tst.get(toMatch) == 1 && toMatch.length() >= MWL)
					words.add(toMatch);
			}
		}
	}

	private void left(int r, int c) {
		if (c >= MWL - 1) {
			String toMatch = new String();
			for (int i = c; i >= 0; i--) {
				toMatch += grid[r][i];
				if (!tst.contains(toMatch))
					return;
				if (tst.get(toMatch) == 1 && toMatch.length() >= MWL)
					words.add(toMatch);
			}
		}
	}

	private void down(int r, int c) {
		if (r <= N - MWL) {
			String toMatch = new String();
			for (int i = r; i < N; i++) {
				toMatch += grid[i][c];
				if (!tst.contains(toMatch))
					return;
				if (tst.get(toMatch) == 1 && toMatch.length() >= MWL)
					words.add(toMatch);
			}
		}
	}

	private void up(int r, int c) {
		if (r >= MWL - 1) {
			String toMatch = new String();
			for (int i = r; i >= 0; i--) {
				toMatch += grid[i][c];
				if (!tst.contains(toMatch))
					return;
				if (tst.get(toMatch) == 1 && toMatch.length() >= MWL)
					words.add(toMatch);
			}
		}
	}

	/**
	 * diagonal search helper functions
	 */
	private void rightDown(int r, int c) {
		if (r <= N - MWL && c <= N - MWL) {
			String toMatch = new String();
			for (int i = 0; r + i < N && c + i < N; i++) {
				toMatch += grid[r + i][c + i];
				if (!tst.contains(toMatch))
					return;
				if (tst.get(toMatch) == 1 && toMatch.length() >= MWL)
					words.add(toMatch);
			}
		}
	}

	private void rightUp(int r, int c) {
		if (r >= MWL - 1 && c <= N - MWL) {
			String toMatch = new String();
			for (int i = 0; r - i >= 0 && c + i < N; i++) {
				toMatch += grid[r - i][c + i];
				if (!tst.contains(toMatch))
					return;
				if (tst.get(toMatch) == 1 && toMatch.length() >= MWL)
					words.add(toMatch);
			}
		}
	}

	private void leftDown(int r, int c) {
		if (r <= N - MWL && c >= MWL - 1) {
			String toMatch = new String();
			for (int i = 0; r + i < N && c - i >= 0; i++) {
				toMatch += grid[r + i][c - i];
				if (!tst.contains(toMatch))
					return;
				if (tst.get(toMatch) == 1 && toMatch.length() >= MWL)
					words.add(toMatch);
			}
		}
	}

	private void leftUp(int r, int c) {
		if (r >= MWL - 1 && c >= MWL - 1) {
			String toMatch = new String();
			for (int i = 0; r - i >= 0 && c - i >= 0; i++) {
				toMatch += grid[r - i][c - i];
				if (!tst.contains(toMatch))
					return;
				if (tst.get(toMatch) == 1 && toMatch.length() >= MWL)
					words.add(toMatch);
			}
		}
	}

	public Iterable<String> wordsFound() {
		return words;
	}

	public int numberFound() {
		return words.size();
	}

	public String longestFound() {
		int max = 0;
		String longest = new String();
		for (String s : words)
			if (s.length() > max) {
				max = s.length();
				longest = s;
			}
		return longest;
	}

	public void printGrid() {
		for (int r = 0; r < N; r++) {
			for (int c = 0; c < N; c++)
				StdOut.print(grid[r][c] + " ");
			StdOut.println();
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int N = Integer.parseInt(args[0]);
		int state = Integer.parseInt(args[1]);
		In dict = new In(args[2]);
		WordSearch ws = new WordSearch(N, state, dict);

		for (String s : ws.wordsFound())
			StdOut.println(s);
		ws.printGrid();
		StdOut.println(ws.numberFound()
				+ " distinct words were found in the grid");
		StdOut.println(ws.longestFound() + " was the longest word found");
	}

}
