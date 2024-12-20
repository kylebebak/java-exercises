import java.util.Arrays;

/**
 * 
 * kylebebak@gmail.com
 * <p>
 * 
 * CSA computes all circular suffixes for a string input and then sorts these
 * suffixes. It takes space linear in the length of the input string; it does
 * not instantiate a new string for each suffix. It takes O(n log n) time if the
 * input doesn't have lots of cycles. In the worst case, e.g. the input is
 * "AAAAAAAAAAAAA", it takes O(n^2 log n) time. After construction, length() and
 * index() are constant time operations
 * <p>
 * 
 * Execution: java CircularSuffixArray input.txt
 */
public class CircularSuffixArray {

	private Suffix[] csa;
	private char[] chars;

	public CircularSuffixArray(String s) {
		// circular suffix array of s
		chars = s.toCharArray();
		csa = new Suffix[s.length()];
		for (int i = 0; i < s.length(); i++)
			csa[i] = new Suffix(i);
		Arrays.sort(csa);
	}

	public int length() {
		// length of s
		return csa.length;
	}

	public int index(int i) {
		// returns original index of ith sorted suffix
		return csa[i].getOffset();
	}

	private class Suffix implements Comparable<Suffix> {
		// Suffix doesn't instantiate a chars array, it just points to the
		// global one that is an instance variable of CSA

		int offset;

		Suffix(int offset) {
			this.offset = offset;
		}

		public int getOffset() {
			return offset;
		}

		public int compareTo(Suffix that) {
			int index = 0;
			while (index < chars.length) {
				if (chars[(index + offset) % chars.length] < chars[(index + that.offset)
						% chars.length])
					return -1;
				else if (chars[(index + offset) % chars.length] > chars[(index + that.offset)
						% chars.length])
					return 1;
				index++;
			}
			return 0;
		}
	}

	/**
	 * Test client for CSA. Reads in a text file, converts it to a big string,
	 * passes this string to a new instance of CSA, and prints out the original
	 * indices of the sorted suffixes.
	 * <p>
	 * 
	 * @param args
	 *            args[0] = text file name
	 */
	public static void main(String[] args) {
		BinaryIn in = new BinaryIn(args[0]);
		StringBuilder sb = new StringBuilder();

		while (!in.isEmpty())
			sb.append(in.readChar());

		String s = sb.toString();
		CircularSuffixArray csa = new CircularSuffixArray(s);
		for (int i = 0; i < csa.length(); i++)
			StdOut.println(csa.index(i));
	}

}
