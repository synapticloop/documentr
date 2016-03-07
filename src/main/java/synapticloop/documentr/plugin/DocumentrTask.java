package synapticloop.documentr.plugin;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

public class DocumentrTask extends DefaultTask {
	@TaskAction

	public void generate() {
		DocumentrPluginExtension extension = getProject().getExtensions().findByType(DocumentrPluginExtension.class);

		if (extension == null) {
			extension = new DocumentrPluginExtension();
		}

		String directory = extension.getDirectory();
		System.out.println(directory);
	}
}
