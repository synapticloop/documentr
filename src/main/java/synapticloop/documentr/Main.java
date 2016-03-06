package synapticloop.documentr;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import synapticloop.templar.Parser;
import synapticloop.templar.exception.ParseException;
import synapticloop.templar.exception.RenderException;
import synapticloop.templar.utils.TemplarContext;
import synapticloop.util.SimpleUsage;

public class Main {
	private static final String VALUE = "value";
	private static final String TYPE = "type";

	private static void usageAndExit(String message) {
		SimpleUsage.usage(message);
		printInbuiltTemplates();
		System.exit(-1);
	}

	private static void printInbuiltTemplates() {
	}

	private static TemplarContext getPopulatedContext(JSONObject jsonObject) {
		TemplarContext templarContext = new TemplarContext();
		
		JSONObject contextObject = jsonObject.getJSONObject("context");
		Iterator<String> keys = contextObject.keys();
		while (keys.hasNext()) {
			String key = (String) keys.next();
			templarContext.add(key, contextObject.get(key));
		}

		templarContext.add("description", "description");
		templarContext.add("group", "group");
		templarContext.add("project", "project");
		templarContext.add("version", "version");

		return(templarContext);
	}

	public static void main(String[] args) {
		if(args.length != 1) {
			usageAndExit(null);
		}

		String option = args[0];
		if("--help".equals(option)) {
			usageAndExit(null);
		}

		//at this point we have a directory - make sure we can find a documentr.json file 
		File file = new File(option + "/documentr.json");

		if(file.exists() && file.canRead()) {
			try {
				StringBuilder stringBuilder = new StringBuilder();

				JSONObject jsonObject = new JSONObject(FileUtils.readFileToString(file));
				JSONArray jsonArray = jsonObject.getJSONArray("templates");

				for (Object object : jsonArray) {
					JSONObject templateObject = (JSONObject)object;
					if("inbuilt".equals(templateObject.getString(TYPE))) {
						stringBuilder.append("{import classpath:/");
						stringBuilder.append(templateObject.getString(VALUE));
						stringBuilder.append(".templar}\n");
					} else if("file".equals(templateObject.getString(TYPE))) {
						stringBuilder.append("{pre\n");
						stringBuilder.append(FileUtils.readFileToString(new File(file.getParent() + "/" + templateObject.getString(VALUE))));
						stringBuilder.append("\npre}\n");
					} else if("markup".equals(templateObject.getString(TYPE))) {
						stringBuilder.append("\n");
						stringBuilder.append(templateObject.getString(VALUE).replaceAll("\\{", "\\{\\{").replaceAll("\\n", "\\{\\\\n\\}").replaceAll("\\t", "\\{\\\\t\\}"));
						stringBuilder.append("\n");
					} else if("template".equals(templateObject.getString(TYPE))) {
						stringBuilder.append("\n");
						stringBuilder.append("{import ");
						stringBuilder.append(file.getParent() + "/" + templateObject.getString(VALUE));
						stringBuilder.append("}\n");
						stringBuilder.append("\n");
					}

				}

				System.out.println(stringBuilder.toString());

				TemplarContext templarContext = getPopulatedContext(jsonObject);

				Parser parser = new Parser(stringBuilder.toString());
				FileUtils.writeStringToFile(new File(file.getParent() + "/README.md"), parser.render(templarContext));

			} catch (IOException | ParseException | RenderException ex) {
				usageAndExit("Cannot parse the documentr.json file, message was: " + ex.getMessage());
			}
		} else {
			usageAndExit("Cannot find the documentr.json file.");
		}
	}

}
