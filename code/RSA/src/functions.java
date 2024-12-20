public class functions {

	/**
	 * Compute a^b % n with repeated squaring, modular exponentiation. In the
	 * RSA algorithm, for both encrypting and decrypting, the input for the base
	 * "a" is always less than the modulo "n"
	 */
	public static int modExp(int a, int b, int n) {
		if (b == 0)
			return 1;
		else if (b == 1) {
			return a;
		} else if (b % 2 == 0) {
			StdOut.println(a + ", " + n);
			return modExp( (a * a) % n, b / 2, n);
		}
		else {
			StdOut.println(a + ", " + n);
			return (a * modExp( (a * a) % n, (b - 1) / 2, n)) % n;
		}
	}

	/**
	 * Compute x^n with repeated squaring
	 */
	public static int repeatedSquaring(int x, int n) {
		if (n == 0)
			return 1;
		else if (n == 1)
			return x;
		else if (n % 2 == 0)
			return repeatedSquaring(x * x, n / 2);
		else
			return x * repeatedSquaring(x * x, (n - 1) / 2);
	}
	
	/**
	 * Compute the factorial of an integer using recursion
	 */
	public static int factorial(int n) {
		if (n < 0)
			throw new IllegalArgumentException("Input must be non-negative");
		if (n == 0)
			return 1;
		if (n == 1)
			return n;
		else
			return n * factorial(n - 1);
	}
	
	/**
	 * Input a decimal integer and output the same integer in a new base,
	 * represented as an int array of digits, starting with the least
	 * significant digit
	 */
	public static int[] changeBase(int in, int B) {
		if (B < 2)
			throw new IllegalArgumentException("Base must be >= 2");
		if (B > in)
			return new int[] { in };

		int b = B;
		int digit = 1;
		while (true) {
			if (b * B > in)
				break;
			b *= B;
			digit++;
		}
		
		int[] out = new int[digit + 1];
		int r = in;
		for (int d = digit; d >= 0; d--) {
			out[d] = r / b;
			r = r % b;
			b /= B;
		}
		
		return out;
	}

	public static void main(String[] args) {

		int a = 23425;
		int b = 2563234;
		int n = 41083;
		System.out.println(modExp(a, b, n));
		
		int[] newBase = functions.changeBase(10000, 128);
		for (int i : newBase)
			StdOut.println(i);
	}

}
