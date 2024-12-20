public class Outcast {

	private WordNet wordnet;

	// constructor takes a WordNet object
	public Outcast(WordNet wordnet) {
		this.wordnet = wordnet;
	}

	// given an array of n WordNet nouns, return an outcast
	public String outcast(String[] nouns) {
		// iterate through each noun pair and compute distances, find the
		// total summed distance of each noun from all other nouns in the input
		String outcast = null;
		int[] totalDistance = new int[nouns.length];
		int thisDistance = 0;
		for (int i = 0; i < nouns.length; i++)
			for (int j = i + 1; j < nouns.length; j++) {
				thisDistance = wordnet.distance(nouns[i], nouns[j]);
				totalDistance[i] += thisDistance;
				totalDistance[j] += thisDistance;
			}
		int maxDistance = 0;
		for (int i = 0; i < nouns.length; i++)
			if (totalDistance[i] > maxDistance) {
				maxDistance = totalDistance[i];
				outcast = nouns[i];
			}
		// for (int i = 0; i < nouns.length; i++) {
		// StdOut.println(nouns[i] + " has total distance of "
		// + totalDistance[i]);
		// StdOut.println();
		// }
		return outcast;
	}

	// for unit testing of this class
	public static void main(String[] args) {
		WordNet wordnet = new WordNet(args[0], args[1]);
		Outcast outcast = new Outcast(wordnet);
		for (int t = 2; t < args.length; t++) {
			String[] nouns = In.readStrings(args[t]);
			StdOut.println(args[t] + ": " + outcast.outcast(nouns));
		}
	}

}
