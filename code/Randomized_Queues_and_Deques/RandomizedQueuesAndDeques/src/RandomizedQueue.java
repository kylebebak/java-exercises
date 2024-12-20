import java.util.Iterator;
import java.util.NoSuchElementException;

import javax.tools.JavaCompiler;

public class RandomizedQueue<Item> implements Iterable<Item> {
	private Item[] a;
	private int N = 0;

	public RandomizedQueue() {
		a = (Item[]) new Object[2];
	}

	public boolean isEmpty() {
		return N == 0;
	}

	public int size() {
		return N;
	}

	public void enqueue(Item item) {
		if (item == null)
			throw new NullPointerException();
		if (N == a.length)
			resize(a.length * 2);
//		if (last == a.length) StdOut.println("oops");
		a[N] = item;
		N++;
	}

	public Item dequeue() {
		if (isEmpty())
			throw new NoSuchElementException();

		int index = StdRandom.uniform(N);
		Item item = a[index];
		for (int j = index; j < N - 1; j++)
			a[j] = a[j + 1];
		a[N - 1] = null;
		N--;
		if (N <= a.length / 4)
			resize(a.length / 2);
		return item;
	}

	public Item sample() {
		if (isEmpty())
			throw new NoSuchElementException();
		return a[StdRandom.uniform(N)];
	}

	private void resize(int max) {
		assert max >= N;
		Item[] temp = (Item[]) new Object[max];
		for (int i = 0; i < N; i++) {
			temp[i] = a[i];
		}
		a = temp;
	}

	public Iterator<Item> iterator() {
		return new QueueIterator();
	}

	private class QueueIterator implements Iterator<Item> {
		private int i;
		public Item[] temp;

		public QueueIterator() {
			i = 0;
			temp = (Item[]) new Object[N];
			for (int j = 0; j < N; j++) temp[j] = a[j];
			StdRandom.shuffle(temp);
		}

		public boolean hasNext() {
			return i < N;
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}

		public Item next() {
			if (!hasNext())
				throw new NoSuchElementException();
			return temp[i++];
		}
	}
}
