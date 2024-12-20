import java.util.Arrays;

public class CoinSumsPrint {

	private static int count = 0;

	public static int sums(int[] coins, int total) {

		for (int i = 0; i < coins.length; i++)
			if (coins[i] <= 0)
				throw new IllegalArgumentException(
						"All coins must have positive integer values");

		count = 0;
		Arrays.sort(coins);
		StringBuilder sb = new StringBuilder();

		sumsHelper(coins, total, sb);
		return count;

	}

	private static void sumsHelper(int[] coins, int total, StringBuilder sb) {
		int c = coins[coins.length - 1];

		int n = total / c;
		StringBuilder sbc;

		for (int i = 0; i <= n; i++) {
			int newTotal = total - i * c;
			sbc = new StringBuilder(sb);
			sbc.append(c + "c : " + i + "  -->  ");

			int[] newCoins = new int[coins.length - 1];
			for (int j = 0; j < coins.length - 1; j++)
				newCoins[j] = coins[j];

			if (newCoins.length == 1) {
				if (newTotal % newCoins[0] == 0) {
					count++;
					sbc.append(newCoins[0] + "c : " + newTotal / newCoins[0]);
					StdOut.println(sbc.toString());
				}
			} else
				sumsHelper(newCoins, newTotal, sbc);
		}

	}

	public static void main(String[] args) {

		if (args.length < 2)
			throw new IllegalArgumentException(
					"Command line input must consist of at least one coin value and a value for the sum");
		
		int[] coins = new int[args.length - 1];
		for (int i = 0; i < args.length - 1; i++)
			coins[i] = Integer.parseInt(args[i]);
		int total = Integer.parseInt(args[args.length - 1]);
		StdOut.println("number of ways : "
				+ CoinSumsPrint.sums(coins, total));

	}

}
