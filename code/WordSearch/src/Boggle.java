import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

/**
 * 
 * @author kylebebak <br><br>
 * 
 *         Uses modified version of TST that allows for a default value to be
 *         passed to the constructor. All nodes in the TST that aren't the final
 *         letter in one of the string keys will have this default value. This
 *         makes it possible to pass a string to the TST method contains() and
 *         check whether this string is a prefix of some word in the TST<br><br>
 *         
 *         TST taken from : <br>
 *         http://algs4.cs.princeton.edu/code/
 */

public class Boggle {

	private char[][] grid;
	private int N;
	private TSTprefix<Integer> tst; // TST containing the dictionary
	private TreeSet<String> words; // where found words are placed
	private ArrayList<String> wordList; // for sorting strings by length after
										// they've been sorted alphabetically in
										// the tree set
	final private int MWL = 3; // minimum word length

	private Set<BogglePath> s;
	private Set<BogglePath> pathsToAdd;

	public Boggle(int N, int gridState, In dict) {
		this.N = N;
		grid = GridGenerator.generateGrid(N, gridState);
		tst = new TSTprefix<Integer>(0);

		while (!dict.isEmpty())
			tst.put(dict.readLine(), 1);

		words = new TreeSet<String>();
		for (int r = 0; r < N; r++)
			for (int c = 0; c < N; c++) {
				BogglePath bp = new BogglePath(N, r, c);
				bp.addChar(grid[r][c]);

				s = new HashSet<BogglePath>();
				s.add(bp);

				search(r, c);
			}

		wordList = new ArrayList<String>();
		for (String w : words)
			wordList.add(w);
		Collections.sort(wordList, new StringLengthComparator());
	}

	private void search(int r, int c) {

		while (!s.isEmpty()) {
			pathsToAdd = new HashSet<BogglePath>();

			Iterator<BogglePath> si = s.iterator();
			while (si.hasNext()) {
				BogglePath p = si.next();
				String w = p.getWord();

				if (tst.contains(w)) {
					if (tst.get(w) == 1 && w.length() >= MWL) {
						if (!words.contains(w))
							System.out.println(p.toString());
						words.add(w);
					}
				} else {
					si.remove();
					continue;
				}

				updatePath(p);
				si.remove();
			}
			s.addAll(pathsToAdd);
		}
	}

	private void updatePath(BogglePath p) {
		BogglePath np;

		np = new BogglePath(p); // new path

		// attempt horizontal and vertical moves
		if (np.moveRight()) {
			np.addChar(grid[np.getRow()][np.getColumn()]);
			pathsToAdd.add(np);
			np = new BogglePath(p);
		}

		if (np.moveDown()) {
			np.addChar(grid[np.getRow()][np.getColumn()]);
			pathsToAdd.add(np);
			np = new BogglePath(p);
		}

		if (np.moveLeft()) {
			np.addChar(grid[np.getRow()][np.getColumn()]);
			pathsToAdd.add(np);
			np = new BogglePath(p);
		}

		if (np.moveUp()) {
			np.addChar(grid[np.getRow()][np.getColumn()]);
			pathsToAdd.add(np);
			np = new BogglePath(p);
		}

		// attempt diagonal moves
		if (np.moveRightDown()) {
			np.addChar(grid[np.getRow()][np.getColumn()]);
			pathsToAdd.add(np);
			np = new BogglePath(p);
		}

		if (np.moveRightUp()) {
			np.addChar(grid[np.getRow()][np.getColumn()]);
			pathsToAdd.add(np);
			np = new BogglePath(p);
		}

		if (np.moveLeftDown()) {
			np.addChar(grid[np.getRow()][np.getColumn()]);
			pathsToAdd.add(np);
			np = new BogglePath(p);
		}

		if (np.moveLeftUp()) {
			np.addChar(grid[np.getRow()][np.getColumn()]);
			pathsToAdd.add(np);
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
		Boggle b = new Boggle(N, gridState, dict);

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
