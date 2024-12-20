import java.util.ArrayList;

public class TestInHashMap {
	
	private SeparateChainingHashST<String, Bag<Integer>> nouns;
	private Digraph G;

	public TestInHashMap(String synsets, String hypernyms) {
		
		String[] thisLine; // local variables for storing strings returned by
							// split()
		String[] thisSynset;
		
		// construct synset/nouns HashMap
		nouns = new SeparateChainingHashST<String, Bag<Integer>>();
		In in = new In(synsets);
		int counter = 0;
		while (in.hasNextLine()) {
			thisLine = in.readLine().split(",");
			int v = Integer.parseInt(thisLine[0]);
			thisSynset = thisLine[1].split(" ");
			for (String noun : thisSynset) {
				Bag<Integer> synsetIDs;
				if (!nouns.contains(noun))
					synsetIDs = new Bag<Integer>();
				else
					synsetIDs = nouns.get(noun);
				synsetIDs.add(v);
				nouns.put(noun, synsetIDs);
			}
			counter++;
		}

		// construct hypernyms digraph
		G = new Digraph(counter);
		in = new In(hypernyms);
		while (in.hasNextLine()) {
			thisLine = in.readLine().split(",");
			for (int i = 1; i < thisLine.length; i++)
				G.addEdge(Integer.parseInt(thisLine[0]),
						Integer.parseInt(thisLine[i]));
		}
		
		StdOut.println(nouns.size());
		StdOut.println(G.V());
		StdOut.println(G.E());
		for(int i : nouns.get("Aberdeen"))
			StdOut.println(i);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TestInHashMap ti = new TestInHashMap(args[0], args[1]);
	}

}
