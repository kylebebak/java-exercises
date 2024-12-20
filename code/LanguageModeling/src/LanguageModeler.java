import java.util.HashMap;

public class LanguageModeler {

	public static void main(String[] args) {
		int K = Integer.parseInt(args[0]); // order of markov model
		String s = StdIn.readAll(); // string input to markov model
		if (s.length() < K)
			throw new IllegalArgumentException(
					"The input message length must not be less than the order of the Markov model");

		HashMap<String, Markov> prefixes = new HashMap<String, Markov>();

		Markov m;
		String ss;
		for (int i = 0; i + K < s.length(); i++) {
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
		 * this is _absolutely_ necessary for generating text, because there
		 * must be a markov object for any K characters of text that the model
		 * can possibly generate.
		 */
		for (int i = 0; i < K; i++) {
			ss = s.substring(s.length() - K + i) + s.substring(0, i);

			if (!prefixes.containsKey(ss))
				m = new Markov(ss);
			else
				m = prefixes.get(ss);

			m.add(s.charAt(i));
			prefixes.put(ss, m);

		}

		StdOut.println(s);
		StdOut.println(prefixes.size() + " distinct keys");
		for (String p : prefixes.keySet())
			StdOut.println(prefixes.get(p).toString());
	}
}
