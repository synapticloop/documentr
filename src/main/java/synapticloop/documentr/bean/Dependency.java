package synapticloop.documentr.bean;

public class Dependency {
	private final String group;
	private String project = null;
	private String version = null;
	private boolean isInternal = false;

	public Dependency(String dependency) {
		if(dependency.endsWith("()")) {
			// we have a built in api
			this.isInternal = true;
			this.group = dependency;
			return;
		}

		String[] splits = dependency.split(":");

		this.group = splits[0];
		this.project = splits[1];
		this.version = splits[2];
	}

	public String getGroup() { return this.group; }
	public String getProject() { return this.project; }
	public String getVersion() { return this.version; }
	public boolean getIsInternal() {return (this.isInternal); }

}
