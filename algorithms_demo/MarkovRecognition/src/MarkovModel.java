import java.util.HashMap;
import java.util.HashSet;

public class MarkovModel {

	private int K; // order of markov model
	private String s; // string input to markov model
	HashMap<String, Markov> prefixes; // K character prefixes in input,
										// construct one markov object for each
	HashSet<Character> alphabet; // distinct chars in input
	int R; // size of alphabet

	public MarkovModel(int K, String s) {
		this.K = K;
		this.s = s;
		if (s.length() < K)
			throw new IllegalArgumentException(
					"The input message length must not be less than the order of the Markov model");
		prefixes = new HashMap<String, Markov>();
		alphabet = new HashSet<Character>();

		Markov m;
		String ss;
		for (int i = 0; i + K < s.length(); i++) {
			alphabet.add(s.charAt(i));
			ss = s.substring(i, i + K);

			if (!prefixes.containsKey(ss))
				m = new Markov(ss);
			else
				m = prefixes.get(ss);

			m.add(s.charAt(i + K));
			prefixes.put(ss, m);
		}

		/**
		 * Markov objects for last K prefixes wrap back to beginning of message.
		 */
		for (int i = 0; i < K; i++) {
			alphabet.add(s.charAt(s.length() - K + i));
			ss = s.substring(s.length() - K + i) + s.substring(0, i);

			if (!prefixes.containsKey(ss))
				m = new Markov(ss);
			else
				m = prefixes.get(ss);

			m.add(s.charAt(i));
			prefixes.put(ss, m);

		}

		R = alphabet.size();
	}

	/**
	 * Probability of c following prefix p : (N(pác) + 1) / (N(p) + R)
	 */
	public float prob(String p, char c) {
		if (!prefixes.containsKey(p))
			return 1 / (float) R;
		return prefixes.get(p).prob(c, R);
	}

	public String toString() {
		return "Order " + K + " markov model for the following input : " + s;
	}

}
