package synapticloop.documentr;

import java.io.File;

import synapticloop.documentr.exception.DocumentrException;
import synapticloop.documentr.generator.Generator;
import synapticloop.util.SimpleUsage;

public class Main {

	public static void main(String[] args) {
		if(args.length != 1) {
			SimpleUsage.usageAndExit(null);
		}

		String option = args[0];
		if("--help".equals(option)) {
			SimpleUsage.usageAndExit(null);
		}

		File rootDirectory = new File(option);
		Generator generator = new Generator(rootDirectory);
		try {
			generator.generate();
		} catch (DocumentrException ex) {
			ex.printStackTrace();
			SimpleUsage.usageAndExit(ex.getMessage());
		}
	}

}
