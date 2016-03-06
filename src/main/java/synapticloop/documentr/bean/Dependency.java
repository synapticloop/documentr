package synapticloop.documentr.bean;

public class Dependency {
	private final String group;
	private final String project;
	private final String version;

	public Dependency(String dependency) {
		String[] splits = dependency.split(":");
		this.group = splits[0];
		this.project = splits[1];
		this.version = splits[2];
	}

	public String getGroup() {
		return this.group;
	}

	public String getProject() {
		return this.project;
	}

	public String getVersion() {
		return this.version;
	}

}
