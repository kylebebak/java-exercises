/*************************************************************************
 * Compilation: javac CCbfs.java Execution: java CCbfs filename.txt
 * Dependencies: Graph.java StdOut.java Queue.java Data files:
 * http://algs4.cs.princeton.edu/41undirected/tinyG.txt
 * 
 * Compute connected components using depth first search. Runs in O(E + V) time.
 * 
 * % java CC tinyG.txt 3 components 0 1 2 3 4 5 6 7 8 9 10 11 12
 * 
 *************************************************************************/

public class CCbfs {
	private boolean[] marked; // marked[v] = has vertex v been marked?
	private int[] id; // id[v] = id of connected component containing v
	private int[] size; // size[id] = number of vertices in given component
	private int count; // number of connected components

	public CCbfs(Graph G) {
		marked = new boolean[G.V()];
		id = new int[G.V()];
		size = new int[G.V()];
		count = 0;
		for (int v = 0; v < G.V(); v++) {
			if (!marked[v]) {
				bfs(G, v);
				count++;
			}
		}
	}

	// breadth first search
	private void bfs(Graph G, int s) {
		marked[s] = true;
		id[s] = count;
		size[count]++;

		Queue<Integer> q = new Queue<Integer>();
		q.enqueue(s);

		while (!q.isEmpty()) {
			int v = q.dequeue();
			for (int w : G.adj(v)) {
				if (!marked[w]) {
					marked[w] = true;
					id[w] = count;
					size[count]++;
					q.enqueue(w);
				}
			}
		}
	}

	// id of connected component containing v
	public int id(int v) {
		return id[v];
	}

	// size of connected component containing v
	public int size(int v) {
		return size[id[v]];
	}

	// number of connected components
	public int count() {
		return count;
	}

	// are v and w in the same connected component?
	public boolean areConnected(int v, int w) {
		return id(v) == id(w);
	}

	// test client
	public static void main(String[] args) {
		In in = new In(args[0]);
		Graph G = new Graph(in);
		CCbfs cc = new CCbfs(G);

		// number of connected components
		int M = cc.count();
		StdOut.println(M + " components");

		// compute list of vertices in each connected component
		Queue<Integer>[] components = (Queue<Integer>[]) new Queue[M];
		for (int i = 0; i < M; i++) {
			components[i] = new Queue<Integer>();
		}
		for (int v = 0; v < G.V(); v++) {
			components[cc.id(v)].enqueue(v);
		}

		// print results
		for (int i = 0; i < M; i++) {
			for (int v : components[i]) {
				StdOut.print(v + " ");
			}
			StdOut.println();
		}
	}
}