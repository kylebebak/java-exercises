import java.util.HashMap;

public class Markov {

	private String ss;
	private int count = 0; // number of times prefix has appeared in text
	private HashMap<Character, Integer> suffixes;
	private boolean updated; // has the Markov object been updated since the
								// last time a char was added?
	char[] chars;
	int[] charCumulus;

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
		updated = false;
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
	 * Return a random suffix char with probability determined by suffix char
	 * frequencies
	 */
	public char random() {
		if (!updated)
			update(); // recompute char cumulus array if a char has been added
						// since the last time random was called
		
		char letter = 's';
		int l = (int) (Math.random() * charCumulus[charCumulus.length - 1]);
		for (int c = 0; c < charCumulus.length; c++)
			if (l < charCumulus[c]) {
				letter = chars[c];
				break;
			}
		
		return letter;
	}

	private void update() {
		updated = true;
		chars = new char[suffixes.size()];
		charCumulus = new int[suffixes.size()];
		
		int i = 0;
		for (char c : suffixes.keySet()) {
			chars[i] = c;
			if (i == 0)
				charCumulus[i] = c;
			else
				charCumulus[i] = charCumulus[i - 1] + chars[i];
			i++;
		}
	}
}
