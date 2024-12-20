/**
 * kylebebak@gmail.com
 * <p>
 * 
 * Encode or decode an input stream using the Burrows-Wheeler algorithm.
 * <p>
 * 
 * Execution: java BurrowsWheeler - < input.txt (compress)<br>
 * Execution: java BurrowsWheeler + < input.txt (expand)
 * <p>
 * 
 * <b>% time java BurrowsWheeler - < mobydick.txt | java MoveToFront - | java
 * Huffman - > mobyDickOutputFileName</b>
 * <p>
 * 
 * <b>% java BurrowsWheeler - < abra.txt | java HexDump</b><br>
 * 00 00 00 03 41 52 44 21 52 43 41 41 41 41 42 42<br>
 * 128 bits
 */
public class BurrowsWheeler {
	// this class has no constructor, only static methods for encoding and
	// decoding files, like Huffman.java. it does not accept these files as
	// input to these functions, but rather as command line arguments

	private static CircularSuffixArray csa; // for encoding
	private static final int R = 256; // radix for decoding (extended ascii)

	// apply Burrows-Wheeler encoding, reading from standard input and writing
	// to standard output
	public static void encode() {
		StringBuilder sb = new StringBuilder();
		while (!BinaryStdIn.isEmpty())
			sb.append(BinaryStdIn.readChar());
		String s = sb.toString();
		csa = new CircularSuffixArray(s);

		// find which index "i" in the sorted suffix array was the first suffix
		// in the original array
		int i = 0;
		while (csa.index(i) != 0)
			i++;

		// write "i" followed by the last column of the circular suffix matrix
		BinaryStdOut.write(i);
		for (int j = 0; j < s.length(); j++) {
			i = csa.index(j);
			if (i == 0)
				BinaryStdOut.write(s.charAt(csa.length() - 1));
			else
				BinaryStdOut.write(s.charAt(i - 1));
		}

		// close output stream
		BinaryStdOut.close();
	}

	// apply Burrows-Wheeler decoding, reading from standard input and writing
	// to standard output
	public static void decode() {
		int first; // row in sorted order where first unshifted suffix occurs
		first = BinaryStdIn.readInt();

		StringBuilder sb = new StringBuilder();
		while (!BinaryStdIn.isEmpty())
			sb.append(BinaryStdIn.readChar());
		char[] t = sb.toString().toCharArray(); // last column of sorted suffix
												// matrix

		// now use key-indexed counting to recover first column by sorting last
		int N = t.length;
		int[] count = new int[R + 1];
		for (int i = 0; i < N; i++)
			count[t[i] + 1]++;

		// compute cumulates for first column
		for (int r = 0; r < R; r++)
			count[r + 1] += count[r];

		int[] next = new int[N];
		// if index[i] = j, i.e., the ith sorted suffix was originally in row j
		// in the unsorted suffix matrix, then next[i] is the row in the sorted
		// suffix matrix where the (j + 1)st suffix appears
		char[] aux = new char[N]; // where sorted first column will be cached

		// look at characters in row i of last column one by one, find the row j
		// in the first column where the suffix right before i's is located, and
		// set next[j] = i
		char c;
		for (int i = 0; i < N; i++) {
			c = t[i];
			next[count[c]] = i;
			aux[count[c]++] = c;
		}

		// write out decoded message with BinaryStdOut using next and aux arrays
		int index = first;
		for (int i = 0; i < N; i++) {
			BinaryStdOut.write(aux[index]);
			index = next[index];
		}

		// close output stream
		BinaryStdOut.close();
	}

	/**
	 * @param args
	 * <br>
	 *            if args[0] is '-', apply Burrows-Wheeler encoding<br>
	 *            if args[0] is '+', apply Burrows-Wheeler decoding
	 */
	public static void main(String[] args) {
		if (args[0].equals("-"))
			encode();
		else if (args[0].equals("+"))
			decode();
		else
			throw new IllegalArgumentException("Illegal command line argument");

	}

}
