package algorithms;

import models.Particle;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Off-Lattice implementation
 */
public class EventDrivenMolecularDynamics {

	/**
	 * Cells from 0 to MxM - 1.
	 * Each one has a list of CellParticles from that cell number.
	 * A CellParticle contains a Particle and the cell's position.
	 */
	private static List<List<CellParticle>> cells = new ArrayList<>();
	private static int M;

	private static PriorityQueue<Event> pq = new PriorityQueue<Event>();
	private static double L;
	private static int tc;

	public static void run(
			List<Particle> particlesFromDynamic,
			double boxSize,
			int matrixSize,
			int limitTime,
			StringBuffer buff
	) throws CloneNotSupportedException {
		M = matrixSize;
		L = boxSize;
		makeMatrix();

		// Particles for fixing Ovito grid
		Particle dummy1 = new Particle(-1, 0, 0);
		Particle dummy2 = new Particle(0, 0, 0);
		dummy1.setPosition(new Point2D.Double(0, 0));
		dummy2.setPosition(new Point2D.Double(L, L));

		// Print dummy particles to simulation output file
		buff.append(particlesFromDynamic.size() + 2).append("\n")
				.append(0 + "\n")
				.append(particleToString(dummy1)).append("\n")
				.append(particleToString(dummy2)).append("\n");

		// Assign grid cell and print location
		for (Particle p : particlesFromDynamic) {
			assignCell(p);
			buff.append(particleToString(p)).append("\n");
		}

		// Save t = 0 order value


		// Main event-driven simulation loop

		// 1) The initial positions and speeds, radii and size of the box are defined with particlesFromDynamic and boxSize.

		// 2) The time until the first shock (event!) (Tc) is calculated.
		tc = calculateTimeUntilNextEvent();

		// 3) All the particles are evolved according to their equations of movement until tc.
		evolveParticlesUntilTc(particlesFromDynamic, tc);

		// 4) The state of the system (positions and speeds) is stored at t = tc
		storeSystemState(particlesFromDynamic, tc);

		// 5) With the "collision operator" the new speeds are determined after the collision, only for the particles that collided.

		// 6) Go to 2).


		// Below is previous run()
		// Do the off-lattice magic
		for (int time = 0; time < limitTime; time++) {
			// estoy en Tn y voy a calcular Tn+1
			List<List<CellParticle>> oldCells = cloneMatrix(cells);
			List<Particle> particles = new LinkedList<>();

			// calculamos vecinos
			for (List<CellParticle> cellParticles : cells) {
				for (CellParticle cp : cellParticles) {
					double cellX = cp.cellPosition.x;
					double cellY = cp.cellPosition.y;

					// Check neighbouring cells from inverted up-side down L shape
					visitNeighbour(cp.particle, cellX, cellY, cells, oldCells);
					visitNeighbour(cp.particle, cellX, cellY + 1, cells, oldCells);
					visitNeighbour(cp.particle, cellX + 1, cellY + 1, cells, oldCells);
					visitNeighbour(cp.particle, cellX + 1, cellY, cells, oldCells);
					visitNeighbour(cp.particle, cellX + 1, cellY - 1, cells, oldCells);
				}
			}

			//cambia positions
			for (List<CellParticle> cpList : cells) {
				for (CellParticle cp : cpList) {
					Particle particle = cp.particle;
					calculatePosition(particle);
					// For printing
					particles.add(particle);
				}
			}

			// imprime y blanquea para el siguiente clonado
			makeMatrix();
			buff.append(particles.size() + 2).append("\n")
					.append(time).append(1).append("\n")
					.append(particleToString(dummy1)).append("\n")
					.append(particleToString(dummy2)).append("\n");

			for (Particle particle : particles) {
				buff.append(particleToString(particle)).append("\n");
				particle.clearNeighbours();
				assignCell(particle);
			}

//			orderValues.push(getOrderValue(particles));
		}
	}

	private static void storeSystemState(List<Particle> particlesFromDynamic, int tc) {

	}

	private static void evolveParticlesUntilTc(List<Particle> particlesFromDynamic, int tc) {

	}

	private static int calculateTimeUntilNextEvent() {
		return 0;
	}

	private static List<List<CellParticle>> cloneMatrix(List<List<CellParticle>> cells) throws CloneNotSupportedException {
		List<List<CellParticle>> clone = new ArrayList<>();

		for (List<CellParticle> cellParticles : cells) {
			List<CellParticle> cloneCellParticles = new ArrayList<>();
			for (CellParticle cellParticle : cellParticles) {
				Particle particleClone = cellParticle.particle.getClone();
				cloneCellParticles.add(new CellParticle(particleClone,
						new Point2D.Double(cellParticle.cellPosition.x, cellParticle.cellPosition.y)));
			}
			clone.add(cloneCellParticles);
		}
		return clone;
	}

	private static void assignCell(Particle p) {
		// Calculate particle's cell indexes
		double cellX = Math.floor(p.getPosition().x / (L / M));
		double cellY = Math.floor(p.getPosition().y / (L / M));

		// Calculate particle's cell number
		int cellNumber = (int) (cellY * M + cellX);

		// Add particle to that cell with cell position
		cells.get(cellNumber).add(new CellParticle(p, new Point2D.Double(cellX, cellY)));
	}

	private static void makeMatrix() {
		cells = new ArrayList<>();
		// Create cell grid
		for (int i = 0; i < M * M; i++)
			cells.add(new ArrayList<>());
	}

	private static void calculatePosition(Particle p) {
//		p.getPosition().x += Math.cos(p.getAngle()) * s; TODO: correct
//		p.getPosition().y += Math.sin(p.getAngle()) * s;
		if (p.getPosition().x >= L) {
			p.getPosition().x -= L;
		}
		if (p.getPosition().y >= L) {
			p.getPosition().y -= L;
		}
		if (p.getPosition().x < 0) {
			p.getPosition().x += L;
		}
		if (p.getPosition().y < 0) {
			p.getPosition().y += L;
		}
	}

	private static void visitNeighbour(Particle particle, double cellX, double cellY, List<List<CellParticle>> cells, List<List<CellParticle>> oldCells) {

		// Reset neighbour cell indexes to comply with contour
		if (cellX >= M) {
			cellX = 0;
		}

		if (cellX < 0) {
			cellX = M - 1;
		}

		if (cellY >= M) {
			cellY = 0;
		}

		if (cellY < 0) {
			cellY = M - 1;
		}

		int neighbourCellNumber = (int) (cellY * M + cellX);

		List<CellParticle> neighbourCellParticles = oldCells.get(neighbourCellNumber);

		// itero por cada particula vecina en matrixA comparando con la particle que me dieron de matriz CELLS
		for (CellParticle neighbourCellParticle : neighbourCellParticles) {

			Particle neighbourParticle = neighbourCellParticle.particle;

			// chequeo que no sean la misma (una misma particula tiene dos objetos, mirar el id)
			if (neighbourParticle.getId() != particle.getId()) {

				if (particle.getPeriodicDistanceBetween(neighbourParticle, L) < rc) {
					// Mutually add both particles as neighbours
					particle.addNeighbour(neighbourParticle); //a la particula en cells le puse vecina a la de matrizA
					//neighbourParticle.addNeighbour(particle);
					boolean eject = false;
					for (List<CellParticle> cpList : cells) {
						for (CellParticle cp : cpList) {
							if (cp.particle.getId() == neighbourParticle.getId()) {
								neighbourParticle.addNeighbour(particle);
								eject = true;
								break;
							}
						}
						if (eject) break;
					}
				}
			}
		}
	}

	private static class CellParticle {
		Particle particle;
		Point2D.Double cellPosition;

		CellParticle(Particle particle, Point2D.Double cellPosition) {
			this.particle = particle;
			this.cellPosition = cellPosition;
		}
	}

	private static String particleToString(Particle p) {
		return p.getId() + " " +
				p.getPosition().x + " " +
				p.getPosition().y + " " +
				p.getVelocity().x + " " +
				p.getVelocity().y + " " //TODO: should colour come here as angle did in TP2?
				;
	}

	private static class Event {
	}
}
