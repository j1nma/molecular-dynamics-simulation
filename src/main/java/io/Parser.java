package io;

import models.Particle;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Parser for static and dynamic files.
 */
public class Parser {

	private String staticFilePath;
	private String dynamicFilePath;
	private int numberOfParticles;
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
		for (int i = 0; i < numberOfParticles; i++) {
			sc.nextDouble(); // ignore id
			double x = sc.nextDouble();
			double y = sc.nextDouble();
			double vx = sc.nextDouble();
			double vy = sc.nextDouble();
			Particle particle = particles.poll();
			assert particle != null;
			particle.setPosition(new Vector2D(x, y));
			particle.setVelocity(new Vector2D(vx, vy));
			particles.add(particle);
		}
		sc.close();
		return true;
	}

	public List<Particle> getParticles() {
		return new ArrayList<>(particles);
	}

}
