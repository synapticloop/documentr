package synapticloop.documentr.bean;

import java.util.ArrayList;
import java.util.List;

import org.gradle.api.artifacts.Dependency;
import org.gradle.api.artifacts.DependencySet;

public class ConfigurationBean {
	private final String name;
	private final List<Dependency> dependencies = new ArrayList<Dependency>();

	public ConfigurationBean(String name) {
		this.name = name;
	}

	public void addDependency(DependencySet dependencySet) {
		for (Dependency dependency : dependencySet) {
			dependencies.add(dependency);
		}
	}

	public String getName() {
		return this.name;
	}

	public List<Dependency> getDependencies() {
		return this.dependencies;
	}
}
