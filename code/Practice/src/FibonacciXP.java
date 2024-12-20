/**
 * Uses XP integer types I wrote for RSA encryption, and goes to an arbitrary
 * term of the sequence, as long as XP is modified to allow for a sufficient
 * number of digits. A rule of thumb is that the result increases by a power of
 * 10 for about every 5 extra terms in the sequence, so if you want the first
 * 1000 terms of the series, you will need to set the field D in XP to have a
 * value of at least 105 or so (which means there are about 210 digits
 * allocated). Any fewer results in overflow for XP which causes an exception
 * 
 * Usage : java -Xmx2g FibonacciXP 20000 > fibonacci_20k.txt
 */
public class FibonacciXP {

	private static XP[] M; // memory array that allows for reuse of results
	private static int N;

	public static void fib(int n) {
		if (n < 1)
			throw new IllegalArgumentException("Input must be positive");
		N = n;
		M = new XP[n + 1];

		// initialize memory array with M[0] = 0, M[1] = 1, and all other
		// entries are null (this value flags them as having not been computed)
		M[0] = new XP("0");
		M[1] = new XP("1");

		// call recursive helper method that can see newly initialized M array
		fibHelper(n);
	}

	private static void fibHelper(int n) {

		if (n == 0 || n == 1)
			return;

		// recurse down n - 2 subtree first, call this left subtree. ONLY
		// RECURSE if M[n - 2] hasn't been computed, this is memoization
		if (M[n - 2] == null)
			fibHelper(n - 2);

		// now recurse down n - 1 subtree, call this right subtree
		if (M[n - 1] == null)
			fibHelper(n - 1);

		// if both M[n - 1] and M[n - 2] have already been computed, set M[n] to
		// their sum and return it
		M[n] = XP.add(M[n - 1], M[n - 2]);
		StdOut.println();
		StdOut.println("f" + n + " : " + M[n].toString());
	}

	public static void main(String[] args) {
		
		int n = Integer.parseInt(args[0]);
		FibonacciXP.fib(n);
	}

}
