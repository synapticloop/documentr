package synapticloop.documentr;

import java.io.File;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import synapticloop.documentr.exception.DocumentrException;
import synapticloop.documentr.generator.Generator;
import synapticloop.util.SimpleUsage;

public class Main {
	private static final String ARG_OPTION_VERBOSE = "--verbose";
	private static final String ARG_OPTION_HELP = "--help";
	private static final String ARG_OPTION_DIRECTORY = "--directory";
	private static final String ARG_OPTION_EXTENSION = "--extension";

	public static void main(String[] args) {
		Options options = new Options();

		OptionGroup helpOptionGroup = new OptionGroup();
		helpOptionGroup.addOption(new Option("h", "help", false, "Print out the usage and help message."));

		options.addOptionGroup(helpOptionGroup);

		OptionGroup generateOptionGroup = new OptionGroup();
		generateOptionGroup.addOption(new Option("v", "verbose", true, "Output more verbose information"));
		generateOptionGroup.addOption(new Option("d", "directory", true, "The directory in which the 'documentr.json' "
				+ "file resides, default the current working directory (i.e. '.')"));
		generateOptionGroup.addOption(new Option("e", "extension", true, "The extension for the README file, default '.md'."));
		options.addOptionGroup(generateOptionGroup);

		CommandLineParser parser = new DefaultParser();
		try {
			// parse the command line arguments
			CommandLine line = parser.parse( options, args );
			if(line.hasOption("h")) {
				SimpleUsage.helpAndExit();
			}
			
			
		} catch(ParseException ex) {
			SimpleUsage.usageAndExit(String.format("Could not parse the command line, message was: %s", ex.getMessage()));
		}

		boolean debug = false;
		String directory = ".";

		switch(args.length) {
		case 0:
			// using the current directory
			break;
		case 1:
			if(ARG_OPTION_HELP.equals(args[0])) {
				SimpleUsage.helpAndExit();
			} else if(ARG_OPTION_VERBOSE.equals(args[0])) {
				debug = true;
			} else {
				directory = args[0];
			}
			break;
		case 2:
			// this can only be --debug, then a directory
			if(ARG_OPTION_VERBOSE.equals(args[0])) {
				debug = true;
				if(ARG_OPTION_HELP.equals(args[1])) {
					SimpleUsage.usageAndExit("You may not use the flag '--verbose' and '--help'");
				} else {
					directory = args[1];
				}
			} else {
				SimpleUsage.usageAndExit("Incorrect arguments, when two arguments are passed in, the first __MUST__ be '--verbose'");
			}
			break;
		default:
			SimpleUsage.usageAndExit("Incorrect arguments");
		}

		File rootDirectory = new File(directory);
		Generator generator = new Generator(rootDirectory, debug);

		try {
			generator.generate();
		} catch (DocumentrException ex) {
			SimpleUsage.usageAndExit(ex);
		}
	}

}
