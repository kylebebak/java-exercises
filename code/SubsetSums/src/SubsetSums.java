import java.util.Collections;
import java.util.Iterator;
import java.util.TreeSet;

/*************************************************************************
 * http://www.cs.princeton.edu/courses/archive/spring03/cs226/assignments/subset
 * .html
 * 
 * COS 226 Programming Assignment 9
 * 
 * Consider the square roots of the numbers from 1 to N. Write a program to
 * divide them into two sets A and B having the property that the sum of the
 * numbers in set A is as close as possible to the sum of the numbers in set B.
 * 
 * Use double-precision floating-point numbers in calculating the square roots
 * and the subset sums.
 *************************************************************************/

public class SubsetSums {

	private int N;
	private TreeSet<Double> A;
	private TreeSet<Double> B;
	private Double[] a;
	private Double[] b;
	private double sa, sb; // sum of numbers in set A and set B
	private double nd, d; // d = sa - sb, compared with new difference nd to
							// check if element should be switched from A to B
	private final double epsilon = .00000000000001;

	// for elements two be switched, abs(nd) must be less than abs(d) - epsilon,
	// because comparisons with double values that are very close to being equal
	// often fail, which can cause the program to get stuck in a while loop, and
	// this is a guarantee against that error

	public SubsetSums(int N) {
		this.N = N;
		A = new TreeSet<Double>(Collections.reverseOrder());
		B = new TreeSet<Double>(Collections.reverseOrder());
		sa = 0;
		sb = 0;
		// populate TreeSets
		for (int i = N; i > 0; i--) {
			double toAdd = Math.sqrt(i);
			if (sa > sb) {
				B.add(toAdd);
				sb += toAdd;
			} else {
				A.add(Math.sqrt(i));
				sa += toAdd;
			}
		}

		// try switching one element for one element
		while (true) {
			d = sa - sb;
			StdOut.println(d);
			if (Math.abs(d) < epsilon)
				break;

			a = A.toArray(new Double[0]);
			b = B.toArray(new Double[0]);

			if (sa < sb) {
				if (!oneOneSwitch(a, b))
					break;
			} else if (!oneOneSwitch(b, a))
				break;
		}

		// try switching two elements for one element
		while (true) {
			d = sa - sb;
			StdOut.println(d);
			if (Math.abs(d) < epsilon)
				break;

			a = A.toArray(new Double[0]);
			b = B.toArray(new Double[0]);

			if (sa < sb) {
				if (!twoOneSwitch(a, b, sa, sb))
					if (!twoOneSwitch(b, a, sb, sa))
						break;
			} else {
				if (!twoOneSwitch(b, a, sb, sa))
					if (!twoOneSwitch(a, b, sa, sb))
						break;
			}
		}

		// try switching two elements for two elements
		while (true) {
			d = sa - sb;
			StdOut.println(d);
			if (Math.abs(d) < epsilon)
				break;

			a = A.toArray(new Double[0]);
			b = B.toArray(new Double[0]);

			if (sa < sb) {
				if (!twoTwoSwitch(a, b, sa, sb))
					if (!twoTwoSwitch(b, a, sb, sa))
						break;
			} else {
				if (!twoTwoSwitch(b, a, sb, sa))
					if (!twoTwoSwitch(a, b, sa, sb))
						break;
			}
		}

		// try switching three elements for two elements
		while (true) {
			d = sa - sb;
			StdOut.println(d);
			if (Math.abs(d) < epsilon)
				break;

			a = A.toArray(new Double[0]);
			b = B.toArray(new Double[0]);

			if (sa < sb) {
				if (!threeTwoSwitch(a, b, sa, sb))
					if (!threeTwoSwitch(b, a, sb, sa))
						break;
			} else {
				if (!threeTwoSwitch(b, a, sb, sa))
					if (!threeTwoSwitch(a, b, sa, sb))
						break;
			}
		}

		// try switching three elements for three elements
		while (true) {
			d = sa - sb;
			StdOut.println(d);
			if (Math.abs(d) < epsilon)
				break;

			a = A.toArray(new Double[0]);
			b = B.toArray(new Double[0]);

			if (sa < sb) {
				if (!threeThreeSwitch(a, b, sa, sb))
					if (!threeThreeSwitch(b, a, sb, sa))
						break;
			} else {
				if (!threeThreeSwitch(b, a, sb, sa))
					if (!threeThreeSwitch(a, b, sa, sb))
						break;
			}
		}

	}

	private boolean oneOneSwitch(Double[] a, Double[] b) {
		// this is the only private switch method that doesn't use
		// binaryDifferenceSearch because bDS doesn't improve performance
		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < b.length; j++) {

				if (a[i] > b[j])
					break;

				nd = Math.abs(sa - sb - 2 * a[i] + 2 * b[j]);
				if (nd < Math.abs(d)) {
					if (a == this.a) {
						// shallow pointer comparison on array passed to
						// oneSwitch with instance variable Double array "a"
						A.remove(a[i]);
						B.remove(b[j]);
						A.add(b[j]);
						B.add(a[i]);
						sa = sa - a[i] + b[j];
						sb = sb + a[i] - b[j];
					} else {
						B.remove(a[i]);
						A.remove(b[j]);
						B.add(b[j]);
						A.add(a[i]);
						sa = sa + a[i] - b[j];
						sb = sb - a[i] + b[j];
					}
					return true;
				}
			}
		}
		return false;
	}

	private boolean twoOneSwitch(Double[] a, Double[] b, double suma,
			double sumb) {

		for (int i = 0; i < a.length; i++) {
			for (int j = i + 1; j < a.length; j++) {
				double t = a[i] + a[j];
				int searchResult = binaryDifferenceSearch(b, t, suma, sumb);

				if (searchResult != -1) {
					if (a == this.a) {
						A.remove(a[i]);
						A.remove(a[j]);
						B.remove(b[searchResult]);
						A.add(b[searchResult]);
						B.add(a[i]);
						B.add(a[j]);
						sa = sa - t + b[searchResult];
						sb = sb + t - b[searchResult];
					} else {
						B.remove(a[i]);
						B.remove(a[j]);
						A.remove(b[searchResult]);
						B.add(b[searchResult]);
						A.add(a[i]);
						A.add(a[j]);
						sa = sa + t - b[searchResult];
						sb = sb - t + b[searchResult];
					}
					return true;
				}
			}
		}
		return false;
	}

	private boolean twoTwoSwitch(Double[] a, Double[] b, double suma,
			double sumb) {

		for (int i = 0; i < a.length; i++) {
			for (int j = i + 1; j < a.length; j++) {
				for (int k = 0; k < b.length; k++) {
					double t = a[i] + a[j] - b[k];
					int searchResult = binaryDifferenceSearch(b, t, suma, sumb);

					if (searchResult != -1 && b[k] != b[searchResult]) {
						// don't switch two elements from a for 2 times an
						// element in b
						if (a == this.a) {
							A.remove(a[i]);
							A.remove(a[j]);
							B.remove(b[k]);
							B.remove(b[searchResult]);
							A.add(b[k]);
							A.add(b[searchResult]);
							B.add(a[i]);
							B.add(a[j]);
							sa = sa - t + b[searchResult];
							sb = sb + t - b[searchResult];
						} else {
							B.remove(a[i]);
							B.remove(a[j]);
							A.remove(b[k]);
							A.remove(b[searchResult]);
							B.add(b[k]);
							B.add(b[searchResult]);
							A.add(a[i]);
							A.add(a[j]);
							sa = sa + t - b[searchResult];
							sb = sb - t + b[searchResult];
						}
						return true;
					}
				}
			}
		}
		return false;
	}

	private boolean threeTwoSwitch(Double[] a, Double[] b, double suma,
			double sumb) {

		for (int i = 0; i < a.length; i++) {
			for (int j = i + 1; j < a.length; j++) {
				for (int l = j + 1; l < a.length; l++) {
					for (int k = 0; k < b.length; k++) {
						double t = a[i] + a[j] + a[l] - b[k];
						int searchResult = binaryDifferenceSearch(b, t, suma,
								sumb);

						if (searchResult != -1 && b[k] != b[searchResult]) {
							// don't switch two elements from a for 2 times an
							// element in b
							if (a == this.a) {
								A.remove(a[i]);
								A.remove(a[j]);
								A.remove(a[l]);
								B.remove(b[k]);
								B.remove(b[searchResult]);
								A.add(b[k]);
								A.add(b[searchResult]);
								B.add(a[i]);
								B.add(a[j]);
								B.add(a[l]);
								sa = sa - t + b[searchResult];
								sb = sb + t - b[searchResult];
							} else {
								B.remove(a[i]);
								B.remove(a[j]);
								B.remove(a[l]);
								A.remove(b[k]);
								A.remove(b[searchResult]);
								B.add(b[k]);
								B.add(b[searchResult]);
								A.add(a[i]);
								A.add(a[j]);
								A.add(a[l]);
								sa = sa + t - b[searchResult];
								sb = sb - t + b[searchResult];
							}
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	private boolean threeThreeSwitch(Double[] a, Double[] b, double suma,
			double sumb) {

		for (int i = 0; i < a.length; i++) {
			for (int j = i + 1; j < a.length; j++) {
				for (int l = j + 1; l < a.length; l++) {
					for (int k = 0; k < b.length; k++) {
						for (int m = k + 1; m < b.length; m++) {
							double t = a[i] + a[j] + a[l] - b[k] - b[m];
							int searchResult = binaryDifferenceSearch(b, t,
									suma, sumb);

							if (searchResult != -1 && b[k] != b[searchResult]
									&& b[m] != b[searchResult]) {
								if (a == this.a) {
									A.remove(a[i]);
									A.remove(a[j]);
									A.remove(a[l]);
									B.remove(b[k]);
									B.remove(b[m]);
									B.remove(b[searchResult]);
									A.add(b[k]);
									A.add(b[m]);
									A.add(b[searchResult]);
									B.add(a[i]);
									B.add(a[j]);
									B.add(a[l]);
									sa = sa - t + b[searchResult];
									sb = sb + t - b[searchResult];
								} else {
									B.remove(a[i]);
									B.remove(a[j]);
									B.remove(a[l]);
									A.remove(b[k]);
									A.remove(b[m]);
									A.remove(b[searchResult]);
									B.add(b[k]);
									B.add(b[m]);
									B.add(b[searchResult]);
									A.add(a[i]);
									A.add(a[j]);
									A.add(a[l]);
									sa = sa + t - b[searchResult];
									sb = sb - t + b[searchResult];
								}
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}

	/**
	 * @param
	 * @return returns the index of element in b that would lessen the sum
	 *         difference of the sets, or -1 if no such element exists
	 */
	private int binaryDifferenceSearch(Double[] b, double target, double suma,
			double sumb) {
		int nLow = 0;
		int nHigh = b.length - 1;
		int n;
		while (true) {
			n = (nLow + nHigh) / 2;
			nd = suma - sumb - 2 * target + 2 * b[n];
			if (Math.abs(nd) < Math.abs(d) - epsilon)
				return n;
			if (nd > d)
				nLow = n + 1;
			else
				nHigh = n - 1;

			if (nHigh < nLow)
				return -1;
		}
	}

	public double difference() {
		return d;
	}

	public int inA() {
		return A.size();
	}

	public int inB() {
		return B.size();
	}

	public Iterable<Integer> setA() {
		Queue<Integer> setA = new Queue<Integer>();
		for (Double d : A)
			setA.enqueue((int) Math.round(d * d));
		return setA;
	}

	public Iterable<Integer> setB() {
		Queue<Integer> setB = new Queue<Integer>();
		for (Double d : B)
			setB.enqueue((int) Math.round(d * d));
		return setB;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// SubsetSums ss = new SubsetSums(Integer.parseInt(args[0]));
		SubsetSums ss = new SubsetSums(150);
		StdOut.println();
		StdOut.println("difference between subset A and subset B = " + ss.difference());
		StdOut.println("A has " + ss.inA() + " elements and a sum of " + ss.sa
				+ ", B has " + ss.inB() + " elements and a sum of " + ss.sb);
		StdOut.println();
		for (int i : ss.setA())
			StdOut.print(i + "   ");
		StdOut.println();
		for (int i : ss.setB())
			StdOut.print(i + "   ");

	}

}
