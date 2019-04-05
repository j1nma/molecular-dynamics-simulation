package io;

import models.Particle;

import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

/**
 * Parser for static and dynamic files.
 */
public class Parser {

	private String staticFilePath;
	private String dynamicFilePath;

	private int numberOfParticles;
	private double boxSize;
	private Queue<Particle> particles;

	public Parser(String staticFilePath, String dynamicFilePath) {
		this.staticFilePath = staticFilePath;
		this.dynamicFilePath = dynamicFilePath;
		this.particles = new LinkedList<>();
	}

	public boolean parse() {
		return parseStaticFile() && parseDynamicFile();
	}

	private boolean parseStaticFile() {
		File staticFile = new File(staticFilePath);
		Scanner sc;
		try {
			sc = new Scanner(staticFile);
		} catch (FileNotFoundException e) {
			System.out.println("Static file not found exception: " + staticFilePath);
			return false;
		}
		numberOfParticles = sc.nextInt() + 1;// + 1 for large particle
		for (int i = 0; i < numberOfParticles; i++) {
			double radius = sc.nextDouble();
			double mass = sc.nextDouble();
			particles.add(new Particle(i + 1, radius, mass));
		}
		sc.close();
		return true;
	}

	private boolean parseDynamicFile() {
		File dynamicFile = new File(dynamicFilePath);
		Scanner sc;
		try {
			sc = new Scanner(dynamicFile);
		} catch (FileNotFoundException e) {
			System.out.println("Dynamic file not found exception: " + dynamicFilePath);
			return false;
		}
		sc.nextInt();   // numberOfParticles already caught in Static parser (in dynamic for ovito)
		boxSize = sc.nextDouble();
		for (int i = 0; i < numberOfParticles; i++) {
			double x = sc.nextDouble();
			double y = sc.nextDouble();
			double vx = sc.nextDouble();
			double vy = sc.nextDouble();
			Particle particle = particles.poll();
			assert particle != null;
			particle.setPosition(new Point2D.Double(x, y));
			particle.setVelocity(new Point2D.Double(vx, vy));
			particles.add(particle);
		}
		sc.close();
		return true;
	}

	public String getStaticFilePath() {
		return staticFilePath;
	}

	public void setStaticFilePath(String staticFilePath) {
		this.staticFilePath = staticFilePath;
	}

	public String getDynamicFilePath() {
		return dynamicFilePath;
	}

	public void setDynamicFilePath(String dynamicFilePath) {
		this.dynamicFilePath = dynamicFilePath;
	}

	public int getNumberOfParticles() {
		return numberOfParticles;
	}

	public void setNumberOfParticles(int numberOfParticles) {
		this.numberOfParticles = numberOfParticles;
	}

	public double getBoxSize() {
		return boxSize;
	}

	public void setBoxSize(double boxSize) {
		this.boxSize = boxSize;
	}

	public List<Particle> getParticles() {
		return (List<Particle>) particles;
	}

	public void setParticles(Queue<Particle> particles) {
		this.particles = particles;
	}
}
