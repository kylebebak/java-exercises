import java.util.ArrayList;

public class WordNet {

	private SeparateChainingHashST<String, Bag<Integer>> nouns;
	private Digraph G;
	private ArrayList<String> synsets = new ArrayList<String>();
	private SAP sap;
	private DirectedCycle dc;

	// i need a list of all the nouns sorted alphabetically, no repeats, and
	// each noun
	// has a list of integers that point to all the different
	// synsets (each synset has a synset id) in which the noun is found

	// constructor takes the name of the two input files
	public WordNet(String synsets, String hypernyms) {

		String[] thisLine; // local variables for storing strings returned by
		// split()
		String[] thisSynset;

		// construct synset/nouns HashMap, and synsets arraylist
		nouns = new SeparateChainingHashST<String, Bag<Integer>>();
		In in = new In(synsets);
		int counter = 0;
		while (in.hasNextLine()) {
			thisLine = in.readLine().split(",");
			int v = Integer.parseInt(thisLine[0]);
			this.synsets.add(thisLine[1]);
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
		// check that digraph has only one root
		int rootVertex = 0;
		for (int v = 0; v < G.V(); v++)
			if (!G.adj(v).iterator().hasNext())
				rootVertex++;
		if (rootVertex > 1)
			throw new java.lang.IllegalArgumentException("there were" + rootVertex);
		// check that graph has no cycles with directed cycle class
		dc = new DirectedCycle(G);
		if (dc.hasCycle())
			throw new java.lang.IllegalArgumentException();

		// construct shortest ancestral path searcher with hypernyms digraph
		sap = new SAP(G);

	}

	// the set of nouns (no duplicates), returned as an Iterable
	public Iterable<String> nouns() {
		return nouns.keys();
	}

	// is the word a WordNet noun?
	public boolean isNoun(String word) {
		return nouns.contains(word);
	}

	// distance between nounA and nounB (defined below)
	public int distance(String nounA, String nounB) {
		checkNoun(nounA);
		checkNoun(nounA);
		return sap.length(nouns.get(nounA), nouns.get(nounB));
	}

	// a synset (second field of synsets.txt) that is the common ancestor of
	// nounA and nounB, in a shortest ancestral path (defined below)
	public String sap(String nounA, String nounB) {
		checkNoun(nounA);
		checkNoun(nounA);
		int sysnetID = sap.ancestor(nouns.get(nounA), nouns.get(nounB));
		return synsets.get(sysnetID);
	}

	private void checkNoun(String noun) {
		if (!isNoun(noun))
			throw new java.lang.IllegalArgumentException();
	}

	// for unit testing of this class
	public static void main(String[] args) {
		WordNet wordnet = new WordNet(args[0], args[1]);
		while (!StdIn.isEmpty()) {
			String nounA = StdIn.readString();
			String nounB = StdIn.readString();
			int length = wordnet.distance(nounA, nounB);
			String ancestor = wordnet.sap(nounA, nounB);
			StdOut.println("ancestor synset          :  " + ancestor);
			StdOut.println("length of ancestral path :  " + length);
		}
	}

	// public static void main(String[] args) {
	// In in = new In(args[0]);
	// Digraph G = new Digraph(in);
	// SAP sap = new SAP(G);
	// while (!StdIn.isEmpty()) {
	// int v = StdIn.readInt();
	// int w = StdIn.readInt();
	// int length = sap.length(v, w);
	// int ancestor = sap.ancestor(v, w);
	// StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
	// }
	// }

}
