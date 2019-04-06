package models;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.util.FastMath;

import java.text.DecimalFormat;

public class Particle implements Cloneable {

	private final int id;
	private Vector2D position;
	private Vector2D velocity;
	private double radius;
	private double mass;
	private int collisionCount = 0;

	public Particle(int id, double radius, double mass) {
		this.id = id;
		this.radius = radius;
		this.mass = mass;
	}

	/**
	 * return the duration of time until the invoking particle collides with a vertical wall,
	 * assuming it follows a straight-line trajectory. If the particle never collides with a vertical wall,
	 * return a negative number.
	 *
	 * @return duration of time until collision
	 */
	public double collidesX(double L) {
		if (velocity.getX() > 0)
			return (L - radius - position.getX()) / velocity.getX();
		if (velocity.getX() < 0)
			return (radius - position.getX()) / velocity.getX();
		return -1;
	}

	/**
	 * return the duration of time until the invoking particle collides with a horizontal wall,
	 * assuming it follows a straight-line trajectory. If the particle never collides with a horizontal wall,
	 * return a negative number.
	 *
	 * @return duration of time until collision
	 */
	public double collidesY(double L) {
		if (velocity.getY() > 0)
			return (L - radius - position.getY()) / velocity.getY();
		if (velocity.getY() < 0)
			return (radius - position.getY()) / velocity.getY();
		return -1;
	}

	/**
	 * return the duration of time until the invoking particle collides with particle b,
	 * assuming both follow straight-line trajectories. If the two particles never collide, return a negative value.
	 *
	 * @param b other particle
	 * @return duration of time until collision
	 */
	public double collides(Particle b) {
		Vector2D dr = new Vector2D(this.position.getX() - b.position.getX(), this.position.getY() - b.position.getY());
		Vector2D dv = new Vector2D(this.velocity.getX() - b.velocity.getX(), this.velocity.getY() - b.velocity.getY());
		double dvdr = dv.dotProduct(dr);
		if (dvdr >= 0)
			return -1; // the quadratic equation has no solution for Δt > 0
		double dvdv = dv.dotProduct(dv);
		double drdr = dr.dotProduct(dr);
		double sigma = this.radius + b.radius;
		double d = FastMath.pow(dvdr, 2) - dvdv * (drdr - FastMath.pow(sigma, 2));
		if (d < 0)
			return -1; // the quadratic equation has no solution for Δt > 0
		return -(dvdr + FastMath.sqrt(d)) / dvdv;
	}

	/**
	 * update the invoking particle to simulate it bouncing off a vertical wall.
	 */
	public void bounceX() {
		this.velocity = new Vector2D(-velocity.getX(), velocity.getY());
		this.collisionCount++;
	}

	/**
	 * update the invoking particle to simulate it bouncing off a horizontal wall.
	 */
	public void bounceY() {
		this.velocity = new Vector2D(velocity.getX(), -velocity.getY());
		this.collisionCount++;
	}

	/**
	 * Update both particles to simulate them bouncing off each other.
	 * <p>
	 * When two hard discs collide, the normal force acts along the line connecting their centers
	 * (assuming no friction or spin).
	 *
	 * @param b other particle in bounce
	 */
	public void bounce(Particle b) {
		Vector2D dr = new Vector2D(this.position.getX() - b.position.getX(), this.position.getY() - b.position.getY());
		Vector2D dv = new Vector2D(this.velocity.getX() - b.velocity.getX(), this.velocity.getY() - b.velocity.getY());

		double dvdr = dv.dotProduct(dr);

		// The impulse (Jx, Jy) due to the normal force in the x and y directions of a perfectly elastic collision at the moment of contact
		double j = (2 * mass * b.mass * dvdr) / ((mass + b.mass) * (radius + b.radius));
		double jx = j * dr.getX() / (radius + b.radius);
		double jy = j * dr.getY() / (radius + b.radius);

		// Once we know the impulse, we can apply Newton's second law (in momentum form) to compute the velocities immediately after the collision.
		this.velocity = new Vector2D(velocity.getX() - jx / mass, velocity.getY() - jy / mass);
		b.velocity = new Vector2D(b.velocity.getX() + jx / b.mass, b.velocity.getY() + jy / b.mass);

		this.collisionCount++;
		b.collisionCount++;
	}

	/**
	 * return the total number of collisions involving this particle.
	 *
	 * @return total collisions
	 */
	public int getCollisionCount() {
		return collisionCount;
	}

	public void evolve(double time) {
		position = position.add(time, velocity);
	}

	public double getKineticEnergy() {
		return 0.5 * mass * velocity.getNormSq();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Particle particle = (Particle) o;
		return id == particle.id;
	}

	@Override
	public int hashCode() {
		return id;
	}

	@Override
	public String toString() {
		DecimalFormat df = new DecimalFormat("###.0000000000");
		return df.format(position.getX()) + " "
				+ df.format(position.getY()) + " "
				+ df.format(velocity.getX()) + " "
				+ df.format(velocity.getY()) + " "
				+ mass + " "
				+ radius
				+ df.format(getKineticEnergy()) + " ";
	}

	public int getId() {
		return id;
	}

	public Vector2D getPosition() {
		return position;
	}

	public void setPosition(Vector2D position) {
		this.position = position;
	}

	public Vector2D getVelocity() {
		return velocity;
	}

	public void setVelocity(Vector2D velocity) {
		this.velocity = velocity;
	}

}
