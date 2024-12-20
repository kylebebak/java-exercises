/*************************************************************************
 *  Compilation:  javac IndexMinPQ.java
 *  Execution:    java IndexMinPQ
 *
 *  Minimum-oriented indexed PQ implementation using a binary heap.
 *
 *********************************************************************/

import java.util.NoSuchElementException;

/**
 * The <tt>IndexMinPQ</tt> class represents an indexed priority queue of generic
 * keys. It supports the usual <em>insert</em> and <em>delete-the-minimum</em>
 * operations. It is backed by a 4-ary heap which performs insert twice as fast
 * as a binary heap, and is equally fast with delete min
 */
public class MinPQ4d<Key extends Comparable<Key>> {
	private int NMAX; // maximum number of elements on PQ
	private int N; // number of elements on PQ
	private Key[] pq; // 4-ary heap using 0-based indexing

	/**
	 * Create an empty indexed priority queue with indices between 0 and NMAX-1.
	 * 
	 * @throws java.lang.IllegalArgumentException
	 *             if NMAX < 0
	 */
	public MinPQ4d(int NMAX) {
		if (NMAX < 0)
			throw new IllegalArgumentException();
		this.NMAX = NMAX;
		pq = (Key[]) new Comparable[NMAX]; // make this of length NMAX??
	}

	/**
	 * Is the priority queue empty?
	 */
	public boolean isEmpty() {
		return N == 0;
	}

	/**
	 * Return the number of keys on the priority queue.
	 */
	public int size() {
		return N;
	}

	/**
	 * Insert a new key into the queue
	 */
	public void insert(Key key) {
		pq[N] = key;
		swim(N);
		N++;
	}

	/**
	 * Return a minimal key.
	 * 
	 * @throws java.util.NoSuchElementException
	 *             if priority queue is empty.
	 */
	public Key minKey() {
		if (N == 0)
			throw new NoSuchElementException("Priority queue underflow");
		return pq[0];
	}

	/**
	 * Delete a minimal key and return it.
	 * 
	 * @throws java.util.NoSuchElementException
	 *             if priority queue is empty.
	 */
	public Key delMin() {
		if (N == 0)
			throw new NoSuchElementException("Priority queue underflow");
		Key min = pq[0];
		exch(0, N - 1);
		N--;
		sink(0);
		pq[N] = null; // to help with garbage collection
		return min;
	}

	/**
	 * Return the key associated with index i.
	 * 
	 * @throws java.lang.IndexOutOfBoundsException
	 *             unless 0 &le; i < NMAX
	 * @throws java.util.NoSuchElementException
	 *             no key is associated with index i
	 */
	public Key keyOf(int i) {
		if (i < 0 || i >= NMAX)
			throw new IndexOutOfBoundsException();
		return pq[i];
	}

	/**************************************************************
	 * General helper functions
	 **************************************************************/
	private boolean greater(int i, int j) {
		return pq[i].compareTo(pq[j]) > 0;
	}

	private void exch(int i, int j) {
		Key t = pq[i];
		pq[i] = pq[j];
		pq[j] = t;
	}

	/**************************************************************
	 * Heap helper functions
	 **************************************************************/
	private void swim(int k) {
		while (k > 0 && greater((k - 1) / 4, k)) {
			exch(k, (k - 1) / 4);
			k = (k - 1) / 4;
		}
	}

	private void sink(int k) {
		while (4 * k + 1 < N) {
			int j = 4 * k + 1;

			for (int c = j + 1; c < j + 4 && c < N; c++) {
				if (greater(j, c))
					j = c;
			}
			if (!greater(k, j))
				break;
			exch(k, j);
			k = j;
		}
	}

	public static void main(String[] args) {
		// insert a bunch of strings
		String[] strings = { "it", "was", "the", "best", "of", "times", "it",
				"was", "the", "worst" };

		MinPQ4d<String> pq = new MinPQ4d<String>(strings.length);
		for (int i = 0; i < strings.length; i++)
			pq.insert(strings[i]);

		// delete and print each key
		while (!pq.isEmpty()) {
			StdOut.println(pq.delMin());
		}

	}
}