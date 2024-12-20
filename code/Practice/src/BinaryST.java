import java.util.LinkedList;

public class BinaryST<Key extends Comparable<Key>, Val> {

	private Node root; // root of BST
	private int size = 0;

	private class Node {
		Key key;
		Val val;
		Node left;
		Node right;

		public Node(Key key, Val val, Node left, Node right) {
			this.key = key;
			this.val = val;
			this.left = left;
			this.right = right;
		}
	}

	public void put(Key key, Val val) {
		root = put(root, key, val);
	}

	private Node put(Node n, Key key, Val val) {

		if (n == null) {
			size++;
			return new Node(key, val, null, null);
		}

		if (key.compareTo(n.key) < 0)
			n.left = put(n.left, key, val);
		else if (key.compareTo(n.key) > 0)
			n.right = put(n.right, key, val);
		else
			return new Node(key, val, n.left, n.right);

		return n;
	}

	public Val get(Key key) {
		return get(root, key);
	}

	private Val get(Node n, Key key) {

		if (n == null)
			throw new IllegalArgumentException("Key not in BST");

		if (key.compareTo(n.key) < 0)
			return get(n.left, key);
		else if (key.compareTo(n.key) > 0)
			return get(n.right, key);

		return n.val;
	}

	public int size() {
		return size;
	}

	/**
	 * Traverses the tree and enqueues all its keys
	 */
	public Iterable<Key> keys() {
		LinkedList<Key> q = new LinkedList<Key>();
		keys(root, q);
		return q;
	}

	private void keys(Node n, LinkedList<Key> q) {
		if (n != null) {
			q.add(n.key);
			keys(n.left, q);
			keys(n.right, q);
		}
	}

	/**
	 * Searches down left subtrees until reaching null, then enqueues the key
	 * and moves back up, searching down right subtrees it encounters on the way
	 * back up
	 */
	public Iterable<Key> ascendingSort() {
		if (size == 0)
			return new LinkedList<Key>();

		LinkedList<Key> q = new LinkedList<Key>();
		ascendingSort(root, q);
		return q;
	}

	private void ascendingSort(Node n, LinkedList<Key> q) {
		if (n.left != null)
			ascendingSort(n.left, q);
		q.add(n.key);
		if (n.right != null)
			ascendingSort(n.right, q);
	}

	/**
	 * Searches down right subtrees until reaching null, then enqueues the key
	 * and moves back up, searching down left subtrees it encounters on the way
	 * back up
	 */
	public Iterable<Key> descendingSort() {
		if (size == 0)
			return new LinkedList<Key>();

		LinkedList<Key> q = new LinkedList<Key>();
		descendingSort(root, q);
		return q;
	}
	
	private void descendingSort(Node n, LinkedList<Key> q) {
		if (n.right != null)
			descendingSort(n.right, q);
		q.add(n.key);
		if (n.left != null)
			descendingSort(n.left, q);
	}

	public static void main(String[] args) {
		BinaryST<String, Integer> st = new BinaryST<String, Integer>();
		for (int i = 0; !StdIn.isEmpty(); i++) {
			String key = StdIn.readString();
			st.put(key, i);
		}

		StdOut.println();
		for (String s : st.keys())
			StdOut.println(s + " " + st.get(s));

		StdOut.println();
		for (String s : st.ascendingSort())
			StdOut.println(s + " " + st.get(s));

		StdOut.println();
		for (String s : st.descendingSort())
			StdOut.println(s + " " + st.get(s));

		StdOut.println();
		StdOut.println(st.size());
	}

}
