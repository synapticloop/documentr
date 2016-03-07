package synapticloop.documentr.generator;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Iterator;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import synapticloop.documentr.exception.DocumentrException;
import synapticloop.documentr.parser.GradleParser;
import synapticloop.templar.Parser;
import synapticloop.templar.exception.ParseException;
import synapticloop.templar.exception.RenderException;
import synapticloop.templar.utils.TemplarContext;

public class Generator {
	private static final String VALUE = "value";
	private static final String TYPE = "type";

	private final File rootDirectory;

	public Generator(File rootDirectory) {
		this.rootDirectory = rootDirectory;
	}

	public void generate() throws DocumentrException {
		//at this point we have a directory - make sure we can find a documentr.json file 
		File documentrJsonFile = new File(rootDirectory.getAbsolutePath() + "/documentr.json");

		if(documentrJsonFile.exists() && documentrJsonFile.canRead()) {
			try {
				StringBuilder stringBuilder = new StringBuilder();

				JSONObject jsonObject = new JSONObject(FileUtils.readFileToString(documentrJsonFile));
				JSONArray jsonArray = jsonObject.getJSONArray("templates");

				for (Object object : jsonArray) {
					JSONObject templateObject = (JSONObject)object;
					if("inbuilt".equals(templateObject.getString(TYPE))) {
						stringBuilder.append("{import classpath:/");
						stringBuilder.append(templateObject.getString(VALUE));
						stringBuilder.append(".templar}\n");
					} else if("file".equals(templateObject.getString(TYPE))) {
						stringBuilder.append("{pre\n");
						stringBuilder.append(FileUtils.readFileToString(new File(documentrJsonFile.getParent() + "/" + templateObject.getString(VALUE))));
						stringBuilder.append("\npre}\n");
					} else if("markup".equals(templateObject.getString(TYPE))) {
						stringBuilder.append("\n");
						stringBuilder.append(templateObject.getString(VALUE).replaceAll("\\{", "\\{\\{").replaceAll("\\n", "\\{\\\\n\\}").replaceAll("\\t", "\\{\\\\t\\}"));
						stringBuilder.append("\n");
					} else if("template".equals(templateObject.getString(TYPE))) {
						stringBuilder.append("\n");
						stringBuilder.append("{import ");
						stringBuilder.append(documentrJsonFile.getParent() + "/" + templateObject.getString(VALUE));
						stringBuilder.append("}\n");
						stringBuilder.append("\n");
					}
				}

				TemplarContext templarContext = new TemplarContext();
				templarContext.add("yearTo", Calendar.getInstance().get(Calendar.YEAR));

				// now we need to see whether we have a build.gradle
				File gradleBuildFile = new File(documentrJsonFile.getParent() + "/build.gradle");

				new GradleParser(templarContext, gradleBuildFile);

				// now override
				overrideContext(templarContext, jsonObject);

				Parser parser = new Parser(stringBuilder.toString());
				FileUtils.writeStringToFile(new File(documentrJsonFile.getParent() + "/README.md"), parser.render(templarContext));

			} catch (IOException | ParseException | RenderException ex) {
				throw new DocumentrException("Cannot parse/render the documentr.json file, message was: " + ex.getMessage());
			}
		} else {
			throw new DocumentrException("Cannot find the documentr.json file.");
		}
	}

	private void overrideContext(TemplarContext templarContext, JSONObject jsonObject) {
		JSONObject contextObject = jsonObject.getJSONObject("context");
		Iterator<String> keys = contextObject.keys();
		while (keys.hasNext()) {
			String key = (String) keys.next();
			templarContext.add(key, contextObject.get(key));
		}
	}
}
