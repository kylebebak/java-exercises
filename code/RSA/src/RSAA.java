/**
 * RSA cryptosystem encrypter and decrypter, accepts a message which can be any
 * ascii string of characters, and decimal integers for the public and private
 * keys.
 * <p>
 * 
 * Execution : <b><i>java RSAA - < input.txt > encrypted.txt</b></i>
 * <b><i>java RSAA + < encrypted.txt > decrypted.txt</b></i>
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

public class RSAA {

	private static final int R = 128; // radix for standard ascii encoding

	public static void encrypt() {
		// read the input
		String[] s = StdIn.readAll().split(",,,,,");
		if (s.length != 3)
			throw new IllegalArgumentException(
					"To encrypt a message, input 3 bar ,,,,, separated values : the message, and then e and n, the public encrypting exponent and modulus");

		XPA m = new XPA(s[0]);
		XP e = new XP(s[1]);
		XP n = new XP(s[2]);
		XPA encryptedMessage = XPA.modExp(m, RSAA.changeBase(e, R),
				RSAA.changeBase(n, R));
		StdOut.print(encryptedMessage.toCharString());
	}

	public static void decrypt() {
		// read the input
		String[] s = StdIn.readAll().split(",,,,,");
		if (s.length != 3)
			throw new IllegalArgumentException(
					"To decrypt a message, input 3 ,,,,, separated values : the encrypted message, and then d and n, the private decrypting exponent, and the public modulus");

		XPA m = new XPA(s[0]);
		XP d = new XP(s[1]);
		XP n = new XP(s[2]);
		XPA decryptedMessage = XPA.modExp(m, RSAA.changeBase(d, R),
				RSAA.changeBase(n, R));
		StdOut.print(decryptedMessage.toCharString());
	}

	/**
	 * Input a base 10 XP and output XPA with same value in the new base "b"
	 */
	public static XPA changeBase(XP in, int b) {

		XP B = new XP(Integer.toString(b));
		if (XP.compare(B, in) > 0) {
			int val = Integer.parseInt(in.toString());
			return new XPA(val, 1);
		}

		int digit = 1;
		XP BB = new XP(B);
		while (true) {
			if (XP.compare(XP.multiply(BB, B), in) > 0)
				break;
			BB = XP.multiply(BB, B);
			digit++;
		}

		XPA out = new XPA(0, digit + 1);
		XP r = new XP(in);
		for (int d = digit; d >= 0; d--) {
			XP[] qr = XP.divide(r, BB);
			out.setDigit(d, Integer.parseInt(qr[0].toString()));
			r = qr[1];
			BB = XP.divide(BB, B)[0];
		}

		return out;
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
