import java.util.HashMap;

public class FrequencyCounter {

	// private static int k; // order of markov model
	// private static String s;

	public static void main(String[] args) {
		int k = Integer.parseInt(args[0]);
		String s = BinaryStdIn.readString();

		HashMap<String, Markov> prefixes = new HashMap<String, Markov>();

		Markov m;
		String ss;
		for (int i = 0; i + k < s.length() - 1; i++) {
			ss = s.substring(i, i + k);
			if (!prefixes.containsKey(ss))
				m = new Markov(ss);
			else
				m = prefixes.get(ss);
			
			m.add(s.charAt(i + k + 1));
			prefixes.put(ss, m);
		}
		
		

	}
}
