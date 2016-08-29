package synapticloop.documentr.bean;

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

import java.util.ArrayList;
import java.util.List;

import org.gradle.api.artifacts.Dependency;
import org.gradle.api.artifacts.DependencySet;

/**
 * This is a simple POJO which holds a named configuration and all of the
 * dependencies for the configuration.
 * 
 * @author synapticloop
 *
 */
public class ConfigurationBean {
	private final String name;
	private final List<Dependency> dependencies = new ArrayList<Dependency>();

	/**
	 * The name of the configuration for the dependencies
	 * 
	 * @param name the name of the configuration
	 */
	public ConfigurationBean(String name) {
		this.name = name;
	}

	/**
	 * Add dependencies to this specific configuration
	 * 
	 * @param dependencySet the set of dependencies to add
	 */
	public void addDependencies(DependencySet dependencySet) {
		for (Dependency dependency : dependencySet) {
			dependencies.add(dependency);
		}
	}

	/**
	 * Get the name of the configuration
	 * 
	 * @return the name of the configuration
	 */
	public String getName() { return this.name; }

	/**
	 * Get the dependencies for this configuration
	 * 
	 * @return the dependencies for this configuration
	 */
	public List<Dependency> getDependencies() { return this.dependencies; }
}
