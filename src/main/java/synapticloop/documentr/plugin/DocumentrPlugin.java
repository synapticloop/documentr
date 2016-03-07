package synapticloop.documentr.plugin;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class DocumentrPlugin implements Plugin<Project> {


	@Override
	public void apply(Project project) {
		project.getExtensions().create("documentrSetting", DocumentrPluginExtension.class);
		project.getTasks().create("documentr", DocumentrTask.class);
	}

}
