package synapticloop.documentr;

import java.io.File;

import synapticloop.documentr.exception.DocumentrException;
import synapticloop.documentr.generator.Generator;
import synapticloop.util.SimpleUsage;

public class Main {
private static final String ARG_OPTION_VERBOSE = "--verbose";
private static final String ARG_OPTION_HELP = "--help";

// wtf
	public static void main(String[] args) {
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
