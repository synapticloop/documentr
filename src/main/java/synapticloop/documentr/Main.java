package synapticloop.documentr;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import synapticloop.templar.Parser;
import synapticloop.templar.exception.ParseException;
import synapticloop.templar.exception.RenderException;
import synapticloop.templar.utils.TemplarContext;
import synapticloop.util.SimpleUsage;

public class Main {
	private static void usageAndExit(String message) {
		SimpleUsage.usage(message);
		printInbuiltTemplates();
		System.exit(-1);
	}

	private static void printInbuiltTemplates() {
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
					String template = (String)object;
					if(template.startsWith("inbuilt:")) {
						stringBuilder.append("{import classpath:/");
						stringBuilder.append(template.substring("inbuilt:".length()));
						stringBuilder.append(".templar}\n");
					}
				}

				TemplarContext templarContext = new TemplarContext();
				templarContext.add("description", "description");
				templarContext.add("group", "group");
				templarContext.add("project", "project");
				templarContext.add("version", "version");

				Parser parser = new Parser(stringBuilder.toString());
				FileUtils.writeStringToFile(new File(file.getParent() + "/README.md"), parser.render(templarContext));

				System.out.println(stringBuilder.toString());
			} catch (IOException | ParseException | RenderException ex) {
				usageAndExit("Cannot parse the documentr.json file, message was: " + ex.getMessage());
			}
		} else {
			usageAndExit("Cannot find the documentr.json file.");
		}
	}

}
