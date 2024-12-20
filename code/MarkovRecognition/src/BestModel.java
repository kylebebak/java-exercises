import java.util.TreeMap;

/**
 * 
 * @author kylebebak
 *         <p>
 * 
 *         This is an attribution or recognition program, for attributing an
 *         input to one of two sources. The attribution is done by creating a
 *         Markov model for each of the sources, and then computing the
 *         probabilities with which each model could have generated the input,
 *         and attributing the input to the higher probability source
 *         <p>
 * 
 *         For English text, it works most effectively when an order K of 5 or 6
 *         is used.
 */
public class BestModel {

	private final static int N = 10; // largest N differences between the two
										// models for a given prefix + char
	private static TreeMap<Double, String> ts;

	public static void main(String[] args) {
		if (args.length < 4)
			throw new IllegalArgumentException(
					"Usage : args[0] = order of markov model, "
							+ "args[1] and args[2] are text files used to build models, "
							+ "args[3] and above are files to be analyzed");
		int K = Integer.parseInt(args[0]); // order of markov model

		In in = new In(args[1]);
		MarkovModel m1 = new MarkovModel(K, in.readAll());
		in = new In(args[2]);
		MarkovModel m2 = new MarkovModel(K, in.readAll());

		StdOut.println("Model 1 : " + args[1]);
		StdOut.println("Model 2 : " + args[2]);

		for (int j = 3; j < args.length; j++) {
			in = new In(args[j]);
			String s = in.readAll();
			// StdOut.println(s);
			if (s.length() < K)
				throw new IllegalArgumentException(
						"The input message length must not be less than the order of the Markov model");

			float likelihood1 = 0; // log(probability) that input was produced
									// by given markov model
			float likelihood2 = 0;
			double d1;
			double d2;
			double diff;
			String p;
			ts = new TreeMap<Double, String>();

			for (int i = 0; i + K < s.length(); i++) {
				p = s.substring(i, i + K);

				d1 = Math.log(m1.prob(p, s.charAt(i + K)));
				d2 = Math.log(m2.prob(p, s.charAt(i + K)));
				diff = Math.abs(d1 - d2);
				if (ts.size() < N)
					ts.put(diff, p + s.charAt(i + K));
				else {
					if (diff > ts.firstKey()) {
						ts.pollFirstEntry();
						ts.put(diff, p + s.charAt(i + K));
					}
				}

				likelihood1 += d1;
				likelihood2 += d2;
			}

			// for last K prefixes wrap back to beginning of string
			for (int i = 0; i < K; i++) {
				p = s.substring(s.length() - K + i) + s.substring(0, i);

				d1 = Math.log(m1.prob(p, s.charAt(i)));
				d2 = Math.log(m2.prob(p, s.charAt(i)));
				diff = Math.abs(d1 - d2);
				if (ts.size() < N)
					ts.put(diff, p + s.charAt(i));
				else {
					if (diff > ts.firstKey()) {
						ts.pollFirstEntry();
						ts.put(diff, p + s.charAt(i));
					}
				}
				
				likelihood1 += d1;
				likelihood2 += d2;
			}

			// divide likelihoods by number of characters in string
			likelihood1 = likelihood1 / (float) s.length();
			likelihood2 = likelihood2 / (float) s.length();

			StdOut.println();
			StdOut.println(args[j] + "  " + likelihood1 + "  " + likelihood2
					+ "  " + Math.abs(likelihood1 - likelihood2));
			if (likelihood1 > likelihood2)
				StdOut.println("Input more similar to model 1");
			else if (likelihood1 < likelihood2)
				StdOut.println("Input more similar to model 2");
			else
				StdOut.println("Input equally similar to both models");
			for (Double d : ts.keySet())
				StdOut.println('"' + ts.get(d) + '"' + " " + d);
		}
	}
}
