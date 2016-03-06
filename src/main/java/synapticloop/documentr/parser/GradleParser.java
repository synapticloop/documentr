package synapticloop.documentr.parser;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;

import synapticloop.templar.utils.TemplarContext;
import synapticloop.util.SimpleLogger;

public class GradleParser {
	private static final SimpleLogger LOGGER = SimpleLogger.getLoggerSimpleName(GradleParser.class);

	public GradleParser(TemplarContext templarContext, File file) {
		if(file.exists() && file.canRead() && file.isFile()) {
			// try and parse it
			try {
				List<String> readLines = FileUtils.readLines(file);
				for (String line : readLines) {
					if(line.trim().startsWith("group")) {
						addToContext(templarContext, "group", line);
					} else if(line.trim().startsWith("version")) {
						addToContext(templarContext, "version", line);
					} else if(line.trim().startsWith("description")) {
						addToContext(templarContext, "description", line);
					} else if(line.trim().startsWith("archivesBaseName")) {
						addToContext(templarContext, "project", line);
					}
				}
			} catch (IOException ex) {
				// TODO Auto-generated catch block
				ex.printStackTrace();
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
