/**
 * Extended precision integer for <b>EVEN</b> bases. Use R = 10 for encoding
 * decimal numbers. Supports various arithmetic operations. Does not support
 * negative integers
 * <p>
 * 
 * Uses this identity for the RSA function (modular exponentiation)<br>
 * <b>xy mod n = ((x mod n) * (y mod n)) mod n</b>
 * 
 */
public class XP {
	private static final int R = 10; // base or radix
	private static final int D = 300; // max number of digits supported
	private static final int DD = 2 * D + 1; // max number of digits allocated
	private static final XP ZERO = new XP("0");
	private static final XP ONE = new XP("1");
	private static final XP TWO = new XP("2");
	// commonly used constants, static so they only have to be initialized once
	private int[] N;
	private int L; // current length (number of digits) of integer

	/**
	 * Takes a string input and parses it, placing the digits into an array of
	 * ints, with least significant digits first
	 */
	public XP(String num) {
		if (num.length() > D)
			throw new IndexOutOfBoundsException(
					"Overflow Error : Integers with more than " + D
							+ " digits aren't supported");
		N = new int[DD];
		L = num.length();
		for (int d = 0; d < num.length(); d++) {
			String c = new String();
			c += num.charAt(num.length() - 1 - d);
			N[d] = Integer.parseInt(c);
		}
	}

	/**
	 * Copy constructor
	 */
	public XP(XP that) {
		N = that.getIntArray();
		L = that.getLength();
	}

	/**
	 * Helper constructor used in arithmetic operations.
	 */
	private XP(int[] N, int L) {
		this.N = N.clone();
		// clone is a shallow copy except for primitive, one dimensional arrays
		this.L = L;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int d = L - 1; d >= 0; d--)
			sb.append(N[d]);
		return sb.toString();
	}

	public int[] getIntArray() {
		int[] copy = N.clone(); // int array backing XP instance is immutable
		return copy;
	}

	private int[] intArray() {
		// gives static XP methods a pointer to backing int array
		return N;
	}

	public int getDigit(int d) {
		return N[d];
	}

	public int getLength() {
		return L;
	}

	public boolean isOdd() {
		return N[0] % 2 != 0;
	}

	/**
	 * Elementary arithmetic operations (addition, subtraction, multiplication,
	 * division). All of these methods return a new instance of XP
	 */

	/**
	 * Add that number to this number
	 */
	public static XP add(XP a, XP b) {
		int[] sum = new int[DD];

		int[] N1 = a.intArray();
		int[] N2 = b.intArray();
		int L1 = a.getLength();
		int L2 = b.getLength();
		int digits = Math.max(L1, L2);

		for (int d = 0; d < digits; d++) {
			int s = sum[d] + N1[d] + N2[d];
			if (s >= R)
				sum[d + 1] += 1;
			sum[d] = s % R;
		}

		if (sum[digits] != 0)
			digits++;
		return new XP(sum, digits);
	}

	/**
	 * Subtract that number from this number. This number must be >= that number
	 */
	public static XP subtract(XP a, XP b) {
		if (XP.compare(a, b) < 0)
			throw new IllegalArgumentException(
					"The difference of two numbers must be non-negative; negative integers aren't supported");
		int[] diff = new int[DD];

		int[] N1 = a.intArray();
		int[] N2 = b.intArray();
		int L1 = a.getLength();
		int L2 = b.getLength();
		int digits = Math.max(L1, L2);

		for (int d = 0; d < digits; d++) {
			int dd = diff[d] + N1[d] - N2[d];
			if (dd < 0) {
				diff[d + 1] -= 1;
				diff[d] = R - Math.abs(dd);
			} else
				diff[d] = dd;
		}

		while (digits > 1) {
			if (diff[digits - 1] == 0)
				digits--;
			else
				break;
		}
		return new XP(diff, digits);
	}

	/**
	 * Multiply this number by that number. Uses the algorithm taught in grade
	 * school, where the larger number (on top) is multiplied by the smaller one
	 * (on bottom), one digit at a time, and then the results are summed
	 */
	public static XP multiply(XP a, XP b) {
		int[] product = new int[DD];

		int[] big;
		int[] small;
		if (XP.compare(a, b) >= 0) {
			big = a.intArray();
			small = b.intArray();
		} else {
			big = b.intArray();
			small = a.intArray();
		}
		int db = Math.max(a.getLength(), b.getLength());
		int ds = Math.min(a.getLength(), b.getLength());

		int toAdd = 0;
		for (int i = 0; i < ds; i++) {
			for (int j = 0; j <= db; j++) {
				int p = big[j] * small[i] + toAdd;
				product[j + i] += p % R;
				toAdd = p / R;
			}
		}
		// second pass sums ints (there are ds of them) computed in first pass
		for (int d = 0; d < db + ds; d++) {
			product[d + 1] += product[d] / R;
			product[d] = product[d] % R;
		}

		int d = db + ds;
		while (d > 1) {
			if (product[d - 1] == 0)
				d--;
			else
				break;
		}
		return new XP(product, d);
	}

	/**
	 * Divide this number by that number. Uses a recursive algorithm that takes
	 * advantage of the other XP arithmetic operations implemented above.
	 * Returns a 2-element array of XPs, the quotient and the remainder
	 */
	public static XP[] divide(XP a, XP b) {

		if (XP.compare(a, b) < 0)
			return new XP[] { ZERO, a };

		XP[] qr = divide(a, XP.multiply(b, TWO));
		qr[0] = XP.multiply(qr[0], TWO);

		if (XP.compare(qr[1], b) < 0)
			return qr;
		else {
			qr[0] = XP.add(qr[0], ONE);
			qr[1] = XP.subtract(qr[1], b);
			return qr;
		}
	}

	/**
	 * The RSA function (modular exponentiation) takes 3 inputs :<br>
	 * base<br>
	 * exponent<br>
	 * modulo
	 * <p>
	 * It computes a^b mod n efficiently using repeated squaring and the
	 * following identity : <b>xy mod n = ((x mod n) * (y mod n)) mod n</b>
	 */
	public static XP modExp(XP a, XP b, XP n) {
		if (XP.compare(n, a) <= 0 || XP.compare(a, ZERO) < 0
				|| XP.compare(b, ZERO) < 0)
			throw new IllegalArgumentException(
					"a and b must be non-negative, and n must be greater than a");

		if (XP.compare(b, ZERO) == 0)
			return ONE;
		else if (XP.compare(b, ONE) == 0)
			return a;
		else if (!b.isOdd())
			return modExp(XP.divide(XP.multiply(a, a), n)[1],
					XP.divide(b, TWO)[0], n);
		else
			return XP
					.divide(XP.multiply(
							a,
							modExp(XP.divide(XP.multiply(a, a), n)[1],
									XP.divide(XP.subtract(b, ONE), TWO)[0], n)),
							n)[1];
	}

	/**
	 * Helper functions, random number generation is for debugging
	 */

	public static int compare(XP a, XP b) {
		int L1 = a.getLength();
		int L2 = b.getLength();
		if (L1 > L2)
			return 1;
		if (L1 < L2)
			return -1;

		for (int d = L1 - 1; d >= 0; d--) {
			if (a.getDigit(d) > b.getDigit(d))
				return 1;
			if (a.getDigit(d) < b.getDigit(d))
				return -1;
		}
		return 0;
	}

	/**
	 * Return a new XP instance with n pseudo-random digits
	 */
	public static XP rand(int n) {
		int[] newN = new int[DD];
		int newL = n;
		for (int d = 0; d < newL - 1; d++)
			newN[d] = (int) (Math.random() * R);
		newN[newL - 1] = 1 + (int) (Math.random() * (R - 1));
		return new XP(newN, newL);
	}

	/**
	 **************************************************************** 
	 * Test client
	 */

	/**
	 * Usage : args[0] = int_0, args[1] = int_1
	 */
	public static void main(String[] args) {
		if (args.length != 2)
			throw new RuntimeException(
					"Usage : args[0] = int_0, args[1] = int_1");
		XP a = new XP(args[0]);
		XP b = new XP(args[1]);
		StdOut.println("a = " + a.toString() + ", a is odd : " + a.isOdd());
		StdOut.println("b = " + b.toString() + ", b is odd : " + b.isOdd());

		XP sum = XP.add(a, b);
		StdOut.println("a + b = " + sum.toString() + ", and has "
				+ sum.getLength() + " digits");

		XP difference = XP.subtract(a, b);
		StdOut.println("a - b = " + difference.toString() + ", and has "
				+ difference.getLength() + " digits");

		XP product = XP.multiply(a, b);
		StdOut.println("a * b = " + product.toString() + ", and has "
				+ product.getLength() + " digits");

		XP[] qr = XP.divide(a, b);
		StdOut.println("a / b = " + qr[0].toString() + ", R = "
				+ qr[1].toString() + ", quotient has " + qr[0].getLength()
				+ " digits");

		StdOut.println();
		StdOut.println("Modular exponentiation");
		a = XP.rand(150);
		b = XP.rand(180);
		XP n = XP.rand(151);
		StdOut.println("a = " + a.toString());
		StdOut.println("b = " + b.toString());
		StdOut.println("n = " + n.toString());
		XP modExp = XP.modExp(a, b, n);
		StdOut.println("a ^ b mod n = " + modExp.toString() + ", and has "
				+ modExp.getLength() + " digits");
	}
}
