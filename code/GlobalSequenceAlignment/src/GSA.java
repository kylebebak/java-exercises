/**
 * http://introcs.cs.princeton.edu/java/assignments/sequence.html
 * <p>
 * Uses dynamic programming (storing and reusing results in a bottom-up
 * implementation of an otherwise intractable recursive algorithm) to find the
 * configuration between 2 genetic sequences that minimizes their edit distance.
 * Edit distance is computed in the following way : for each character /
 * character pair in the sequences, add 0 for a match, add 1 for a mismatch, and
 * add 2 for a character in the longer sequence paired with a a gap in the
 * shorted sequence.
 * <p>
 * This implementation has an instance variable allocated for the edit distance
 * matrix, and this matrix uses space proportional to M * N, where M and N are
 * the lengths of the sequences being compared.
 * 
 */
public class GSA {

	private int M; // length of first string
	private int N; // length of second string
	private char[] c1;
	private char[] c2;
	private int[][] ed; // edit distance matrix

	int pad; // for printing ed matrix to string

	/**
	 * Pass two strings as input and GSA will compute their edit distance matrix
	 * and find the optimal way to align them
	 */
	public GSA(String s1, String s2) {
		c1 = s1.toCharArray();
		c2 = s2.toCharArray();
		M = c1.length;
		N = c2.length;
		int min = Math.min(M, N);
		int diff = Math.abs(M - N); // absolute value of input string length
									// difference

		ed = new int[M + 1][N + 1];
		// first string goes down, second string goes right

		// initialize the last column and last row of ed matrix
		for (int m = 0; m <= M; m++)
			ed[m][N] = (M - m) * 2;
		for (int n = 0; n <= N; n++)
			ed[M][n] = (N - n) * 2;

		// fill in rest of ed, making sure that ed[i+1][j+1], ed[i+1][j], and
		// ed[i][j+1] are computed before computing ed[i][j]
		for (int i = min - 1; i >= 0; i--) {

			int i1 = i;
			int i2 = i;
			if (M > N)
				i2 += diff;
			if (M < N)
				i1 += diff;

			for (int m = M - min + i; m >= 0; m--) {
				// go up along farthest uncomputed column
				int o11;
				if (c1[m] == c2[i1])
					o11 = 0;
				else
					o11 = 1;
				ed[m][i1] = edMin(ed[m + 1][i1 + 1] + o11, ed[m + 1][i1] + 2,
						ed[m][i1 + 1] + 2);
			}

			for (int n = N - min + i; n >= 0; n--) {
				// go left along farthest uncomputed row
				int o11;
				if (c1[i2] == c2[n])
					o11 = 0;
				else
					o11 = 1;
				ed[i2][n] = edMin(ed[i2 + 1][n + 1] + o11, ed[i2][n + 1] + 2,
						ed[i2 + 1][n] + 2);
			}

		}
		pad = Math.max(Integer.toString(ed[M][0]).length(),
				Integer.toString(ed[0][N]).length());
	}

	/**
	 * Helper function compares 3 values and returns the smallest
	 */
	private int edMin(int ed11, int ed10, int ed01) {
		int smallest = ed11;

		if (smallest > ed10)
			smallest = ed10;
		if (smallest > ed01)
			smallest = ed01;
		return smallest;
	}

	public int editDistance() {
		return ed[0][0];
	}

	/**
	 * Returns the edit distance matrix calculated for the two strings
	 */
	public String editDistanceMatrix() {
		StringBuilder sb = new StringBuilder();

		sb.append(fixedLenthString("", pad + 2));
		for (int n = 0; n < N; n++)
			sb.append(fixedLenthString(Character.toString(c2[n]), pad + 2));
		sb.append(fixedLenthString("-", pad + 2));
		sb.append("\r\n"); // carriage return, new line

		for (int m = 0; m <= M; m++) {
			if (m < M)
				sb.append(fixedLenthString(Character.toString(c1[m]), pad + 2));
			else
				sb.append(fixedLenthString("-", pad + 2));

			for (int n = 0; n <= N; n++)
				sb.append(fixedLenthString(Integer.toString(ed[m][n]), pad + 2));
			if (m < M)
				sb.append("\r\n"); // carriage return, new line
		}
		return sb.toString();
	}

	/**
	 * Returns a string representation showing which characters from each string
	 * are aligned in the optimal alignment
	 */
	public String optimalAlignment() {

		StringBuilder s1 = new StringBuilder();
		StringBuilder s2 = new StringBuilder();
		StringBuilder dist = new StringBuilder();

		int m = 0;
		int n = 0;
		while (m < M || n < N) {

			// StdOut.println("m = " + m);
			// StdOut.println("n = " + n);
			if (m < M && n < N) {
				if ((ed[m][n] == ed[m + 1][n + 1] && c1[m] == c2[n])
						|| (ed[m][n] == ed[m + 1][n + 1] + 1 && c1[m] != c2[n])) {
					// match char at m in s1 to char at n in s2
					s1.append(c1[m] + " ");
					s2.append(c2[n] + " ");
					if (c1[m++] == c2[n++])
						dist.append("0 ");
					else
						dist.append("1 ");
					continue;
				}
			}

			if (m < M) {
				if (ed[m][n] == ed[m + 1][n] + 2) {
					s1.append(c1[m++] + " ");
					s2.append("- ");
					dist.append("2 ");
					continue;
				}
			}

			if (n < N) {
				if (ed[m][n] == ed[m][n + 1] + 2) {
					s1.append("- ");
					s2.append(c2[n++] + " ");
					dist.append("2 ");
					continue;
				}
			}
		}
		return s1.toString() + "\r\n" + s2.toString() + "\r\n"
				+ dist.toString();
	}

	/**
	 * Formatting helper function for padding a string with white space
	 */
	private String fixedLenthString(String string, int length) {
		return String.format("%1$" + length + "s", string);
	}

	public static void main(String[] args) {

		String[] ss = StdIn.readStrings();
		if (ss.length != 2)
			throw new IllegalArgumentException(
					"File must have two strings separated by a line break, with no other white space");

//		GSA gsa = new GSA("TAAGGTCA", "AACAGTTACC");
//		GSA gsa = new GSA("atattat", "tattata");
//		GSA gsa = new GSA("abcdefghizzzzjklmnop", "azzbcdefghijklmnop");
		GSA gsa = new GSA(ss[0], ss[1]);
		StdOut.println(gsa.editDistance());

		StdOut.println();
		StdOut.println(gsa.editDistanceMatrix());

		StdOut.println();
		StdOut.println(gsa.optimalAlignment());

	}
}
