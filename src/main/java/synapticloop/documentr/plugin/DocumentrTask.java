package synapticloop.documentr.plugin;

import java.io.File;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

import synapticloop.documentr.exception.DocumentrException;
import synapticloop.documentr.generator.Generator;

public class DocumentrTask extends DefaultTask {

	@TaskAction
	public void generate() throws DocumentrException {
		DocumentrPluginExtension extension = getProject().getExtensions().findByType(DocumentrPluginExtension.class);

		if (extension == null) {
			extension = new DocumentrPluginExtension();
		}

		Generator generator = new Generator(getProject(), new File(extension.getDirectory()), extension.getVerbose());
		generator.generate();
	}
}
