import algorithms.EventDrivenMolecularDynamics;
import com.google.devtools.common.options.OptionsParser;
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
		if (options.time <= 0
				|| options.M <= 0
				|| options.staticFile.isEmpty()
				|| options.dynamicFile.isEmpty()) {
			printUsage(parser);
		}

		// Parse static and dynamic files
		Parser staticAndDynamicParser = new Parser(options.staticFile, options.dynamicFile);
		if (!staticAndDynamicParser.parse()) return;

		List<Particle> particles = staticAndDynamicParser.getParticles();

		// Run algorithm
		runAlgorithm(
				particles,
				staticAndDynamicParser.getBoxSize(),
				options.M,
				options.time
		);
	}

	private static void runAlgorithm(List<Particle> particles,
	                                 double L,
	                                 int M,
	                                 int time) throws CloneNotSupportedException {

		StringBuffer buffer = new StringBuffer();
		long startTime = System.currentTimeMillis();

		EventDrivenMolecularDynamics.run(
				particles,
				L,
				M,
				time,
				buffer
		);

		long stopTime = System.currentTimeMillis();
		long elapsedTime = stopTime - startTime;

		System.out.println("Event Driven Molecular Dynamics execution time: " + elapsedTime + "ms");

		OvitoWriter<Particle> ovitoWriter;
		try {
			ovitoWriter = new OvitoWriter<>(Paths.get("ovito_file.txt"));
			ovitoWriter.writeBuffer(buffer);
			ovitoWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

//		System.out.println(OffLattice.getOrderValues().peek());

//		OctaveWriter octaveWriter;
//		try {
//			octaveWriter = new OctaveWriter(Paths.get("particular_va_file.txt"));
//			octaveWriter.writeOrderValuesThroughIterations(OffLattice.getOrderValues());
//			octaveWriter.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}

	private static void printUsage(OptionsParser parser) {
		System.out.println("Usage: java -jar molecular-dynamics-simulation-1.0-SNAPSHOT.jar OPTIONS");
		System.out.println(parser.describeOptions(Collections.emptyMap(),
				OptionsParser.HelpVerbosity.LONG));
	}

}
