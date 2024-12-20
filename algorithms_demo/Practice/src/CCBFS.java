import java.util.LinkedList;

public class CCBFS {

	private Graph g;
	private int V; // number of vertices in graph
	private int count; // number of connected components

	private boolean[] marked;
	private int[] id;
	private int[] size;

	public CCBFS(Graph g) {
		this.g = g;
		this.V = g.V();
		count = -1;

		LinkedList<Integer> q = new LinkedList<Integer>();
		marked = new boolean[V];
		id = new int[V];
		size = new int[V];

		for (int s = 0; s < V; s++) {
			
			if (!marked[s]) {
				marked[s] = true;
				q.add(s);
				id[s] = ++count;
				size[count]++;
			}
			
			while (!q.isEmpty()) {

				int v = q.pollFirst();
				for (int w : g.adj(v))
					if (!marked[w]) {
						q.add(w);
						marked[w] = true;
						id[w] = count;
						size[count]++;
					}
			}
		}
	}
	
	/**
	 * Size of CC containing v
	 */
	public int size(int v) {
		return size[id[v]];
	}
	/**
	 * ID of component containing v
	 */
	public int id(int v) {
		return id[v];
	}

	/**
	 * Are these two vertices connected?
	 */
	public boolean areConnected(int v, int w) {
		return id[v] == id[w];
	}

	/**
	 * Total number of connected components
	 */
	public int count() {
		return count + 1;
	}

	public static void main(String[] args) {
		In in = new In(args[0]);
        Graph G = new Graph(in);
        CCBFS cc = new CCBFS(G);

        // number of connected components
        int M = cc.count();
        StdOut.println(M + " components");

        // compute list of vertices in each connected component
        LinkedList<Integer>[] components = (LinkedList<Integer>[]) new LinkedList[M];
        for (int i = 0; i < M; i++)
            components[i] = new LinkedList<Integer>();
        
        for (int v = 0; v < G.V(); v++)
            components[cc.id(v)].add(v);

        // print results
        for (int i = 0; i < M; i++) {
            for (int v : components[i])
                StdOut.print(v + " ");
            StdOut.println();
        }
    }
}
