/**
 * Execution : <b><i>java RSAA - < input.txt > encrypted.txt</b></i>
 * <p>
 * Let's say Alice wants to securely send Bob a message using the RSA public key
 * cryptosystem. RSA involves three integer parameters d, e, and n that satisfy
 * certain mathematical properties. The private key (d, n) is known only by Bob,
 * while the public key (e, n) is published on the Internet.
 * <p>
 * If Alice wants to send Bob a message (e.g., her credit card number) she
 * encodes her message as an integer M that is between 0 and n-1. Then she
 * computes:
 * <p>
 * 
 * E(M) = M^e mod n, and sends the integer E(M) to Bob.
 * <p>
 * As an example, if M = 2003, e = 7, d = 2563, n = 3713, then Alice computes
 * E(M) = 20037 mod 3713 = 129,350,063,142,700,422,208,187 mod 3713 = 746.
 * <p>
 * When Bob receives the encrypted communication E(M), he decrypts it by
 * computing: M = E(M)^d mod n. Continuing with the example above, Bob recovers
 * the original message by computing: M = 7462563 mod 3713 = 2003.
 * <p>
 * 
 * <b>TO DO</b>: write a program that compute two large pseudo-random prime
 * numbers p and q of a specified number of digits. Compute n = pq, this is the
 * modulus in the public key. Compute phi = (p - 1) (q - 1), and select a small
 * integer e that is relatively prime with phi. This is the exponent in the
 * public key. Finally, compute d such that de = 1 (mod phi), this is the
 * decrypting exponent in the private key
 */

public class RSA {

	public static void encrypt() {
		// read the input
		String t = StdIn.readAll().trim();
		String[] s = t.split(",");
		for (int i = 0; i < s.length; i++)
			s[i] = s[i].trim();
		if (s.length != 3)
			throw new IllegalArgumentException(
					"To encrypt a message, input 3 comma separated values : the message, and then e and n, the public encrypting exponent and modulus");

		XP encryptedMessage = XP.modExp(new XP(s[0]), new XP(s[1]),
				new XP(s[2]));
		StdOut.print(encryptedMessage.toString());
	}

	public static void decrypt() {
		// read the input
		String t = StdIn.readAll().trim();
		String[] s = t.split(",");
		for (int i = 0; i < s.length; i++)
			s[i] = s[i].trim();
		if (s.length != 3)
			throw new IllegalArgumentException(
					"To decrypt a message, input 3 comma separated values : the encrypted message, and then d and n, the private decrypting exponent, and the public modulus");

		XP decyptedMessage = XP
				.modExp(new XP(s[0]), new XP(s[1]), new XP(s[2]));
		StdOut.print(decyptedMessage.toString());
	}

	public static void main(String[] args) {
		if (args[0].equals("-"))
			encrypt();
		else if (args[0].equals("+"))
			decrypt();
		else
			throw new IllegalArgumentException("Illegal command line argument");
	}

}
