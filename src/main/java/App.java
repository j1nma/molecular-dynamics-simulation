import algorithms.OffLattice;
import com.google.devtools.common.options.OptionsParser;
import io.OctaveWriter;
import io.OvitoWriter;
import io.Parser;
import io.SimulationOptions;
import models.Particle;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

public class App {

	public static void main(String[] args) throws CloneNotSupportedException {
		// Parse command line options
		OptionsParser parser = OptionsParser.newOptionsParser(SimulationOptions.class);
		parser.parseAndExitUponError(args);
		SimulationOptions options = parser.getOptions(SimulationOptions.class);
		assert options != null;
		if (options.rc < 0
				|| options.noise < 0
				|| options.boxSide < 0
				|| options.speed < 0
				|| options.time <= 0
				|| options.M <= 0
				|| options.dynamicFile.isEmpty()) {
			printUsage(parser);
		}

		// Parse dynamic file
		Parser dynamicParser = new Parser(options.dynamicFile);
		if (!dynamicParser.parse()) return;

		List<Particle> particles = dynamicParser.getParticles();

		// Validate matrix size meets non-punctual criteria
		if (!BoxSizeMeetsCriteria(options.M,
				options.boxSide,
				options.rc)) {
			System.out.println("L / M > interactionRadius failed.");
			return;
		}

		// Run algorithm
		runAlgorithm(
				particles,
				options.boxSide,
				options.M,
				options.rc,
				options.time,
				options.noise,
				options.speed
		);
	}

	private static void runAlgorithm(List<Particle> particles,
	                                 double L,
	                                 int M,
	                                 double interactionRadius,
	                                 int time,
	                                 double noise,
	                                 double speed) throws CloneNotSupportedException {

		StringBuffer buffer = new StringBuffer();
		long startTime = System.currentTimeMillis();

		OffLattice.run(
				particles,
				L,
				M,
				interactionRadius,
				time,
				noise,
				speed,
				buffer
		);

		long stopTime = System.currentTimeMillis();
		long elapsedTime = stopTime - startTime;

		System.out.println("Off-Lattice Method execution time: " + elapsedTime + "ms");

		OvitoWriter<Particle> ovitoWriter;
		try {
			ovitoWriter = new OvitoWriter<>(Paths.get("ovito_file.txt"));
			ovitoWriter.writeBuffer(buffer);
			ovitoWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println(OffLattice.getOrderValues().peek());

		OctaveWriter octaveWriter;
		try {
			octaveWriter = new OctaveWriter(Paths.get("particular_va_file.txt"));
			octaveWriter.writeOrderValuesThroughIterations(OffLattice.getOrderValues());
			octaveWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static void printUsage(OptionsParser parser) {
		System.out.println("Usage: java -jar tp2-1.0-SNAPSHOT.jar OPTIONS");
		System.out.println(parser.describeOptions(Collections.emptyMap(),
				OptionsParser.HelpVerbosity.LONG));
	}

	private static boolean BoxSizeMeetsCriteria(int M, double L, double interactionRadius) {
		return M > 0 && L / M > interactionRadius;
	}
}
