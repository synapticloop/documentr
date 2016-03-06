package synapticloop.documentr.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class DependencyGroup {
	public HashMap<String, List<Dependency>> dependencyGroups = new LinkedHashMap<String, List<Dependency>>();

	public DependencyGroup() {
	}

	public void addDependency(String group, String dependency) {
		String dependencyTemp = dependency.replaceAll("\"", "").replaceAll("'", "");
		List<Dependency> dependencies = null;
		if(!dependencyGroups.containsKey(group)) {
			dependencies = new ArrayList<Dependency>();
		} else {
			dependencies = dependencyGroups.get(group);
		}

		dependencies.add(new Dependency(dependencyTemp));
		dependencyGroups.put(group, dependencies);
	}

	public HashMap<String, List<Dependency>> getDependencyGroups() {
		return(dependencyGroups);
	}
}
