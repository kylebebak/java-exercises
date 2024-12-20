/**
 * Will compute up to the 46th integer in the sequence before overflow. I could
 * do this program with the XP integer types I defined for RSA encryption, and
 * then I could go to an arbitrary number of digits
 */
public class FibonacciVoid {

	private static int[] M; // memory array that allows for reuse of results

	public static void fib(int n) {
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
		fibHelper(n);
	}

	private static void fibHelper(int n) {
		
		if (n == 0 || n == 1)
			return;
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
		StdOut.println("f" + n + " : " + M[n]);
	}
	
	public static void fib2(int n) {
		if (n < 0)
			throw new IllegalArgumentException("Input must be positive");
		
		M = new int[n + 1];
		M[0] = 0;
		M[1] = 1;
		StdOut.println("f0 : " + M[0]);
		StdOut.println("f1 : " + M[1]);
		
		for (int i = 2; i <= n; i++) {
			M[i] = M[i - 1] + M[i - 2];
			StdOut.println("f" + i + " : " + M[i]);
		}
	}

	public static void main(String[] args) {
		
		int n = Integer.parseInt(args[0]);
		FibonacciVoid.fib(n);
		
		StdOut.println();
		FibonacciVoid.fib2(n);
	}

}
