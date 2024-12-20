package noDeadEnds;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

// comment this code and submit to codeEval

public class RobotPathsNoDeadEnds {

	private final int N;
	private int count;
	private int stuckCount;
	private Set<OrientedPath> s;
	private Set<OrientedPath> pathsToAdd;

	public RobotPathsNoDeadEnds(int N) {
		this.N = N;
		count = 0;
		stuckCount = 0;
		s = new HashSet<OrientedPath>();
		s.add(new OrientedPath(N));

		while (!s.isEmpty()) {
			pathsToAdd = new HashSet<OrientedPath>();

			Iterator<OrientedPath> setIterator = s.iterator();
			while (setIterator.hasNext()) {
				OrientedPath p = setIterator.next();

				if (p.isAtGoal()) {
					count++;
					setIterator.remove();
					continue;
				}
				if (p.isStuck()) {
					stuckCount++;
					setIterator.remove();
					continue;
				}

				updateOrientedPath(p);
				setIterator.remove();
			}
			s.addAll(pathsToAdd);
		}
	}

	private void updateOrientedPath(OrientedPath p) {
		OrientedPath np = new OrientedPath(p);
		
		if (np.isMarked(np.getRow() - 1, np.getColumn()) && np.getTurnCounter() >= 3)
			;
		if (np.isMarked(np.getRow(), np.getColumn() - 1) && np.getTurnCounter() <= -3)
			;
		if (np.moveRight()) {
			pathsToAdd.add(np);
			np = new OrientedPath(p);
		}
		
		if (np.isMarked(np.getRow(), np.getColumn() + 1) && np.getTurnCounter() >= 3)
			;
		if (np.isMarked(np.getRow(), np.getColumn() - 1) && np.getTurnCounter() <= -3)
			;
		if (np.moveDown()) {
			pathsToAdd.add(np);
			np = new OrientedPath(p);
		}

		if (np.getRow() != 0 && np.getRow() != N - 1)
			if (np.moveLeft()) {
				pathsToAdd.add(np);
				np = new OrientedPath(p);
			}

		if (np.getColumn() != 0 && np.getColumn() != N - 1)
			if (np.moveUp())
				pathsToAdd.add(np);

	}

	public int numberOfOrientedPaths() {
		return count;
	}

	public int numberOfDeadEnds() {
		return stuckCount;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		RobotPathsNoDeadEnds rp = new RobotPathsNoDeadEnds(
				Integer.parseInt(args[0]));
		System.out.println(rp.numberOfOrientedPaths());
		System.out.println(rp.numberOfDeadEnds());
	}
}