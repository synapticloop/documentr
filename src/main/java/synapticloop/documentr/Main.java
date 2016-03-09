package synapticloop.documentr;

import java.io.File;

import synapticloop.documentr.exception.DocumentrException;
import synapticloop.documentr.generator.Generator;
import synapticloop.util.SimpleUsage;

public class Main {
// wtf
	public static void main(String[] args) {
		System.out.println("asdlkj");
		String directory = ".";
		switch(args.length) {
		case 0:
			// using the current directory
			break;
		case 1:
			if("--help".equals(args[0])) {
				SimpleUsage.helpAndExit();
			} else {
				directory = args[0];
			}
			break;
		default:
			SimpleUsage.usageAndExit("Incorrect arguments");
		}

		File rootDirectory = new File(directory);
		Generator generator = new Generator(rootDirectory);

		try {
			generator.generate();
		} catch (DocumentrException ex) {
			SimpleUsage.usageAndExit(ex);
		}
	}

}
