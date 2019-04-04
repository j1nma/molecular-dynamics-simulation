package models;

import java.awt.geom.Point2D;
import java.util.HashSet;
import java.util.Set;

public class Particle implements Cloneable {

	private final int id;
	private Point2D.Double position;
	private Point2D.Double velocity;
	private double radius;
	private double mass;
	private Set<Particle> neighbours;

	public Particle(int id, double radius, double mass) {
		this.id = id;
		this.radius = radius;
		this.mass = mass;
		this.neighbours = new HashSet<>();
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
				", neighbours=" + neighbours +
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

	public Set<Particle> getNeighbours() {
		return neighbours;
	}

	/**
	 * To understand this calculation, think about a 8x8 matrix.
	 * The distance between (0,0) and (0,7) is 7 without periodic boundaries,
	 * but with it, it should be smaller (1, since they 'touch' each other).
	 * So this applies too for (0,5) and (0,6), or, when the index is greater than L/2.
	 * L is the size of the side of the box/matrix.
	 */
	public double getPeriodicDistanceBetween(Particle particle, double L) {
		Point2D.Double particlePosition = particle.getPosition();
		double dx = Math.abs(this.position.x - particlePosition.x);
		if (dx > L / 2)
			dx = L - dx;

		double dy = Math.abs(this.position.y - particlePosition.y);
		if (dy > L / 2)
			dy = L - dy;

		return Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
	}

	/**
	 * The distance between points contemplates border-to-border distance.
	 * That is why the radii are subtracted.
	 */
	public double getDistanceBetween(Particle particle) {
		Point2D.Double particlePosition = particle.getPosition();
		return Math.sqrt(Math.pow(position.x - particlePosition.x, 2) +
				Math.pow(position.y - particlePosition.y, 2));
	}

	public void addNeighbour(Particle neighbour) {
		this.neighbours.add(neighbour);
	}

	public void clearNeighbours() {
		this.neighbours = new HashSet<>();
	}

	public Particle getClone() throws CloneNotSupportedException {
		return (Particle) super.clone();
	}
}
