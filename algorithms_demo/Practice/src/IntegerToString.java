import java.util.HashMap;

/**
 * This class does just what it sounds like. All its methods are static, and the
 * hash maps that make the conversions possible are initialized statically
 * outside of any constructor. Its method is essentially recursive.
 *
 * Usage: java IntegerToString [integer]
 */
public class IntegerToString {

	// all of these only have to be initialized once, when IntegerToString is
	// first called
	private static final String[] nineteen = { "", "one", "two", "three",
			"four", "five", "six", "seven", "eight", "nine", "ten", "eleven",
			"twelve", "thirteen", "fourteen", "fifteen", "sixteen",
			"seventeen", "eighteen", "nineteen" };
	private static final String[] ninety = { "", "", "twenty", "thirty",
			"forty", "fifty", "sixty", "seventy", "eighty", "ninety" };
	private static final String[] ninehundred = { "", "one hundred",
			"two hundred", "three hundred", "four hundred", "five hundred",
			"six hundred", "seven hundred", "eight hundred", "nine hundred" };

	private static String[] bigStrings = { "", "thousand", "million",
			"billion", "trillion", "quadrillion", "quintillion", "sextillion",
			"septillion", "octillion", "nonillion" };

	private static final HashMap<Integer, String> one;
	private static final HashMap<Integer, String> ten;
	private static final HashMap<Integer, String> hundred;

	private static HashMap<Integer, String> big;

	// the static modifier allows me to initialize these static final variables
	// without calling any of the class methods
	static {
		one = new HashMap<Integer, String>();
		for (int i = 0; i < nineteen.length; i++)
			one.put(i, nineteen[i]);

		ten = new HashMap<Integer, String>();
		for (int i = 0; i < ninety.length; i++)
			ten.put(i, ninety[i]);

		hundred = new HashMap<Integer, String>();
		for (int i = 0; i < ninehundred.length; i++)
			hundred.put(i, ninehundred[i]);

		big = new HashMap<Integer, String>();
		for (int i = 0; i < bigStrings.length; i++)
			big.put(i, bigStrings[i]);
	}
	
	/**
	 * This method excepts an integer and converts it to an English string,
	 * complete with commas.
	 * <p>
	 * Integer inputs larger than <b>Integer.MAX_VALUE (2^31 - 1)</b> will throw
	 * an exception
	 */
	public static String toEnglish(int n) {
		if (n > Integer.MAX_VALUE || n < Integer.MIN_VALUE)
			throw new IllegalArgumentException("Input integer overflow");

		return toEnglish(Integer.toString(n));
	}

	/**
	 * This method excepts a string integer and converts it to an English
	 * string, complete with commas.
	 */
	public static String toEnglish(String sn) {

		// input string validation
		int L = sn.length();
		if (sn.equals("0"))
			return "zero";
		if (sn.charAt(0) == '-')
			return "negative " + toEnglish(sn.substring(1));
		if (L > 1)
			if (sn.charAt(0) == '0')
				throw new IllegalArgumentException(
						"Input string can't have leading zeros");
		if (L > 3 * bigStrings.length)
			throw new IllegalArgumentException("Input string overflow");

		StringBuilder sb = new StringBuilder();
		int b = (L - 1) / 3; // number of values retrieved from big hash map,
								// one for each power of 1000 that divides input
		int offset = L % 3;
		if (offset == 0)
			offset = 3;

		// call helper function to build string 3 digits at a time
		toEnglish(sn.substring(0, L - b * 3), b, sb);
		for (int i = 0; i < b; i++)
			toEnglish(sn.substring(offset + i * 3, offset + (i + 1) * 3), b - 1
					- i, sb);

		// remove trailing whitespace and possible commas
		if (sb.charAt(sb.length() - 1) == ' ')
			sb.deleteCharAt(sb.length() - 1);
		if (sb.charAt(sb.length() - 1) == ',')
			sb.deleteCharAt(sb.length() - 1);

		return sb.toString();
	}

	/**
	 * Helper function builds string representation of input 3 digits at a time
	 */
	private static void toEnglish(String s, int b, StringBuilder sb) {
		int L = s.length();

		if (L == 3) {
			if (s.charAt(0) != '0')
				sb.append(hundred.get(Integer.parseInt(s.substring(0, 1)))
						+ " ");
			if (s.charAt(1) == '1') {
				sb.append(one.get(Integer.parseInt(s.substring(1, 3))) + " ");
			} else {
				if (s.charAt(1) != '0')
					sb.append(ten.get(Integer.parseInt(s.substring(1, 2)))
							+ " ");
				if (s.charAt(2) != '0')
					sb.append(one.get(Integer.parseInt(s.substring(2, 3)))
							+ " ");
			}
		}

		if (L == 2) {
			if (s.charAt(0) == '1') {
				sb.append(one.get(Integer.parseInt(s)) + " ");
			} else {
				sb.append(ten.get(Integer.parseInt(s.substring(0, 1))) + " ");
				if (s.charAt(1) != '0')
					sb.append(one.get(Integer.parseInt(s.substring(1, 2)))
							+ " ");
			}
		}

		if (L == 1)
			sb.append(one.get(Integer.parseInt(s)) + " ");
		
		// append space and comma if necessary
		if (b != 0)
			if (L == 3) {
				if (s.charAt(0) != '0' || s.charAt(1) != '0'
						|| s.charAt(2) != '0')
					sb.append(big.get(b) + ", ");
			} else {
				sb.append(big.get(b) + ", ");
			}

	}


	/**
	 * Test client
	 */
	public static void main(String[] args) {

		if (args.length > 1) {
			StdOut.println("Usage:\n java IntegerToString [integer]");
			return;
		}

		if (args.length == 0) {

			StdOut.println(toEnglish("-100123920016893248723839298000328"));
			StdOut.println(toEnglish(-920000));
			StdOut.println(toEnglish(-9200001));
			StdOut.println(toEnglish(920000));
			StdOut.println(toEnglish(92000));
			StdOut.println(toEnglish(8000));
			StdOut.println(toEnglish(920000000));
			StdOut.println(toEnglish("-0"));
			StdOut.println(toEnglish(-0));
		}

		if (args.length == 1) StdOut.println(toEnglish(args[0]));

	}

}
