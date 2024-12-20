import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
	private Item[] a;
	private int N = 0;
	private int last = 0;
	private int first = 0;

	public Deque() {
		a = (Item[]) new Object[2];
	}

	public boolean isEmpty() {
		return N == 0;
	}

	public int size() {
		return N;
	}
	

	public void addFirst(Item item) {
		if (item == null)
			throw new NullPointerException();
		if (N == a.length)
			resize(a.length * 2);
		first--;
		if (first < 0) first = a.length - 1;
		a[first] = item;
		N++;
	}

	public void addLast(Item item) {
		if (item == null)
			throw new NullPointerException();
		if (N == a.length)
			resize(a.length * 2);
		a[last++] = item;
		if (last == a.length)
			last = 0;
		N++;
	}

	
	public Item removeFirst() {
		if (isEmpty())
			throw new NoSuchElementException();

		Item item = a[first];
		a[first++] = null;
		if (first == a.length)
			first = 0;
		N--;
		if (N <= a.length / 4)
			resize(a.length / 2);
		return item;
	}

	public Item removeLast() {
		if (isEmpty())
			throw new NoSuchElementException();
		last--;
		if (last < 0) last = a.length - 1;

		Item item = a[last];
		a[last] = null;
		N--;
		if (N <= a.length / 4)
			resize(a.length / 2);
		return item;
	}

	private void resize(int max) {
		assert max >= N;
		Item[] temp = (Item[]) new Object[max];
		for (int i = 0; i < N; i++) {
			temp[i] = a[(first + i) % a.length];
		}
		a = temp;
		first = 0;
		last = N;
	}

	public Iterator<Item> iterator() {
		return new QueueIterator();
	}

	private class QueueIterator implements Iterator<Item> {
		private int i;

		public QueueIterator() {
			i = first;
		}

		public boolean hasNext() {
			return i < N + first;
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}

		public Item next() {
			if (!hasNext())
				throw new NoSuchElementException();
			return a[(i++ % a.length)];
		}
	}
}
