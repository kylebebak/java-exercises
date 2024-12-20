import java.awt.Color;

public class SeamCarver {

	private int H;
	private int W;
	private double[][] energy; // this stores the information in the
								// image "digraph", it's analogous to a
								// matrix of edge objects
	private final double borderEnergy = 195075.0; // energy of border pixels
	private double[][] energyTo; // these two are vertex variables in the
	// digraph paradigm
	private int[][] vertexTo;

	private Picture picture;
	private boolean transposed = false;

	public SeamCarver(Picture picture) {

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
		// current picture, this is only the place new Picture() is called
		Picture newPicture = new Picture(W, H);
		for (int y = 0; y < H; y++)
			for (int x = 0; x < W; x++)
				newPicture.set(x, y, picture.get(x, y));
		picture = newPicture;
		return picture;
	}

	public int width() {
		return W; // width of current picture, W and H are never transposed
	}

	public int height() {
		return H; // height of current picture
	}

	public double energy(int x, int y) {
		// energy of pixel at column x and row y, transposed as needed
		return energy[y][x];
	}

	private void setEnergy(int x, int y) {

		if (x == 0 || x == energy[0].length - 1 || y == 0
				|| y == energy.length - 1) {
			energy[y][x] = borderEnergy;
			return;
		}

		int px = transposed ? y : x;
		int py = transposed ? x : y;

		Color c0 = picture.get(px - 1, py);
		Color c1 = picture.get(px + 1, py);
		energy[y][x] = Math.pow(c0.getRed() - c1.getRed(), 2)
				+ Math.pow(c0.getGreen() - c1.getGreen(), 2)
				+ Math.pow(c0.getBlue() - c1.getBlue(), 2);
		c0 = picture.get(px, py - 1);
		c1 = picture.get(px, py + 1);
		energy[y][x] += Math.pow(c0.getRed() - c1.getRed(), 2)
				+ Math.pow(c0.getGreen() - c1.getGreen(), 2)
				+ Math.pow(c0.getBlue() - c1.getBlue(), 2);
	}

	public int[] findHorizontalSeam() {
		// sequence of indices for horizontal seam
		if (!transposed)
			transposeMatrices();
		int W = energy[0].length;
		int H = energy.length;
		return findSeam(W, H);
	}

	public int[] findVerticalSeam() {
		// sequence of indices for vertical seam
		if (transposed)
			transposeMatrices();
		int W = energy[0].length;
		int H = energy.length;
		return findSeam(W, H);
	}

	// helper function for finding seam
	private int[] findSeam(int W, int H) {
		// energyTo matrix must be reset when a seam is found
		// all elements in first row are zero, all others are +infinity
		for (int x = 0; x < W; x++)
			energyTo[0][x] = 0.0;
		for (int y = 1; y < H; y++)
			for (int x = 0; x < W; x++)
				energyTo[y][x] = Double.POSITIVE_INFINITY;
		int[] seam = new int[H];

		for (int y = 0; y < H - 1; y++) {
			for (int x = 0; x < W; x++) {
				if (x != 0)
					relaxVertex(x, y, x - 1, y + 1);
				relaxVertex(x, y, x, y + 1);
				if (x != W - 1)
					relaxVertex(x, y, x + 1, y + 1);
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

	private void relaxVertex(int sx, int sy, int x, int y) {
		// recalculate path energy to a given vertex from a given source vertex
		double energyToNewVertex = energyTo[sy][sx] + energy[sy][sx];
		if (energyTo[y][x] > energyToNewVertex) {
			energyTo[y][x] = energyToNewVertex;
			vertexTo[y][x] = sx;
		}
	}

	public void removeHorizontalSeam(int[] a) {
		// remove horizontal seam from picture
		if (a.length != W || H <= 1)
			throw new IllegalArgumentException();
		// seam length must match W, removing seam can't get of image
		// remove seam from image by shifting pixels up one row
		int r;
		for (int x = 0; x < W; x++) {
			r = a[x];
			if (r < 0 || r >= H) 
				throw new IndexOutOfBoundsException();
			// vertices must be within image
			if (x > 0)
				if (Math.abs(r - a[x - 1]) > 1)
					throw new IllegalArgumentException();
			// consecutive seam vertices must be adjacent
			for (int y = r + 1; y < H; y++)
				picture.set(x, y - 1, picture.get(x, y));
		}

		if (!transposed)
			transposeMatrices();
		int W = energy[0].length;
		int H = energy.length;
		this.H--; // decrement global height
		removeSeam(a, W, H);
	}

	public void removeVerticalSeam(int[] a) {
		// remove vertical seam from picture
		if (a.length != H || W <= 1)
			throw new IllegalArgumentException();
		// remove seam from image by shifting pixels left one row
		int c;
		for (int y = 0; y < H; y++) {
			c = a[y];
			if (c < 0 || c >= W) 
				throw new IndexOutOfBoundsException();
			if (y > 0)
				if (Math.abs(c - a[y - 1]) > 1)
					throw new IllegalArgumentException();
			for (int x = c + 1; x < W; x++)
				picture.set(x - 1, y, picture.get(x, y));
		}

		if (transposed)
			transposeMatrices();
		int W = energy[0].length;
		int H = energy.length;
		this.W--; // decrement global width
		removeSeam(a, W, H);
	}

	private void removeSeam(int[] a, int W, int H) {
		W--;
		int c;
		// remove seam from matrices (done vertically, using transpose as
		// needed) then recalculate energies at vertices next to removed seam
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

	// helper function for transposing matrices
	private void transposeMatrices() {
		energy = transpose(energy);
		energyTo = transpose(energyTo);
		vertexTo = transpose(vertexTo);
		transposed = !transposed;
	}

	// helper functions for transposing a 2d array
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

	// helper functions for removing an element from an array
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
}
