package synapticloop.documentr.plugin;

public class DocumentrPluginExtension {
	private String directory = ".";
	private boolean verbose = false;

	public String getDirectory() {
		return(directory);
	}

	public void setDirectory(String directory) {
		this.directory = directory;
	}

	public boolean getVerbose() {
		return this.verbose;
	}

	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}
}
