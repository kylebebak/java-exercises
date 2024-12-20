public class BinomialDynamic {

	public static int[][] binomial;

	public static void nChooseK(int N, int K) {

		binomial = new int[N + 1][N + 1];
		for (int k = 1; k <= N; k++)
			binomial[0][k] = 0;
		for (int n = 0; n <= N; n++)
			binomial[n][0] = 1;

		StdOut.print(binomial[0][0]);
		for (int n = 1; n <= N; n++) {
			
			StdOut.println();
			StdOut.print(binomial[n][0]);
			
			for (int k = 1; k <= N; k++) {
				binomial[n][k] = binomial[n - 1][k - 1] + binomial[n - 1][k];
				
				if (k <= n)
					StdOut.print("  " + binomial[n][k]);
			}
		}
		
		StdOut.println();
		StdOut.print(N + " choose " + K + " = " + binomial[N][K]);
	}

	public static void main(String[] args) {

		nChooseK(25, 10);
	}
}
