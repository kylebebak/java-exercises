/**
 * author: kylebebak@gmail.com
 * <p>
 * 
 * Encode or decode an input stream using the move-to-front algorithm.
 * <p>
 * 
 * Execution: java MoveToFront - < input.txt (encode)<br>
 * Execution: java MoveToFront + < input.txt (decode)
 * <p>
 * 
 * <b>% java MoveToFront - < abra.txt | java HexDump</b><br>
 * 41 42 52 02 44 01 45 01 04 04 02 26 96<br> bits
 */
public class MoveToFront {
	// this class has no constructor, only static methods

	// alphabet size of extended ASCII
	private static final int R = 256;
	private static char[] alphabet = new char[R];

	// apply move-to-front encoding, reading from standard input and writing to
	// standard output
	public static void encode() {

		// initialize ordered alphabet array
		for (char c = 0; c < R; c++)
			alphabet[c] = c;

		// read input characters and perform MTF encoding
		int toWrite;
		while (!BinaryStdIn.isEmpty()) {
			toWrite = moveToFront(BinaryStdIn.readChar());
			BinaryStdOut.write((char) toWrite);
		}

		// close output stream
		BinaryStdOut.close();
	}

	// apply move-to-front decoding, reading from standard input and writing to
	// standard output
	public static void decode() {

		// initialize ordered alphabet array
		for (char c = 0; c < R; c++)
			alphabet[c] = c;

		// read input characters and perform MTF encoding
		char c;
		while (!BinaryStdIn.isEmpty()) {
			c = alphabet[BinaryStdIn.readChar()];
			BinaryStdOut.write(c);
			moveToFront(c);
		}

		// close output stream
		BinaryStdOut.close();

	}

	// move char c to the front of the alphabet array, move all chars with
	// indices less than index of char c one forward in alphabet array. returns
	// the index at which this character was found in the alphabet array
	private static int moveToFront(char c) {
		int index = 0;
		while (alphabet[index] != c)
			index++;
		for (int i = index; i > 0; i--)
			alphabet[i] = alphabet[i - 1];
		alphabet[0] = c;
		return index;
	}

	/**
	 * @param args
	 * <br>
	 *            if args[0] is '-', apply move-to-front encoding<br>
	 *            if args[0] is '+', apply move-to-front decoding
	 */
	public static void main(String[] args) {
		if (args[0].equals("-"))
			encode();
		else if (args[0].equals("+"))
			decode();
		else
			throw new IllegalArgumentException("Illegal command line argument");
	}

}
