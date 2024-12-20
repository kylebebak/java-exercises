import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

// comment this code and submit to codeEval

public class RobotPaths {

	private int R;
	private int C;
	private int count;
	private int stuckCount;
	private Set<Path> s;
	private Set<Path> pathsToAdd;

	public RobotPaths(int R, int C) {
		this.R = R;
		this.C = C;
		count = 0;
		stuckCount = 0;
		s = new HashSet<Path>();
		s.add(new Path(R, C));

		while (!s.isEmpty()) {
			pathsToAdd = new HashSet<Path>();

			Iterator<Path> setIterator = s.iterator();
			while (setIterator.hasNext()) {
				Path p = setIterator.next();

				if (p.isAtGoal()) {
					count++;
					System.out.println(p.toString());
					setIterator.remove();
					continue;
				}
				if (p.isStuck()) {
					stuckCount++;
					setIterator.remove();
					continue;
				}

				updatePath(p);
				setIterator.remove();
			}
			s.addAll(pathsToAdd);
		}
	}

	private void updatePath(Path p) {
		Path newPath;

		newPath = new Path(p);
		if (newPath.moveRight()) {
			pathsToAdd.add(newPath);
			newPath = new Path(p);
		}

		if (newPath.moveDown()) {
			pathsToAdd.add(newPath);
			newPath = new Path(p);
		}

		if (newPath.getRow() != 0 && newPath.getRow() != R - 1)
			if (newPath.moveLeft()) {
				pathsToAdd.add(newPath);
				newPath = new Path(p);
			}

		if (newPath.getColumn() != 0 && newPath.getColumn() != C - 1)
			if (newPath.moveUp())
				pathsToAdd.add(newPath);

	}

	public int numberOfPaths() {
		return count;
	}

	public int numberOfDeadEnds() {
		return stuckCount;
	}

	/**
	 * @param args <br/>
	 * args[0] is number of rows in grid, args[1] is number of columns
	 */ 
	public static void main(String[] args) {
		RobotPaths rp = new RobotPaths(
				Integer.parseInt(args[0]), Integer.parseInt(args[1]));
		System.out.println();
		System.out.println("paths that reached goal : " + rp.numberOfPaths());
		System.out.println("paths that got stuck    : " + rp.numberOfDeadEnds());
	}
}