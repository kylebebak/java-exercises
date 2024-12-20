import java.util.HashMap;

public class TextGenerator {

	public static void main(String[] args) {
		int K = Integer.parseInt(args[0]); // order of markov model
		int M = Integer.parseInt(args[1]); // length of message to be generated
		if (M < K)
			throw new IllegalArgumentException(
					"The output message length must not be less than the order of the Markov model");
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

		/**
		 * Now generate text and send it to the standard output stream, or just
		 * print it to the console. The first K characters of the text will be
		 * the same as the input message, and then the Markov model will take
		 * over to generate the characters that follow;
		 */
		StringBuilder sb = new StringBuilder();
		for (int c = 0; c < K; c++)
			sb.append(s.charAt(c));
		for (int c = K; c < M; c++)
			sb.append(prefixes.get(sb.substring(c - K, c)).random());
		
		StdOut.println(sb.toString());

	}
}
