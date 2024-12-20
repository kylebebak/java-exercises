import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class DijkstraSPs {

	private EdgeWeightedDigraph g;
	private int V; // number of vertices in graph
	private int s; // source vertex
	private int count; // number of vertices connected to s, including s
	private final double epsilon = .000000000001;

	private int[] vertexTo;
	private double[] distTo;
	PriorityQueue<Double> q;
	Map<Double, Integer> mdv;
	Map<Integer, Double> mvd;

	public DijkstraSPs(EdgeWeightedDigraph g, int s) {
		this.g = g;
		this.V = g.V();
		this.s = s;
		count = 0;

		q = new PriorityQueue<Double>();
		mdv = new HashMap<Double, Integer>();
		mvd = new HashMap<Integer, Double>();

		vertexTo = new int[V];
		distTo = new double[V];
		for (int i = 0; i < V; i++)
			distTo[i] = Double.POSITIVE_INFINITY;

		vertexTo[s] = s;
		distTo[s] = 0;
		q.add(distTo[s]);
		mdv.put(distTo[s], s);
		mvd.put(s, distTo[s]);

		while (!q.isEmpty()) {
			
			count++;
			double minDist = q.poll();
			int v = mdv.get(minDist);	
			mdv.remove(minDist);
			mvd.remove(v);
			// mapping distances to vertices lets me retrieve a vertex by
			// polling its distTo key in the pq and then getting this key

			for (DirectedEdge e : g.adj(v))
				relax(e);
		}
	}

	private void relax(DirectedEdge e) {
		int v = e.from();
		int w = e.to();

		double newDist = distTo[v] + e.weight();

		if (newDist < distTo[w]) {
			if (mvd.containsKey(w)) {
				// if vertex already present in pq, delete it
				q.remove(distTo[w]);				
				mdv.remove(distTo[w]);
			}
			// update distTo--vertex pair
			if (mdv.containsKey(newDist))
				newDist += epsilon;
			// this ensures there are no duplicate distTo keys
			q.add(newDist);
			mdv.put(newDist, w);
			mvd.put(w, newDist);

			distTo[w] = newDist;
			vertexTo[w] = v;
		}

	}

	public boolean hasPathTo(int v) {
		return distTo[v] != Double.POSITIVE_INFINITY;
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
	
	public double distTo(int v) {
		return distTo[v];
	}

	public static void main(String[] args) {
		In in = new In(args[0]);
        EdgeWeightedDigraph G = new EdgeWeightedDigraph(in);
        int s = Integer.parseInt(args[1]);

        // compute shortest paths
        DijkstraSPs sp = new DijkstraSPs(G, s);

        // print shortest paths
        for (int t = 0; t < G.V(); t++) {
            if (sp.hasPathTo(t)) {
                StdOut.printf("%d to %d (%.2f)  ", s, t, sp.distTo(t));
                if (sp.hasPathTo(t)) {
                    for (int v : sp.pathTo(t)) {
                        StdOut.print(v + "   ");
                    }
                }
                StdOut.println();
            }
            else {
                StdOut.printf("%d to %d         no path\n", s, t);
            }
        }
    }

}
