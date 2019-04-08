package algorithms;

import models.Particle;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Brownian motion implementation
 */
public class EventDrivenMolecularDynamics {

	private static PriorityQueue<Event> pq = new PriorityQueue<>();
	private static double L;
	private static double currentSimulationTime;
	private static double lastEventTime;

	public static void run(
			List<Particle> particlesFromDynamic,
			double boxSize,
			double limitTime,
			StringBuffer buff,
			PrintWriter eventWriter) {
		L = boxSize;

		// Particles for fixing Ovito grid
		Particle dummy1 = new Particle(-1, 0, 0);
		Particle dummy2 = new Particle(0, 0, 0);
		dummy1.setPosition(new Vector2D(0, 0));
		dummy1.setVelocity(new Vector2D(0, 0));
		dummy2.setPosition(new Vector2D(L, L));
		dummy2.setVelocity(new Vector2D(0, 0));


		// Print dummy particles to simulation output file
		buff.append(particlesFromDynamic.size() + 2).append("\n")
				.append(0 + "\n")
				.append(particleToString(dummy1)).append("\n")
				.append(particleToString(dummy2)).append("\n");

		// Print location
		for (Particle p : particlesFromDynamic) {
			buff.append(particleToString(p)).append("\n");
		}

		// Determine all future collisions that would occur involving either i or j, assuming all particles move
		// in straight line trajectories from time t onwards. Insert these events onto the priority queue.
		determineFutureCollisions(particlesFromDynamic, limitTime);

		// Set last event time as 0
		lastEventTime = 0.0;

		// Main event-driven simulation loop
		while (!pq.isEmpty()) {
			// Delete the impending event, i.e., the one with the minimum priority t.
			Event nextEvent = pq.poll();

			// If the event corresponds to an invalidated collision, discard it. The event is invalid if one of the particles has
			//participated in a collision since the event was inserted onto the priority queue.
			assert nextEvent != null;
			if (nextEvent.wasSuperveningEvent())
				continue;

			currentSimulationTime = nextEvent.getTime();
			System.out.println(currentSimulationTime);
			// If the event corresponds to a physical collision between particles i and j:

			// Advance all particles to time t along a straight line trajectory.
			advanceAllParticlesTc(particlesFromDynamic, currentSimulationTime - lastEventTime);

			// Update the velocities of the two colliding particles i and j according to the laws of elastic collision.
			// If the event corresponds to a physical collision between particles i and a wall, do the analogous
			// thing for particle i.
			updateVelocities(nextEvent.particle1, nextEvent.particle2);

			// Write time of collision
			eventWriter.println(currentSimulationTime - lastEventTime);

			// Determine all future collisions that would occur involving either i or j, assuming all particles move
			// in straight line trajectories from time t onwards. Insert these events onto the priority queue.
			if (nextEvent.particle1 != null)
				determineFutureCollisions(nextEvent.particle1, particlesFromDynamic, limitTime);
			if (nextEvent.particle2 != null)
				determineFutureCollisions(nextEvent.particle2, particlesFromDynamic, limitTime);

			lastEventTime = currentSimulationTime;
		}
	}

	private static void updateVelocities(Particle p1, Particle p2) {
		if (p1 == null && p2 == null) throw new IllegalArgumentException();
		if (p1 != null && p2 != null)
			p1.bounce(p2);
		else if (p2 == null)
			p1.bounceX();
		else
			p2.bounceY();
	}

	private static void advanceAllParticlesTc(List<Particle> particles, double t) {
		for (Particle p1 : particles) {
			p1.evolve(t);
		}
	}

	private static void determineFutureCollisions(Particle p1, List<Particle> particles, double limitTime) {
		// Collisions with walls
		calculateCollisionWithWalls(p1, limitTime);

		// Collisions with other particles
		for (Particle p2 : particles) {
			if (p1 != p2) {
				calculateCollisionBetweenParticles(p1, p2, limitTime);
			}
		}
	}

	private static void determineFutureCollisions(List<Particle> particles, double limitTime) {
		for (Particle p1 : particles) {
			// Collisions with walls
			calculateCollisionWithWalls(p1, limitTime);

			// Collisions between particles
			for (Particle p2 : particles) {
				if (p1 != p2) {
					calculateCollisionBetweenParticles(p1, p2, limitTime);
				}
			}
		}
	}

	private static void calculateCollisionWithWalls(Particle p1, double limitTime) {
		double tcX = p1.collidesX(L);
		double tcY = p1.collidesY(L);
		if (tcX >= 0 && currentSimulationTime + tcX <= limitTime) {
			pq.offer(new Event(currentSimulationTime + tcX, p1, null));
		}
		if (tcY >= 0 && currentSimulationTime + tcY <= limitTime) {
			pq.offer(new Event(currentSimulationTime + tcY, null, p1));
		}
	}

	private static void calculateCollisionBetweenParticles(Particle p1, Particle p2, double limitTime) {
		double tc = p1.collides(p2);
		if (tc >= 0 && currentSimulationTime + tc <= limitTime) {
			pq.offer(new Event(currentSimulationTime + tc, p1, p2));
		}
	}

	private static String particleToString(Particle p) {
		return p.getId() + " " +
				p.getPosition().getX() + " " +
				p.getPosition().getY() + " " +
				p.getVelocity().getX() + " " +
				p.getVelocity().getY() + " " //TODO: should colour come here as angle did in TP2?
				;
	}

	/**
	 * A data type to represent collision events.
	 * There are four different types of events:
	 * a collision with a vertical wall,
	 * a collision with a horizontal wall,
	 * a collision between two particles,
	 * and a redraw event.
	 */
	private static class Event implements Comparable<Event> {

		private double t;
		private Particle particle1;
		private Particle particle2;
		private int collisionsP1 = 0;
		private int collisionsP2 = 0;

		/**
		 * Create a new event representing a collision between particles a and b at time t.
		 * If neither a nor b is null, then it represents a pairwise collision between a and b;
		 * if both a and b are null, it represents a redraw event; if only b is null,
		 * it represents a collision between a and a vertical wall; if only a is null,
		 * it represents a collision between b and a horizontal wall.
		 *
		 * @param t time t
		 * @param a particle a
		 * @param b particle b
		 */
		public Event(double t, Particle a, Particle b) {
			this.t = t;
			this.particle1 = a;
			this.particle2 = b;
			if (particle1 != null)
				collisionsP1 = particle1.getCollisionCount();
			if (particle2 != null)
				collisionsP2 = particle2.getCollisionCount();
		}

		/**
		 * return the time associated with the event.
		 */
		public double getTime() {
			return this.t;
		}

		/**
		 * return the first particle, possibly null.
		 */
		public Particle getParticle1() {
			return this.particle1;
		}

		/**
		 * return the second particle, possibly null.
		 */
		public Particle getParticle2() {
			return this.particle2;
		}

		/**
		 * compare the time associated with this event and x. Return a positive number (greater), negative number (less), or zero (equal) accordingly.
		 */
		public int compareTo(Event e) {
			return Double.compare(t, e.getTime());
		}

		/**
		 * return true if the event has been invalidated since creation, and false if the event has been invalidated.
		 */
		public boolean wasSuperveningEvent() {
			if (particle1 == null)
				return particle2.getCollisionCount() != collisionsP2;
			if (particle2 == null)
				return particle1.getCollisionCount() != collisionsP1;
			return particle1.getCollisionCount() != collisionsP1 || particle2.getCollisionCount() != collisionsP2;
		}
	}
}
