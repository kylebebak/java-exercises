/**
 * Will compute up to the 46th integer in the sequence before overflow. I could
 * do this program with the XP integer types I defined for RSA encryption, and
 * then I could go to an arbitrary number of digits
 */
public class FibonacciDynamic {

	private static int[] M; // memory array that allows for reuse of results

	public static int fib(int n) {
		if (n < 1)
			throw new IllegalArgumentException("Input must be positive");
		M = new int[n + 1];

		// initialize memory array with M[0] = 0, M[1] = 1, and all other
		// entries set to -1 (this value flags them as having not been computed)
		for (int i = 0; i < M.length; i++)
			M[i] = -1;
		M[0] = 0;
		M[1] = 1;

		// call recursive helper method that can see newly initialized M array
		return fibHelper(n);
	}

	private static int fibHelper(int n) {

		if (n == 0)
			return M[0];

		if (n == 1)
			return M[1];

		// recurse down n - 2 subtree first, call this left subtree. ONLY
		// RECURSE if M[n - 2] hasn't been computed, this is memoization
		if (M[n - 2] == -1)
			fibHelper(n - 2);

		// now recurse down n - 1 subtree, call this right subtree
		if (M[n - 1] == -1)
			fibHelper(n - 1);

		// if both M[n - 1] and M[n - 2] have already been computed, set M[n] to
		// their sum and return it
		M[n] = M[n - 1] + M[n - 2];
		StdOut.println(M[n]);
		return M[n];
	}

	public static void main(String[] args) {
		
		int n = Integer.parseInt(args[0]);
		StdOut.println("result " + FibonacciDynamic.fib(n));
	}

}
