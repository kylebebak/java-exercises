public class Event implements Comparable<Event> {

	private double t;
	private P4rticle a;
	private P4rticle b;
	private int cca;
	private int ccb;

	/**
	 * Particle A collides with particle B. A null means B collides with
	 * horizontal wall, B null means A collides with vertical wall, both null
	 * means a redraw event, which has nothing to do with particle dynamics, and
	 * is simply taking advantage of the final fourth state available to us as a
	 * consequence of the two particle "bits" (null or not null) passed as
	 * arguments to the event constructor
	 */
	public Event(double t, P4rticle a, P4rticle b) {
		this.t = t;
		this.a = a;
		this.b = b;
		// Event doesn't instantiate new particles, it just gets pointers to the
		// particles
		if (a != null)
			cca = a.getCollisionCount();
		if (b != null)
			ccb = b.getCollisionCount();
	}

	/**
	 * Return the time associated with the event
	 */
	public double getTime() {
		return t;
	}

	/**
	 * Return pointer to first particle, possibly null
	 */
	public P4rticle getParticleA() {
		return a;
	}

	/**
	 * Return pointer to second particle, possibly null
	 */
	public P4rticle getParticleB() {
		return b;
	}

	/**
	 * Return true if the event hasn't been invalidated since creation
	 */
	public boolean isStillValid() {
		if (a != null && b != null)
			return (cca == a.getCollisionCount() && ccb == b
					.getCollisionCount());
		else if (a != null)
			return cca == a.getCollisionCount();
		else if (b != null)
			return ccb == b.getCollisionCount();

		// this last possibility only happens when both particles are null and
		// event is a redraw event, and this always returns true
		else
			return true;
	}

	@Override
	public int compareTo(Event o) {
		// compare the time associated with this event and x. Return a positive
		// number (greater), negative number (less), or zero (equal)
		if (t > o.t)
			return 1;
		if (t < o.t)
			return -1;
		return 0;
	}

}