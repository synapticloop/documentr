package synapticloop.documentr.plugin;

/*
 * Copyright (c) 2016 - 2019 Synapticloop.
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

import org.gradle.api.DefaultTask;
import org.gradle.api.Project;
import org.gradle.api.logging.Logger;
import org.gradle.api.tasks.TaskAction;

public class DocumentrHelpTask extends DefaultTask {
	private Logger logger;
	private Project project;

	/**
	 * Instantiate the task, setting the group and description
	 */
	public DocumentrHelpTask() {
		super.setGroup("Documentation");
		super.setDescription("Documentr - prints out a help message");

		this.project = getProject();
		this.logger = project.getLogger();
	}

	/**
	 * Print out all of the templates that are available
	 * 
	 */
	@TaskAction
	public void help() {
		logger.lifecycle("Found the following in-built templates:");

		logger.lifecycle("  attribution");
		logger.lifecycle("  badge-bintray");
		logger.lifecycle("  badge-shield-io-github-release");
		logger.lifecycle("  badge-shield-io-gradle-plugin");
		logger.lifecycle("  badge-travis-ci");
		logger.lifecycle("  dependencies");
		logger.lifecycle("  dumpcontext");
		logger.lifecycle("  gradle-build");
		logger.lifecycle("  gradle-plugin-usage");
		logger.lifecycle("  gradle-test");
		logger.lifecycle("  jvm-compatability");
		logger.lifecycle("  license-apache-2.0");
		logger.lifecycle("  license-bsd-2-clause");
		logger.lifecycle("  license-bsd-3-clause");
		logger.lifecycle("  license-mit");
		logger.lifecycle("  logging-slf4j");
		logger.lifecycle("  project-description");
		logger.lifecycle("  project-name");
		logger.lifecycle("  publishing-all-in-one-jar");
		logger.lifecycle("  publishing-bintray");
		logger.lifecycle("  publishing-github");
		logger.lifecycle("  publishing-gradle-plugin");
		logger.lifecycle("  publishing-jitpack");
		logger.lifecycle("  publishing-maven");
		logger.lifecycle("  test-warn");
		logger.lifecycle("  test-warning");

		logger.lifecycle("Which can be included in your 'documentr.json' file thusly:");
		logger.lifecycle("\t\t{ \"type\":\"inbuilt\", \"value\":\"badge-travis-ci\" }");
	}
}
