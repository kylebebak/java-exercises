/**
 * @author kylebebak<br>
 * <br>
 * 
 *         generates a grid of characters, with several options (states) for
 *         choosing the frequency of the characters chosen. the state most
 *         likely to generate english words is the third, which comes from the
 *         following source:<br>
 * <br>
 * 
 *         http://oxforddictionaries.com/words/what-is-the-frequency-of-the-
 *         letters-of-the-alphabet-in-english<br>
 * <br>
 * 
 *         with this state, random letter frequency is the same as the
 *         cumulative letter frequency of the words in the OED
 */

public class GridGenerator {

	// for state 0
	private static final int A = 97;
	private static final int R = 26;

	// for state 1
	private static final char[] vowels = { 'a', 'e', 'i', 'o', 'u' };
	private static final char[] consonants = { 'b', 'c', 'd', 'f', 'g', 'h',
			'j', 'k', 'l', 'm', 'n', 'p', 'q', 'r', 's', 't', 'v', 'w', 'x',
			'y', 'z' };
	private static final double vowelFrequency = .38;
	
	// for state 2
	private static final char[] lettersByFrequency = { 'e', 'a', 'r', 'i', 'o',
			't', 'n', 's', 'l', 'c', 'u', 'd', 'p', 'm', 'h', 'g', 'b', 'f',
			'y', 'w', 'k', 'v', 'x', 'z', 'j', 'q' };
	private static final double[] letterFrequency = { .111607, .084966,
			.075809, .075448, .071635, .069509, .066544, .057351, .054893,
			.045388, .036308, .033844, .031671, .030129, .030034, .024705,
			.020720, .018121, .017779, .012899, .011016, .010074, .002902,
			.002722, .001965, .001962 };
	private static double[] letterFrequencyCumulus = new double[26];

	public static char[][] generateGrid(int N, int state) {
		if (state < 0 || state > 2)
			throw new IllegalArgumentException(
					"state must be an int between 0 and 2, inclusive");
		// generate a grid of chars from 'a' to 'z'. these are the characters
		// corresponding to the integers 97 - 122
		char[][] grid = new char[N][N];

		letterFrequencyCumulus[0] = letterFrequency[0];
		for (int c = 1; c < letterFrequency.length; c++)
			letterFrequencyCumulus[c] = letterFrequencyCumulus[c - 1]
					+ letterFrequency[c];

		for (int j = 0; j < N; j++)
			for (int i = 0; i < N; i++) {

				// any random letter
				if (state == 0)
					grid[j][i] = (char) (A + (int) (Math.random() * R));

				// either a random vowel or a random consonant, with a fixed
				// probability for each set
				if (state == 1) {
					if (Math.random() < vowelFrequency)
						grid[j][i] = vowels[(int) (Math.random() * vowels.length)];
					else
						grid[j][i] = consonants[(int) (Math.random() * consonants.length)];
				}
				
				// letter frequency matches frequency of appearance in the OED
				if (state == 2) {
					double l = Math.random();
					int letter = 0;
					for (int c = 0; c < letterFrequencyCumulus.length; c++)
						if (l < letterFrequencyCumulus[c]) {
							letter = c;
							break;
						}
					grid[j][i] = lettersByFrequency[letter];
				}
			}
		return grid;
	}

}
