import java.util.LinkedList;
import java.util.ListIterator;

public class ListOperations {

	public static <T> LinkedList<T> reverse(java.util.List<T> l) {
		ListIterator<T> li = l.listIterator();
		LinkedList<T> reverse = new LinkedList<T>();
		
		reverse(reverse, li);
		return reverse;
	}

	// public static void reverse(java.util.List<?> l) {
	// ListIterator<?> li = l.listIterator();
	// }
	
	private static <T> void reverse(LinkedList<T> reverse, ListIterator<T> li) {
		T e;
		if (li.hasNext()) {
			e = li.next();
			reverse(reverse, li);
			reverse.add(e);
		}
	}
	
	public static void main(String[] args) {
		LinkedList<Integer> l = new LinkedList<Integer>();
		int range = Integer.parseInt(args[0]);
		for (int i = 0; i < range; i++)
			l.add((int) (Math.random() * range));
		
		StdOut.println();
		for (Object i : l.toArray())
			StdOut.println(i);
		
		StdOut.println();
		for (Object i : ListOperations.reverse(l).toArray())
			StdOut.println(i);
	}

}
