
public class testOverflow {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		double inf = Double.POSITIVE_INFINITY;
		StdOut.println(inf);
		inf += 10;
		StdOut.println(inf);
		StdOut.println(inf > 1000000000);

	}

}
