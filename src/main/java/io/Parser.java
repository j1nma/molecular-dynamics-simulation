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

	private int numberOfSmallParticles;
	private double boxSide;
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
		numberOfSmallParticles = sc.nextInt();
		for (int i = 0; i < numberOfSmallParticles; i++) {
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
		sc.nextInt();   // numberOfSmallParticles already caught in Static parser (in dynamic for ovito)
		boxSide = sc.nextDouble();
		for (int i = 0; i < numberOfSmallParticles; i++) {
			double x = sc.nextDouble();
			double y = sc.nextDouble();
			double vx = sc.nextDouble();
			double vy = sc.nextDouble();
			Particle particle = particles.poll();
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

	public int getNumberOfSmallParticles() {
		return numberOfSmallParticles;
	}

	public void setNumberOfSmallParticles(int numberOfSmallParticles) {
		this.numberOfSmallParticles = numberOfSmallParticles;
	}

	public double getBoxSide() {
		return boxSide;
	}

	public void setBoxSide(double boxSide) {
		this.boxSide = boxSide;
	}

	public List<Particle> getParticles() {
		return (List<Particle>) particles;
	}

	public void setParticles(Queue<Particle> particles) {
		this.particles = particles;
	}
}
