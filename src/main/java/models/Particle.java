package models;

import java.awt.geom.Point2D;

public class Particle implements Cloneable {

	private final int id;
	private Point2D.Double position;
	private Point2D.Double velocity;
	private double radius;
	private double mass;

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
	public double collidesX() {
		return 0.0;
	}

	/**
	 * return the duration of time until the invoking particle collides with a vertical wall,
	 * assuming it follows a straight-line trajectory. If the particle never collides with a vertical wall,
	 * return a negative number.
	 *
	 * @return duration of time until collision
	 */
	public double collidesY() {
		return 0.0;
	}

	/**
	 * return the duration of time until the invoking particle collides with particle b,
	 * assuming both follow straight-line trajectories. If the two particles never collide, return a negative value.
	 *
	 * @param b other particle
	 * @return duration of time until collision
	 */
	public double collides(Particle b) {
		return 0.0;
	}

	/**
	 * update the invoking particle to simulate it bouncing off a vertical wall.
	 */
	public void bounceX() {

	}

	/**
	 * update the invoking particle to simulate it bouncing off a horizontal wall.
	 */
	public void bounceY() {

	}

	/**
	 * update both particles to simulate them bouncing off each other.
	 *
	 * @param b other particle in bounce
	 */
	public void bounce(Particle b) {

	}

	/**
	 * return the total number of collisions involving this particle.
	 *
	 * @return total collisions
	 */
	public int getCollisionCount() {
		return 0;
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
		return "Particle{" +
				"id=" + id +
				", position=" + position +
				", velocity=" + velocity +
				", radius=" + radius +
				", mass=" + mass +
				'}';
	}

	public int getId() {
		return id;
	}

	public Point2D.Double getPosition() {
		return position;
	}

	public void setPosition(Point2D.Double position) {
		this.position = position;
	}

	public Point2D.Double getVelocity() {
		return velocity;
	}

	public void setVelocity(Point2D.Double velocity) {
		this.velocity = velocity;
	}

	public double getRadius() {
		return radius;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}

	public double getMass() {
		return mass;
	}

	public void setMass(double mass) {
		this.mass = mass;
	}

	public Particle getClone() throws CloneNotSupportedException {
		return (Particle) super.clone();
	}
}
