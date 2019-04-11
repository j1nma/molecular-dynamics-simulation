import algorithms.EventDrivenMolecularDynamics;
import com.google.devtools.common.options.OptionsParser;
import io.OvitoWriter;
import io.Parser;
import io.SimulationOptions;
import models.Particle;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

public class App {

	private static final String OUTPUT_DIRECTORY = "./output";
	private static final String OVITO_FILE = OUTPUT_DIRECTORY + "/ovito_file.txt";
	private static final String COLLISION_DIRECTORY = OUTPUT_DIRECTORY + "/collisionFrequency";
	private static final String COLLISION_FREQUENCY_FILE = COLLISION_DIRECTORY + "/collision_frequency.txt";
	private static final String SPEEDS_DIRECTORY = OUTPUT_DIRECTORY + "/lastThirdSpeeds";
	private static final String INITIAL_SPEEDS_FILE = SPEEDS_DIRECTORY + "/initial_speeds.txt";
	private static final String LAST_THIRD_SPEEDS_FILE = SPEEDS_DIRECTORY + "/last_third_speeds.txt";
	private static final String TRAJECTORY_DIRECTORY = OUTPUT_DIRECTORY + "/bigParticleTrajectory";
	private static final String TRAJECTORY_FILE = TRAJECTORY_DIRECTORY + "/trajectory.txt";
	private static final String BIG_PARTICLE_DIFFUSION_DIRECTORY = OUTPUT_DIRECTORY + "/bigParticleDiffusion";
	private static final String BIG_PARTICLE_DIFFUSION_FILE = BIG_PARTICLE_DIFFUSION_DIRECTORY + "/big_particle_diffusion.txt";
	private static PrintWriter eventWriter;
	private static PrintWriter initialSpeedsWriter;
	private static PrintWriter lastThirdSpeedsWriter;
	private static PrintWriter bigParticleTrajectoryWriter;
	private static PrintWriter bigParticleDiffusionWriter;

	public static void main(String[] args) throws IOException {

		// Create directories
		new File(OUTPUT_DIRECTORY).mkdirs();
		new File(COLLISION_DIRECTORY).mkdirs();
		new File(SPEEDS_DIRECTORY).mkdirs();
		new File(TRAJECTORY_DIRECTORY).mkdirs();
		new File(BIG_PARTICLE_DIFFUSION_DIRECTORY).mkdirs();

		// Parse command line options
		OptionsParser parser = OptionsParser.newOptionsParser(SimulationOptions.class);
		parser.parseAndExitUponError(args);
		SimulationOptions options = parser.getOptions(SimulationOptions.class);
		assert options != null;
		if (options.time <= 0
				|| options.maxEvents <= 0
				|| options.boxSize <= 0
				|| options.staticFile.isEmpty()
				|| options.dynamicFile.isEmpty()) {
			printUsage(parser);
		}

		// Parse static and dynamic files
		Parser staticAndDynamicParser = new Parser(options.staticFile, options.dynamicFile);
		if (!staticAndDynamicParser.parse()) return;
		List<Particle> particles = staticAndDynamicParser.getParticles();

		// Initialize file writers
		eventWriter = new PrintWriter(new FileWriter(COLLISION_FREQUENCY_FILE));
		initialSpeedsWriter = new PrintWriter(new FileWriter(INITIAL_SPEEDS_FILE));
		lastThirdSpeedsWriter = new PrintWriter(new FileWriter(LAST_THIRD_SPEEDS_FILE));
		bigParticleTrajectoryWriter = new PrintWriter(new FileWriter(TRAJECTORY_FILE));
		bigParticleDiffusionWriter = new PrintWriter(new FileWriter(BIG_PARTICLE_DIFFUSION_DIRECTORY));

		// Run algorithm
		runAlgorithm(
				particles,
				options.boxSize,
				options.time,
				options.maxEvents
		);
	}

	private static void runAlgorithm(List<Particle> particles,
	                                 double L,
	                                 double limitTime,
	                                 int maxEvents) {

		StringBuffer buffer = new StringBuffer();
		long startTime = System.currentTimeMillis();

		// Print temperature (constant over time)
		System.out.println("Temperature (K):\t" + EventDrivenMolecularDynamics.calculateTemperature(particles));

		EventDrivenMolecularDynamics.run(
				particles,
				L,
				limitTime,
				maxEvents,
				buffer,
				eventWriter,
				initialSpeedsWriter,
				lastThirdSpeedsWriter,
				bigParticleTrajectoryWriter,
				bigParticleDiffusionWriter
		);

		long stopTime = System.currentTimeMillis();
		long elapsedTime = stopTime - startTime;

		System.out.println("======================== Results ========================");
		System.out.println("Event Driven Molecular Dynamics execution time (ms):\t" + elapsedTime);

		OvitoWriter<Particle> ovitoWriter;
		try {
			ovitoWriter = new OvitoWriter<>(Paths.get(OVITO_FILE));
			ovitoWriter.writeBuffer(buffer);
			ovitoWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("Average time between collisions (s):\t" +
				EventDrivenMolecularDynamics.getAverageTimeBetweenCollisions());
	}

	private static void printUsage(OptionsParser parser) {
		System.out.println("Usage: java -jar molecular-dynamics-simulation-1.0-SNAPSHOT.jar OPTIONS");
		System.out.println(parser.describeOptions(Collections.emptyMap(),
				OptionsParser.HelpVerbosity.LONG));
	}

}
