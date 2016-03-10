package synapticloop.documentr;

import java.io.File;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import synapticloop.documentr.exception.DocumentrException;
import synapticloop.documentr.generator.Generator;
import synapticloop.util.SimpleUsage;

public class Main {

	public static void main(String[] args) {
		Options options = new Options();

		options.addOption("h", "help", false, "Print out the usage and help message.");
		options.addOption("v", "verbose", true, "Output more verbose information");
		options.addOption("d", "directory", true, "The directory in which the 'documentr.json' "
				+ "file resides, default the current working directory (i.e. '.')");
		options.addOption("e", "extension", true, "The extension for the README file, default '.md'.");

		boolean verbose = false;
		String directory = ".";
		String extension = "md";

		CommandLineParser parser = new DefaultParser();
		try {
			// parse the command line arguments
			CommandLine line = parser.parse(options, args);
			if(line.hasOption("h")) {
				SimpleUsage.helpAndExit();
			}

			// at this point we are going to do the generation
			if(line.hasOption("v")) {
				verbose = true;
			}

			if(line.hasOption("d")) {
				directory = line.getOptionValue("d");
			}

			if(line.hasOption("e")) {
				extension = line.getOptionValue("e");
				if(extension.startsWith(".")) {
					extension = extension.substring(0, 1);
				}
			}

		} catch(ParseException ex) {
			SimpleUsage.usageAndExit(String.format("Could not parse the command line, message was: %s", ex.getMessage()));
		}

		File rootDirectory = new File(directory);
		Generator generator = new Generator(rootDirectory, extension, verbose);

		try {
			generator.generate();
		} catch (DocumentrException ex) {
			SimpleUsage.usageAndExit(ex);
		}
	}

}
