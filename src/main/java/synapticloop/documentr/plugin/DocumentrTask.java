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
import org.gradle.api.tasks.TaskAction;

import synapticloop.documentr.exception.DocumentrException;
import synapticloop.documentr.generator.Generator;

public class DocumentrTask extends DefaultTask {
	/**
	 * Instantiate the task, setting the group and description
	 */
	public DocumentrTask() {
		super.setGroup("Documentation");
		super.setDescription("Generates a README file for your project.");
	}

	/**
	 * Generate the README file from the documentr.json input file
	 * 
	 * @throws DocumentrException If there was an error parsing/rendering the 
	 *     README file
	 */
	@TaskAction
	public void generate() throws DocumentrException {
		DocumentrPluginExtension extension = getProject().getExtensions().findByType(DocumentrPluginExtension.class);

		if (extension == null) {
			extension = new DocumentrPluginExtension();
		}

		Generator generator = new Generator(getProject(), extension);
		generator.generate();
	}
}
