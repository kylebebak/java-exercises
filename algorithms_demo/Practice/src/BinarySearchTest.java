public class BinarySearchTest {

	/**
	 * Search array a for key k and return k's index in array. Return -1 if key
	 * not in array. Array must be in sorted order
	 */
	public static int binarySearch(Comparable[] a, Comparable k) {

		int lo = 0;
		int hi = a.length - 1;
		int m;

		while (lo <= hi) {
			m = (lo + hi) / 2;
			if (k.compareTo(a[m]) < 0)
				hi = m - 1;
			else if (k.compareTo(a[m]) > 0)
				lo = m + 1;
			else
				return m;
		}

		return -1; // indicates key was not in array

	}

	public static int pShiftedBinarySearch(Comparable[] a, Comparable k, int p) {

		int lo = 0;
		int hi = a.length - 1;
		int m;
		int ms; // shifted m

		while (lo <= hi) {
			m = (lo + hi) / 2;
			ms = (m + p) % a.length;

			if (k.compareTo(a[ms]) < 0)
				hi = m - 1;
			else if (k.compareTo(a[ms]) > 0)
				lo = m + 1;
			else
				return ms;
		}

		return -1; // indicates key was not in array

	}

	static public void main(String[] args) {
		Integer[] a = { 46, 51, 53, 3, 5, 7, 8, 9, 10, 13, 21, 24, 27, 35, 39, 42,
				44, 45 };
		int target = pShiftedBinarySearch(a, Integer.parseInt(args[0]), 3);
	    StdOut.println(target);
	}

}
