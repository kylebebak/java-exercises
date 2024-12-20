import java.util.HashMap;

public class Markov {

	private String ss;
	private int count = 0; // number of times prefix has appeared in text
	private HashMap<Character, Integer> suffixes;

	/**
	 * This is the prefix string for this markov object
	 */
	public Markov(String substring) {
		ss = substring;
		suffixes = new HashMap<Character, Integer>();
	}

	/**
	 * Increment the frequency for some suffix char in the markov object
	 */
	public void add(char c) {
		count++;
		if (!suffixes.containsKey(c))
			suffixes.put(c, 1);
		else
			suffixes.put(c, suffixes.get(c) + 1);
	}

	/**
	 * Prefix frequency, prefix, and suffix chars / suffix char frequency pairs
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(count + " " + ss + " ");
		for (char c : suffixes.keySet())
			sb.append(suffixes.get(c) + " " + c + " ");
		return sb.toString();
	}

	/**
	 * Return Laplace-smoothed probability that char c follows the prefix
	 * represented by this Markov object. Laplace smoothing guarantees that
	 * probabilities can be computed for any k-character prefix, even one that
	 * doesn't appear in the input. It also guarantees that any character that
	 * appears in the input has a non-zero probability of appearing as a suffix
	 * character. Probability of c following prefix p : (N(pác) + 1) / (N(p) + R)
	 */
	public float prob(char c, int R) {
		// R is size of alphabet, i.e. number of distinct chars that appeared in input
		if (!suffixes.containsKey(c))
			return 1 / (float) (count + R); 
		return (suffixes.get(c) + 1) / (float) (count + R);
	}
}
