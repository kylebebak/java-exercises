public class MergeSortBottomUp {

	public static void sort(Comparable[] a) {
		int range = 1;
		while (range <= a.length) {
			// sort progressively bigger sections of the array until you finally
			// sort the first half against the second half
			range *= 2;

			Comparable[] aux = new Comparable[a.length];
			for (int i = 0; i < a.length; i++)
				aux[i] = a[i]; // copy a to aux

			int lo = 0;
			while (lo < a.length - 1) {
				// sort range - sized sections of the array, from left to right
				int hi = Math.min(a.length, lo + range);
				int m = Math.min(a.length - 1, lo + range / 2 - 1);

				merge(a, aux, lo, m, hi);
				lo += range;
			}

		}
	}

	private static void merge(Comparable[] a, Comparable[] aux, int lo, int m,
			int hi) {

		// merge aux back to a, sorting [lo, m] against [m + 1, hi)
		int i = lo;
		int j = m + 1;
		for (int k = lo; k < hi; k++) {
			if (i > m)
				a[k] = aux[j++];
			else if (j >= hi)
				a[k] = aux[i++];
			else if (less(aux[i], aux[j]))
				a[k] = aux[i++];
			else
				a[k] = aux[j++];
		}

	}

	/***********************************************************************
	 * Helper functions for sorting
	 ***********************************************************************/
	private static boolean less(Comparable v, Comparable w) {
		return v.compareTo(w) < 0;
	}

	/**
	 * Not needed in bottom up implementation
	 */
	private static void exch(Object[] a, int i, int j) {
		Object swapped = a[i];
		a[i] = a[j];
		a[j] = swapped;
	}

	public static void show(Comparable[] a) {
		for (int i = 0; i < a.length; i++)
			StdOut.println(a[i]);
	}

	// Read strings from standard input, sort them, and print.
	public static void main(String[] args) {
		String[] a = StdIn.readStrings();
		StdOut.println(a.length + " items to sort");
		MergeSortBottomUp.show(a);

		StdOut.println();
		MergeSortBottomUp.sort(a);
		MergeSortBottomUp.show(a);
		
		StdOut.println();
		StdOut.println(BinarySearchTest.binarySearch(a, "jay"));
	}

}
