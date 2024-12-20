/*************************************************************************
 * Compilation: javac ResizeDemo.java Execution: java ResizeDemo input.png
 * columnsToRemove rowsToRemove Dependencies: SeamCarver.java SCUtility.java
 * Picture.java Stopwatch.java StdDraw.java
 * 
 * 
 * Read image from file specified as command line argument. Use SeamCarver to
 * remove number of rows and columns specified as command line arguments. Show
 * the images in StdDraw and print time elapsed to screen.
 * 
 *************************************************************************/

public class ResizeDemo {
	public static void main(String[] args) {
		if (args.length != 3 && args.length != 7) {
			System.out
					.println("Usage:\njava ResizeDemo [image filename] [num cols to remove/add] [num rows to remove/add]     optional rectangle removal: [x0][y0][x1][y1]");
			return;
		}

		Picture inputImg = new Picture(args[0]);
		int columns = Integer.parseInt(args[1]);
		int rows = Integer.parseInt(args[2]);
		boolean addHorizontal = (rows >= 0);
		boolean addVertical = (columns >= 0);

		System.out.printf("image is %d columns by %d rows\n", inputImg.width(),
				inputImg.height());
		SeamCarver sc = new SeamCarver(inputImg);

		Stopwatch sw = new Stopwatch();
		
		if (args.length == 7)
			sc.removeRegion(Integer.parseInt(args[3]),
					Integer.parseInt(args[4]), Integer.parseInt(args[5]),
					Integer.parseInt(args[6]));

		for (int i = 0; i < Math.abs(columns); i++) {
			int[] verticalSeam = sc.findVerticalSeam();
			if (addVertical)
				sc.addVerticalSeam(verticalSeam);
			else
				sc.removeVerticalSeam(verticalSeam);
		}

		for (int i = 0; i < Math.abs(rows); i++) {
			int[] horizontalSeam = sc.findHorizontalSeam();
			if (addHorizontal)
				sc.addHorizontalSeam(horizontalSeam);
			else
				sc.removeHorizontalSeam(horizontalSeam);
		}

		Picture outputImg = sc.picture();

		System.out.printf("new image size is %d columns by %d rows\n",
				sc.width(), sc.height());

		System.out.println("Resizing time: " + sw.elapsedTime() + " seconds.");
		inputImg.show();
		outputImg.show();
	}
}
