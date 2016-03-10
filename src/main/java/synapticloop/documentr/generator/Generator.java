package synapticloop.documentr.generator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

import org.apache.commons.io.FileUtils;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.ConfigurationContainer;
import org.gradle.api.artifacts.DependencySet;
import org.json.JSONArray;
import org.json.JSONObject;

import synapticloop.documentr.bean.ConfigurationBean;
import synapticloop.documentr.exception.DocumentrException;
import synapticloop.templar.Parser;
import synapticloop.templar.exception.ParseException;
import synapticloop.templar.exception.RenderException;
import synapticloop.templar.utils.TemplarContext;

public class Generator {
	private static final String VALUE = "value";
	private static final String TYPE = "type";

	private static final int TYPE_FILE = 1;
	private static final int TYPE_INBUILT = 0;
	private static final int TYPE_TEMPLAR = 2;
	private static final int TYPE_TEMPLATE = 3;
	private static final int TYPE_MARKUP = 4;

	private static final Map<String, Integer> TYPE_LOOKUP = new HashMap<String, Integer>();
	static {
		TYPE_LOOKUP.put("inbuilt", TYPE_INBUILT);
		TYPE_LOOKUP.put("file", TYPE_FILE);
		TYPE_LOOKUP.put("templar", TYPE_TEMPLAR);
		TYPE_LOOKUP.put("template", TYPE_TEMPLATE);
		TYPE_LOOKUP.put("markup", TYPE_MARKUP);
	}

	private Project project;
	private final File rootDirectory;
	private final TemplarContext templarContext = new TemplarContext();
	private List<ConfigurationBean> configurationBeans = new ArrayList<ConfigurationBean>();
	private boolean verbose = false;

	public Generator(Project project, File rootDirectory, boolean verbose) {
		this.project = project;
		this.rootDirectory = rootDirectory;
		this.verbose = verbose;

		// now go through and initialise the templarcontext
		ConfigurationContainer configurations = project.getConfigurations();
		SortedSet<String> configurationNames = configurations.getNames();
		for (String configurationName : configurationNames) {
			ConfigurationBean configurationBean = new ConfigurationBean(configurationName);
			Configuration configuration = configurations.getByName(configurationName);
			DependencySet dependencySet = configuration.getDependencies();
			configurationBean.addDependency(dependencySet);
			configurationBeans.add(configurationBean);
		}

		templarContext.add("configurations", project.getConfigurations());
		templarContext.add("configurationBeans", configurationBeans);

		Iterator<String> iterator = project.getProperties().keySet().iterator();
		while (iterator.hasNext()) {
			String key = (String) iterator.next();
			templarContext.add(key, project.getProperties().get(key));
		}
	}

	public Generator(File rootDirectory, boolean debug) {
		this.rootDirectory = rootDirectory;
		this.verbose = debug;
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

					String type = templateObject.getString(TYPE);
					if(!TYPE_LOOKUP.containsKey(type)) {
						throw new DocumentrException(String.format("Unknown type of '%s'", type));
					}

					String pathname = documentrJsonFile.getParent() + "/" + templateObject.getString(VALUE);
					switch(TYPE_LOOKUP.get(type)) {
					case TYPE_FILE:
						stringBuilder.append("{pre\n");
						stringBuilder.append(FileUtils.readFileToString(new File(pathname)));
						stringBuilder.append("\npre}\n");
						break;
					case TYPE_MARKUP:
						stringBuilder.append("\n");
						stringBuilder.append(templateObject.getString(VALUE).replaceAll("\\{", "\\{\\{").replaceAll("\\n", "\\{\\\\n\\}").replaceAll("\\t", "\\{\\\\t\\}"));
						stringBuilder.append("\n");
						break;
					case TYPE_TEMPLATE:
						stringBuilder.append("\n");
						stringBuilder.append("{import ");
						stringBuilder.append(pathname);
						stringBuilder.append("}\n");
						stringBuilder.append("\n");
						break;
					case TYPE_TEMPLAR:
						stringBuilder.append(templateObject.getString(VALUE));
						break;
					case TYPE_INBUILT:
						stringBuilder.append("{import classpath:/");
						stringBuilder.append(templateObject.getString(VALUE));
						stringBuilder.append(".templar}\n");
						break;
					default:
						throw new DocumentrException(String.format("Could not determine type %s", type));
					}
				}

				templarContext.add("yearTo", Calendar.getInstance().get(Calendar.YEAR));

				// now override
				overrideContext(templarContext, jsonObject);

				if(verbose) {
					System.out.println(stringBuilder.toString());
				}

				Parser parser = new Parser(stringBuilder.toString());
				FileUtils.writeStringToFile(new File(documentrJsonFile.getParent() + "/README.md"), parser.render(templarContext));
			} catch (IOException | ParseException | RenderException ex) {
				throw new DocumentrException(String.format("Cannot parse/render the '%' file, message was: %s", documentrJsonFile, ex.getMessage()), ex);
			}
		} else {
			throw new DocumentrException(String.format("Cannot find the '%s' file.", documentrJsonFile));
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
