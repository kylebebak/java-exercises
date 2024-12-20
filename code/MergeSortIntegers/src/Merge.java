public class Merge {

	public static void sort(Comparable[] a) {
		sort(a, 0, a.length);
	}

	// Sort a[lo, hi).
	public static void sort(Comparable[] a, int lo, int hi) {
		int N = hi - lo; // number of elements to sort

		// 0- or 1-element file, so we're done
		if (N <= 1)
			return;

		// recursively sort left and right halves
		int mid = lo + N / 2;
		sort(a, lo, mid);
		sort(a, mid, hi);

		// merge two sorted subarrays
		Comparable[] aux = new Comparable[N];
		int i = lo, j = mid;
		for (int k = 0; k < N; k++) {
			if (i == mid)
				aux[k] = a[j++];
			else if (j == hi)
				aux[k] = a[i++];
			else if (a[j].compareTo(a[i]) < 0)
				aux[k] = a[j++];
			else
				aux[k] = a[i++];
		}

		// copy back
		for (int k = 0; k < N; k++) {
			a[lo + k] = aux[k];
		}

		/*
		 * optional for printing trace of each merge operation
		 */
		for (int l = 0; l < a.length; l++) {
			StdOut.print(a[l] + " ");
		}
		StdOut.println();
	}

	/***********************************************************************
	 * Check if array is sorted - useful for debugging
	 ***********************************************************************/
	private static boolean isSorted(Comparable[] a) {
		for (int i = 1; i < a.length; i++)
			if (a[i].compareTo(a[i - 1]) < 0)
				return false;
		return true;
	}

	/***********************************************************************
	 * Show results
	 ***********************************************************************/
	public static void show(Comparable[] a) {
		for (int i = 0; i < a.length; i++)
			System.out.println(a[i]);
	}

	/*
	 * 1 2 6 4 7 11 13 3 4 8 9 21 test string of integers an line of whitespace
	 * has to trail the integers entered (just push enter after putting in the
	 * ints, then close stdin with ctrl + d)
	 */

	public static void main(String[] args) {
		String[] a = StdIn.readAll().split("\\s+");
		Integer[] aa = new Integer[a.length];

		for (int i = 0; i < a.length; i++)
			aa[i] = Integer.parseInt(a[i]);
		Merge.sort(aa);
		StdOut.println();

		for (int i = 0; i < aa.length; i++)
			StdOut.print(aa[i] + " ");
		StdOut.println();
	}
}
