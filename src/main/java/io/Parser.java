package io;

import models.Particle;

import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 * Parser for dynamic file.
 */
public class Parser {

	private String dynamicFilePath;

	private int numberOfParticles = 0;
	private List<Particle> particles;

	public Parser(String dynamicFilePath) {
		this.dynamicFilePath = dynamicFilePath;
		this.particles = new LinkedList<>();
	}

	public boolean parse() {
		return parseDynamicFile();
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

		sc.nextInt();   // t0 not used

		int id = 1;
		while (sc.hasNextDouble()) {
			double x = sc.nextDouble();
			double y = sc.nextDouble();
			double angle = sc.nextDouble();
			Particle p = new Particle(id++, angle);
			p.setPosition(new Point2D.Double(x, y));
			particles.add(p);
			numberOfParticles++;
		}
		sc.close();
		return true;
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

	public List<Particle> getParticles() {
		return particles;
	}

	public void setParticles(List<Particle> particles) {
		this.particles = particles;
	}
}
