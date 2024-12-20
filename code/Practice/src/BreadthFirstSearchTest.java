import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;

public class BreadthFirstSearchTest {

	private Graph g;
	private int V; // number of vertices in graph
	private int s; // source vertex
	private int count; // number of vertices connected to s, excluding s

	private boolean[] marked;
	private int[] vertexTo;

	public BreadthFirstSearchTest(Graph g, int s) {
		this.g = g;
		this.V = g.V();
		this.s = s;
		count = 0;

		LinkedList<Integer> q = new LinkedList<Integer>();
		marked = new boolean[V];
		vertexTo = new int[V];

		q.add(s);
		marked[s] = true;
		vertexTo[s] = s;
		while (!q.isEmpty()) {

			int v = q.pollFirst();
			for (int w : g.adj(v))
				if (!marked[w]) {
					q.add(w);
					marked[w] = true;
					vertexTo[w] = v;
					count++;
				}
		}
	}

	public boolean hasPathTo(int v) {
		return marked[v];
	}

	public int count() {
		return count;
	}

	public Iterable<Integer> pathTo(int v) {
		if (!hasPathTo(v))
			return new ArrayDeque<Integer>();
		
		Deque<Integer> path = new ArrayDeque<Integer>();
		path.addFirst(v);
		while (v != s) {
			v = vertexTo[v];
			path.addFirst(v);
		}
		return path;
	}

	public static void main(String[] args) {
		In in = new In(args[0]);
        Graph G = new Graph(in);
        int s = Integer.parseInt(args[1]);
        BreadthFirstSearchTest search = new BreadthFirstSearchTest(G, s);
        for (int v = 0; v < G.V(); v++) {
            if (search.hasPathTo(v))
                StdOut.print(v + " ");
        }

        StdOut.println();
        if (search.count() != G.V()) StdOut.println("NOT connected");
        else                         StdOut.println("connected");

	}

}
