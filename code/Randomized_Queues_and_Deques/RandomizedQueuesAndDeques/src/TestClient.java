public class TestClient {

	public static void main(String[] args) {
		
		RandomizedQueue<Integer> rq = new RandomizedQueue<Integer>();
		for (int i = 0; i < 100000; i++)
			rq.enqueue(i);

		StdOut.println();
		StdOut.println("dequeue random items from array list");
		for (int i = 0; i < 100000; i++)
			rq.dequeue();
		
		
		for (int i = 0; i < 1000; i++)
			rq.enqueue(i);
		
		for (int i = 0; i < 900; i++)
			//StdOut.println(rq.dequeue());
			rq.dequeue();

		
		StdOut.println();
		StdOut.println("return values from iterator");
		for (Integer i : rq)
			StdOut.println(i);
		
		StdOut.println();
		StdOut.println();

		
		
		
		Deque<Integer> d = new Deque<Integer>();
		
//		for (int i = 0; i < 20; i++)
//			d.addFirst(i);
//		for (int i = 20; i < 40; i++)
//			d.addLast(i);
//		
//		StdOut.println();
//		StdOut.println("remove random items from either the front or the back");
//		for (int i = 0; i < 20; i++) {
//			if (StdRandom.uniform(2) == 0)
//				StdOut.println(d.removeFirst());
//			else
//				StdOut.println(d.removeLast());
//		}
//		
//		StdOut.println();
//		StdOut.println("return values from iterator");
//		for (Integer i : d)
//			StdOut.println(i);
		
//		for (int i = 0; i < 1000; i++) {
//			int input = StdRandom.uniform(50);
//			if (StdRandom.uniform(2) == 0) d.addLast(input);
//			else d.addFirst(input);
//		}
//		
//		for (int i = 0; i < 1000; i++) {
//			if (StdRandom.uniform(2) == 0) StdOut.println(d.removeLast());
//			else StdOut.println(d.removeFirst());
//		}
//		
//		for (int i = 0; i < 1000; i++) {
//			int input = StdRandom.uniform(50);
//			if (StdRandom.uniform(2) == 0) d.addLast(input);
//			else d.addFirst(input);
//		}
//		
//		for (int i = 0; i < 980; i++) {
//			if (StdRandom.uniform(2) == 0) StdOut.println(d.removeLast());
//			else StdOut.println(d.removeFirst());
//		}
//		
//		StdOut.println();
//		StdOut.println();
//		for (int i : d) StdOut.println(i);
		
		
	}

}
