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
			name = "staticFile",
			abbrev = 's',
			help = "Path to static file.",
			category = "startup",
			defaultValue = "/"
	)
	public String staticFile;

	@Option(
			name = "dynamicFile",
			abbrev = 'd',
			help = "Path to dynamic file.",
			category = "startup",
			defaultValue = "/"
	)
	public String dynamicFile;

	@Option(
			name = "time",
			abbrev = 't',
			help = "Time of simulation.",
			category = "startup",
			defaultValue = "100"
	)
	public int time;

}
