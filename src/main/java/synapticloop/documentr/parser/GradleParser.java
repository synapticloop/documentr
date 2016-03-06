package synapticloop.documentr.parser;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;

import synapticloop.documentr.bean.DependencyGroup;
import synapticloop.templar.utils.TemplarContext;
import synapticloop.util.SimpleLogger;

public class GradleParser {
	private static final SimpleLogger LOGGER = SimpleLogger.getLoggerSimpleName(GradleParser.class);

	public GradleParser(TemplarContext templarContext, File file) {
		if(file.exists() && file.canRead() && file.isFile()) {
			// try and parse it
			try {
				List<String> readLines = FileUtils.readLines(file);
				Iterator<String> iterator = readLines.iterator();
				while (iterator.hasNext()) {
					String line = iterator.next().trim();
					if(line.startsWith("group")) {
						addToContext(templarContext, "group", line);
					} else if(line.startsWith("version")) {
						addToContext(templarContext, "version", line);
					} else if(line.startsWith("description")) {
						addToContext(templarContext, "description", line);
					} else if(line.startsWith("archivesBaseName")) {
						addToContext(templarContext, "project", line);
					} else if(line.startsWith("dependencies")) {
						addToContext(templarContext, iterator, "dependencyGroup");
					}
				}
			} catch (IOException ex) {
				// TODO Auto-generated catch block
				ex.printStackTrace();
			}
		}
	}

	private void addToContext(TemplarContext templarContext, Iterator<String> iterator, String key) {
		DependencyGroup dependencyGroup = new DependencyGroup();
		while (iterator.hasNext()) {
			String line = iterator.next().trim();
			if(line.startsWith("}")) {
				templarContext.add("dependencyGroup", dependencyGroup);
				return;
			}

			if(line.startsWith("runtime")) {
				String dependency = line.split(" ")[1];
				dependencyGroup.addDependency("runtime", dependency);
				LOGGER.info(String.format("Found 'runtime' dependency of '%s'", dependency));
			} else if(line.startsWith("compile")) {
				String dependency = line.split(" ")[1];
				dependencyGroup.addDependency("compile", dependency);
				LOGGER.info(String.format("Found 'compile' dependency of '%s'", dependency));
			}
		}
	}

	private void addToContext(TemplarContext templarContext, String key, String line) {
		LOGGER.info(String.format("Looking for key '%s' in line '%s'", key, line));
		String[] split = line.trim().split("=", 2);
		if(split.length != 2) {
			LOGGER.info(String.format("Could not find splittable '=' for key '%s' in line '%s', ignoring...", key, line));
			return;
		}

		String value = split[1].trim().replaceAll("\"", "").replaceAll("'", "");
		templarContext.add(key, value);
		LOGGER.info(String.format("Added '%s':'%s' to context", key, value));
	}

}
