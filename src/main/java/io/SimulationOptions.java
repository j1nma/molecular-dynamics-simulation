package io;

import com.google.devtools.common.options.Option;
import com.google.devtools.common.options.OptionsBase;

/**
 * Command-line options definition for example server.
 */
public class SimulationOptions extends OptionsBase {

	@Option(
			name = "help",
			abbrev = 'h',
			help = "Prints usage info.",
			defaultValue = "false"
	)
	public boolean help;

	@Option(
			name = "matrix",
			abbrev = 'M',
			help = "Box division.",
			category = "startup",
			defaultValue = "10"
	)
	public int M;

	@Option(
			name = "radius",
			abbrev = 'r',
			help = "interaction radius",
			category = "startup",
			defaultValue = "0.5"
	)
	public double rc;

	@Option(
			name = "dynamicFile",
			abbrev = 'd',
			help = "Path to dynamic file.",
			category = "startup",
			defaultValue = "/"
	)
	public String dynamicFile;

	@Option(
			name = "noise",
			abbrev = 'n',
			help = "Noise in the environment",
			category = "startup",
			defaultValue = "0.1"
	)
	public double noise;

	@Option(
			name = "boxSide",
			abbrev = 'l',
			help = "The length of the box side",
			category = "startup",
			defaultValue = "20.0"
	)
	public double boxSide;

	@Option(
			name = "speed",
			abbrev = 's',
			help = "Speed module of the environment",
			category = "startup",
			defaultValue = "0.03"
	)
	public double speed;

	@Option(
			name = "time",
			abbrev = 't',
			help = "Time of simulation.",
			category = "startup",
			defaultValue = "100"
	)
	public int time;

}
