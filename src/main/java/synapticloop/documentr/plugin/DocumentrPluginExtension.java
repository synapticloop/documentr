package synapticloop.documentr.plugin;

public class DocumentrPluginExtension {
	private boolean verbose = false;

	private String directory = ".";
	private String extension = ".md";

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

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}
}
