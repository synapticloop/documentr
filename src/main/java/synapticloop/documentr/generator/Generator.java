package synapticloop.documentr.generator;

/*
 * Copyright (c) 2016 Synapticloop.
 * 
 * All rights reserved.
 * 
 * This code may contain contributions from other parties which, where 
 * applicable, will be listed in the default build file for the project 
 * ~and/or~ in a file named CONTRIBUTORS.txt in the root of the project.
 * 
 * This source code and any derived binaries are covered by the terms and 
 * conditions of the Licence agreement ("the Licence").  You may not use this 
 * source code or any derived binaries except in compliance with the Licence.  
 * A copy of the Licence is available in the file named LICENSE.txt shipped with 
 * this source code or binaries.
 */

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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

import nl.jworks.markdown_to_asciidoc.Converter;
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

	private final File rootDirectory;
	private final String extension;
	private boolean verbose = false;

	private final TemplarContext templarContext = new TemplarContext();
	private List<ConfigurationBean> configurationBeans = new ArrayList<ConfigurationBean>();

	public Generator(Project project, File rootDirectory, String extension, boolean verbose) {
		this.rootDirectory = rootDirectory;
		this.verbose = verbose;
		this.extension = extension;

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

	public Generator(File rootDirectory, String extension, boolean debug) {
		this.extension = extension;
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

					String value = templateObject.getString(VALUE);
					String pathname = documentrJsonFile.getParent() + "/" + value;
					switch(TYPE_LOOKUP.get(type)) {
					case TYPE_FILE:
						stringBuilder.append("{pre\n");
						stringBuilder.append(FileUtils.readFileToString(new File(pathname)));
						stringBuilder.append("\npre}\n");
						break;
					case TYPE_MARKUP:
						stringBuilder.append("\n");
						stringBuilder.append(value.replaceAll("\\{", "\\{\\{").replaceAll("\\n", "\\{\\\\n\\}").replaceAll("\\t", "\\{\\\\t\\}"));
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
						stringBuilder.append(value);
						break;
					case TYPE_INBUILT:
						stringBuilder.append("{import classpath:/");
						stringBuilder.append(getInbuiltTemplateName(value));
						stringBuilder.append("}\n");
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
				String renderered = parser.render(templarContext);
				File outputFile = new File(documentrJsonFile.getParent() + "/README." + extension);
				if("adoc".equals(extension)) {
					FileUtils.writeStringToFile(outputFile, Converter.convertMarkdownToAsciiDoc(renderered));
				} else {
					FileUtils.writeStringToFile(outputFile, renderered);
				}
			} catch (IOException | ParseException | RenderException ex) {
				throw new DocumentrException(String.format("Cannot parse/render the '%s' file, message was: %s", documentrJsonFile, ex.getMessage()), ex);
			}
		} else {
			throw new DocumentrException(String.format("Cannot find the '%s' file.", documentrJsonFile));
		}
	}

	private String getInbuiltTemplateName(String template) {
		InputStream resourceAsStream = null;
		try {
			String lookForTemplate = String.format("%s.%s.templar", template, extension);
			resourceAsStream = Generator.class.getResourceAsStream("/" + lookForTemplate);
			if(null == resourceAsStream) {
				return(String.format("%s.md.templar", template));
			} else {
				return(lookForTemplate);
			}
		} finally {
			if(null != resourceAsStream) {
				try { resourceAsStream.close(); } catch (IOException ex) { /* do nothing */ }
			}
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
