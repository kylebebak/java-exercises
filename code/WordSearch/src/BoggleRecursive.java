import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeSet;

/**
 * 
 * @author kylebebak <br>
 * <br>
 * 
 *         Uses modified version of TST that allows for a default value to be
 *         passed to the constructor. All nodes in the TST that aren't the final
 *         letter in one of the string keys will have this default value. This
 *         makes it possible to pass a string to the TST method contains() and
 *         check whether this string is a prefix of some word in the TST<br>
 * <br>
 * 
 *         TST taken from : <br>
 *         http://algs4.cs.princeton.edu/code/
 */

public class BoggleRecursive {

	private char[][] grid;
	private int N;
	private TSTprefix<Integer> tst; // TST containing the dictionary
	private TreeSet<String> words; // where found words are placed
	private ArrayList<String> wordList; // for sorting strings by length after
										// they've been sorted alphabetically in
										// the tree set
	final private int MWL = 3; // minimum word length

	public BoggleRecursive(int N, int gridState, In dict) {
		this.N = N;
		grid = GridGenerator.generateGrid(N, gridState);
		tst = new TSTprefix<Integer>(0);

		while (!dict.isEmpty())
			tst.put(dict.readLine(), 1);

		words = new TreeSet<String>();
		for (int r = 0; r < N; r++)
			for (int c = 0; c < N; c++) {
				BogglePath p = new BogglePath(N, r, c);
				p.addChar(grid[r][c]);

				search(r, c, p);
			}

		wordList = new ArrayList<String>();
		for (String w : words)
			wordList.add(w);
		Collections.sort(wordList, new StringLengthComparator());
	}

	/**
	 * recursive search method makes copies of p and sends them to valid
	 * adjacent squares, and then dereferences p
	 */
	private void search(int r, int c, BogglePath p) {

		if (!isValidPrefix(p))
			return;

		BogglePath np = new BogglePath(p);

		if (np.moveRight()) {
			np.addChar(grid[np.getRow()][np.getColumn()]);
			search(r, c + 1, np);
			np = new BogglePath(p);
		}

		if (np.moveDown()) {
			np.addChar(grid[np.getRow()][np.getColumn()]);
			search(r + 1, c, np);
			np = new BogglePath(p);
		}

		if (np.moveLeft()) {
			np.addChar(grid[np.getRow()][np.getColumn()]);
			search(r, c - 1, np);
			np = new BogglePath(p);
		}

		if (np.moveUp()) {
			np.addChar(grid[np.getRow()][np.getColumn()]);
			search(r - 1, c, np);
			np = new BogglePath(p);
		}

		if (np.moveRightDown()) {
			np.addChar(grid[np.getRow()][np.getColumn()]);
			search(r + 1, c + 1, np);
			np = new BogglePath(p);
		}

		if (np.moveRightUp()) {
			np.addChar(grid[np.getRow()][np.getColumn()]);
			search(r - 1, c + 1, np);
			np = new BogglePath(p);
		}

		if (np.moveLeftDown()) {
			np.addChar(grid[np.getRow()][np.getColumn()]);
			search(r + 1, c - 1, np);
			np = new BogglePath(p);
		}

		if (np.moveLeftUp()) {
			np.addChar(grid[np.getRow()][np.getColumn()]);
			search(r - 1, c - 1, np);
		}
		p = null;
	}

	/**
	 * returns true if p's string is a valid prefix in the TST, false if it's
	 * not. also, caches p's string if it is a string in then tst
	 */
	private boolean isValidPrefix(BogglePath p) {
		String w = p.getWord();

		if (tst.contains(w)) {
			if (tst.get(w) == 1 && w.length() >= MWL) {
				if (!words.contains(w))
					System.out.println(p.toString());
				words.add(w);
			}
			return true;
		} else {
			return false;
		}
	}

	public Iterable<String> wordsFound() {
		return wordList;
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
		int gridState = Integer.parseInt(args[1]);
		In dict = new In(args[2]);
		BoggleRecursive b = new BoggleRecursive(N, gridState, dict);

		for (String s : b.wordsFound())
			StdOut.println(s);
		b.printGrid();
		StdOut.println(b.numberFound()
				+ " distinct words were found in the grid");
		String longest = b.longestFound();
		StdOut.println(longest + " (length : " + longest.length() + ")"
				+ " was the longest word found");
	}
}
