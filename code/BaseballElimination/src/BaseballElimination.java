public class BaseballElimination {

	private final int N; // number of teams in league
	private SeparateChainingHashST<String, Integer> teams; // look up team #
															// with team name
	private String[] t; // look up team name with team #
	private int[] w; // number of wins for each team
	private int[] l; // number of losses for each team
	private int[] r; // number of games remaining for each team
	private int[][] g; // number of games remaining between each pair of
						// teams
	private FlowNetwork fn;

	private final int V; // number of total vertices in flow network
	private final int nChoose2; // cached for reuse

	public BaseballElimination(String filename) {
		// create a baseball division from given filename
		In in = new In(filename);
		N = in.readInt();
		w = new int[N];
		l = new int[N];
		r = new int[N];
		g = new int[N][N];
		t = new String[N];
		teams = new SeparateChainingHashST<String, Integer>();
		for (int i = 0; i < N; i++) {
			String teamName = in.readString();
			teams.put(teamName, i);
			t[i] = teamName;
			w[i] = in.readInt();
			l[i] = in.readInt();
			r[i] = in.readInt();
			for (int j = 0; j < N; j++)
				g[i][j] = in.readInt();
		}

		nChoose2 = N * (N - 1) / 2;
		V = nChoose2 + N + 1 + 1; // game vertices are in graph vertices 0 to
									// nC2 - 1, team vertices are in nC2 to nC2
									// + N - 1, source vertex s is at V - 2, and
									// sink vertex t is at V - 1
	}

	public int numberOfTeams() {
		return N;
	}

	public Iterable<String> teams() {
		// all teams
		return teams.keys();
	}

	public int wins(String team) {
		// number of wins for given team
		return w[teamIndex(team)];
	}

	public int losses(String team) {
		// number of losses for given team
		return l[teamIndex(team)];
	}

	public int remaining(String team) {
		// number of remaining games for given team
		return r[teamIndex(team)];
	}

	public int against(String team1, String team2) {
		// number of remaining games between team1 and team2
		return g[teamIndex(team1)][teamIndex(team2)];
	}

	public boolean isEliminated(String team) {
		// is given team eliminated?
		int t = teamIndex(team);
		newEliminationNetwork(t);
		for (FlowEdge e : fn.adj(V - 1))
			if (e.capacity() == Double.POSITIVE_INFINITY)
				return true;
		// team is trivially eliminated

		int allGamesPlayed = 0;
		for (FlowEdge e : fn.adj(V - 2))
			allGamesPlayed += e.capacity();
		FordFulkerson ff = new FordFulkerson(fn, V - 2, V - 1);
		return (ff.value() < allGamesPlayed);
	}

	public Iterable<String> certificateOfElimination(String team) {
		// subset R of teams that eliminates given team; null if not eliminated
		Queue<String> q = new Queue<String>();

		newEliminationNetwork(teamIndex(team));
		for (FlowEdge e : fn.adj(V - 1))
			if (e.capacity() == Double.POSITIVE_INFINITY) {
				q.enqueue(t[e.from() - nChoose2]);
				return q;
			}
		// team is trivially eliminated

		FordFulkerson ff = new FordFulkerson(fn, V - 2, V - 1);

		for (int T = 0; T < N; T++)
			if (T != teamIndex(team) && ff.inCut(nChoose2 + T))
				q.enqueue(t[T]);
		if (q.isEmpty())
			return null;
		else
			return q;
	}

	private int teamIndex(String teamName) {
		if (!teams.contains(teamName))
			throw new IllegalArgumentException("Team name(s) not valid");
		return teams.get(teamName);
	}

	/*
	 * helper function for preparing initializing capacities in network
	 * depending on which query team t is specified
	 */
	private void newEliminationNetwork(int t) {
		fn = new FlowNetwork(V);
		// N choose 2 team pairs, N team vertices, 1 source, 1 sink
		// in this order. source is at V - 2, sink at V - 1.
		// all edges involving query team have capacity 0 and are effectively
		// not in flow network

		int count = 0;
		for (int v = 0; v < N; v++) {
			for (int w = v + 1; w < N; w++) {
				// add edges from source vertex to game vertices
				if (v == t || w == t)
					fn.addEdge(new FlowEdge(V - 2, count, 0));
				else
					fn.addEdge(new FlowEdge(V - 2, count, g[v][w]));

				// add edges from game vertices to team vertices
				if (v == t)
					fn.addEdge(new FlowEdge(count, nChoose2 + v, 0));
				else
					fn.addEdge(new FlowEdge(count, nChoose2 + v,
							Double.POSITIVE_INFINITY));
				if (w == t)
					fn.addEdge(new FlowEdge(count, nChoose2 + w, 0));
				else
					fn.addEdge(new FlowEdge(count, nChoose2 + w,
							Double.POSITIVE_INFINITY));
				count++;
			}
			// add edges from team vertices to sink vertex
			if (v == t)
				fn.addEdge(new FlowEdge(nChoose2 + v, V - 1, 0));
			else {
				if (w[t] + r[t] - w[v] < 0)
					fn.addEdge(new FlowEdge(nChoose2 + v, V - 1,
							Double.POSITIVE_INFINITY));
				// set edge capacity to +inf if query team is trivially
				// eliminated to later identify which team eliminated them
				else
					fn.addEdge(new FlowEdge(nChoose2 + v, V - 1, w[t] + r[t]
							- w[v]));
			}
		}
	}

	public static void main(String[] args) {
		BaseballElimination division = new BaseballElimination(args[0]);
		for (String team : division.teams()) {
			if (division.isEliminated(team)) {
				StdOut.print(team + " is eliminated by the subset R = { ");
				for (String t : division.certificateOfElimination(team))
					StdOut.print(t + " ");
				StdOut.println("}");
			} else {
				StdOut.println(team + " is not eliminated");
			}
		}
	}
}
