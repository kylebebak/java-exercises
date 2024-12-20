import java.awt.Color;
import java.util.ArrayList;

public class SeamCarverNoTranspose {

	private int H;
	private int W;
	private double[][] energy; // this stores the information in the
								// image "digraph", it's analogous to a
								// matrix of edge objects
	private double[][] energyTo; // these two are vertex variables in the
	// digraph paradigm
	private int[][] vertexTo;

	private Picture picture;

	public SeamCarverNoTranspose(Picture picture) {

		this.picture = new Picture(picture); // copy constructor so original
												// picture isn't mutated
		W = picture.width();
		H = picture.height();

		energy = new double[H][W];
		energyTo = new double[H][W];
		vertexTo = new int[H][W];

		for (int y = 0; y < H; y++)
			for (int x = 0; x < W; x++)
				setEnergy(x, y);
	}

	public Picture picture() {
		// current picture
		Picture newPicture = new Picture(W, H);
		for (int y = 0; y < H; y++)
			for (int x = 0; x < W; x++)
				newPicture.set(x, y, picture.get(x, y));
		picture = newPicture;
		return picture;
	}

	public int width() {
		return W; // width of current picture
	}

	public int height() {
		return H; // height of current picture
	}

	public double energy(int x, int y) {
		// energy of pixel at column x and row y
		return energy[y][x];
	}

	private void setEnergy(int x, int y) {

		if (x == 0 || x == W - 1 || y == 0 || y == H - 1) {
			energy[y][x] = 195075.0;
			return;
		}

		Color c0 = picture.get(x - 1, y);
		Color c1 = picture.get(x + 1, y);
		energy[y][x] = Math.pow(c0.getRed() - c1.getRed(), 2)
				+ Math.pow(c0.getGreen() - c1.getGreen(), 2)
				+ Math.pow(c0.getBlue() - c1.getBlue(), 2);
		c0 = picture.get(x, y - 1);
		c1 = picture.get(x, y + 1);
		energy[y][x] += Math.pow(c0.getRed() - c1.getRed(), 2)
				+ Math.pow(c0.getGreen() - c1.getGreen(), 2)
				+ Math.pow(c0.getBlue() - c1.getBlue(), 2);
	}

	// helper function for transposing matrices
	private void transposeMatrices() {
		energy = transpose(energy);
		energyTo = transpose(energyTo);
		vertexTo = transpose(vertexTo);
	}

	public int[] findHorizontalSeam() {
		// sequence of indices for horizontal seam
		// matrices must be reset when looking for a seam
		for (int y = 0; y < H; y++)
			energyTo[y][0] = 0;
		for (int y = 0; y < H; y++)
			for (int x = 1; x < W; x++)
				energyTo[y][x] = Double.POSITIVE_INFINITY;

		int[] seam = new int[W];

		for (int x = 0; x < W - 1; x++) {
			for (int y = 0; y < H; y++) {
				if (y != 0)
					relaxVertex(x, y, x + 1, y - 1, false);
				relaxVertex(x, y, x + 1, y, false);
				if (y != H - 1)
					relaxVertex(x, y, x + 1, y + 1, false);
			}
		}
		double minEnergy = Double.POSITIVE_INFINITY;
		int r = 0;
		for (int y = 0; y < H; y++)
			if (energyTo[y][W - 1] < minEnergy) {
				minEnergy = energyTo[y][W - 1];
				r = y;
			}
		
		seam[W - 1] = r;
		for (int w = W - 1; w > 0; w--) {
			r = vertexTo[r][w];
			seam[w - 1] = r;
		}
		return seam;
	}

	public int[] findVerticalSeam() {
		// sequence of indices for vertical seam
		// energyTo matrix must be reset when looking for a seam
		for (int x = 0; x < W; x++)
			energyTo[0][x] = 0;
		for (int y = 1; y < H; y++)
			for (int x = 0; x < W; x++)
				energyTo[y][x] = Double.POSITIVE_INFINITY;

		int[] seam = new int[H];

		for (int y = 0; y < H - 1; y++) {
			for (int x = 0; x < W; x++) {
				if (x != 0)
					relaxVertex(x, y, x - 1, y + 1, true);
				relaxVertex(x, y, x, y + 1, true);
				if (x != W - 1)
					relaxVertex(x, y, x + 1, y + 1, true);
			}
		}
		double minEnergy = Double.POSITIVE_INFINITY;
		int c = 0;
		for (int x = 0; x < W; x++)
			if (energyTo[H - 1][x] < minEnergy) {
				minEnergy = energyTo[H - 1][x];
				c = x;
			}
		seam[H - 1] = c;
		for (int h = H - 1; h > 0; h--) {
			c = vertexTo[h][c];
			seam[h - 1] = c;
		}
		return seam;
	}

	private void relaxVertex(int sx, int sy, int x, int y, boolean vertical) {
		// recalculate path energy to a given vertex from a given source vertex
		double energyToNewVertex = energyTo[sy][sx] + energy[sy][sx];
		if (energyTo[y][x] > energyToNewVertex) {
			energyTo[y][x] = energyToNewVertex;
			vertexTo[y][x] = vertical ? sx : sy;
		}
	}

	public void removeHorizontalSeam(int[] a) {
		// remove horizontal seam from picture
		if (a.length != W)
			throw new UnsupportedOperationException(
					"Seam length must match image dimension");
	}

	public void removeVerticalSeam(int[] a) {
		// remove vertical seam from picture
		if (a.length != H)
			throw new UnsupportedOperationException(
					"Seam length must match image dimension");
		int c;
		for (int y = 0; y < H; y++) {
			c = a[y];
			for (int x = c + 1; x < W; x++)
				picture.set(x - 1, y, picture.get(x, y));
		}
		W--;

		// remove vertical seam from matrices, then recalculate energies, but
		// only at vertices adjacent to removed seam
		for (int y = 0; y < H; y++) {
			c = a[y];
			energy[y] = removeElement(energy[y], c);
			// energyTo[y] = removeElement(energyTo[y], c);
			// vertexTo[y] = removeElement(vertexTo[y], c);

			if (c < W)
				setEnergy(c, y);
			if (c > 0)
				setEnergy(c - 1, y);
		}
	}

	// helper function for transposing a 2d array
	private double[][] transpose(double[][] in) {
		if (in.length == 0)
			throw new UnsupportedOperationException("Array underflow");
		if (in[0].length == 0)
			throw new UnsupportedOperationException("Array underflow");

		double[][] out = new double[in[0].length][in.length];
		for (int i = 0; i < in.length; i++)
			for (int j = 0; j < in[i].length; j++)
				out[j][i] = in[i][j];
		return out;
	}

	private int[][] transpose(int[][] in) {
		if (in.length == 0)
			throw new UnsupportedOperationException("Array underflow");
		if (in[0].length == 0)
			throw new UnsupportedOperationException("Array underflow");

		int[][] out = new int[in[0].length][in.length];
		for (int i = 0; i < in.length; i++)
			for (int j = 0; j < in[i].length; j++)
				out[j][i] = in[i][j];
		return out;
	}

	// helper function for removing an element from an array
	private double[] removeElement(double[] in, int element) {
		if (in.length == 0)
			throw new UnsupportedOperationException("Array underflow");
		double[] out = new double[in.length - 1];
		System.arraycopy(in, 0, out, 0, element);
		System.arraycopy(in, element + 1, out, element, out.length - element);
		return out;
	}

	private int[] removeElement(int[] in, int element) {
		if (in.length == 0)
			throw new UnsupportedOperationException("Array underflow");
		int[] out = new int[in.length - 1];
		System.arraycopy(in, 0, out, 0, element);
		System.arraycopy(in, element + 1, out, element, out.length - element);
		return out;
	}

	public static void main(String[] args) {
		Picture inputImg = new Picture(args[0]);
		SeamCarverNoTranspose sc = new SeamCarverNoTranspose(inputImg);
		Stopwatch sw = new Stopwatch();
		for (int i = 0; i < 100; i++) {
			for (int column : sc.findHorizontalSeam()) {
		//		StdOut.println(column);
			}
		}
		System.out.println("Seam-finding time " + sw.elapsedTime() + " seconds.");
	}
}
