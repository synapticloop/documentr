package synapticloop.documentr;

/*
 * Copyright (c) 2016 Synapticloop.
 * 
 * All rights reserved.
 * 
 * This code may contain contributions from other parties which, where 
 * applicable, will be listed in the default build file for the project 
 * ~and/or~ in a file named CONTRIBUTORS.txt in the root of the project.
 * 
 * This source code and any derived binaries are covered by the terms and 
 * conditions of the Licence agreement ("the Licence").  You may not use this 
 * source code or any derived binaries except in compliance with the Licence.  
 * A copy of the Licence is available in the file named LICENSE.txt shipped with 
 * this source code or binaries.
 */

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

	private static boolean verbose = false;
	private static String directory = ".";
	private static String extension = "md";

	private static void parseCommandLineOptions(String[] args) throws ParseException {
		Options options = new Options();

		options.addOption("h", "help", false, "Print out the usage and help message.");
		options.addOption("v", "verbose", true, "Output more verbose information");
		options.addOption("d", "directory", true, "The directory in which the 'documentr.json' "
				+ "file resides, default the current working directory (i.e. '.')");
		options.addOption("e", "extension", true, "The extension for the README file, default '.md'.");

		CommandLineParser parser = new DefaultParser();
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

	}

	/**
	 * The main entry point for the documentr process
	 * 
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		try {
			parseCommandLineOptions(args);
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
