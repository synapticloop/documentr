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
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.ConfigurationContainer;
import org.gradle.api.artifacts.DependencySet;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.pegdown.PegDownProcessor;
import org.pegdown.ast.HeaderNode;
import org.pegdown.ast.Node;
import org.pegdown.ast.RootNode;

import nl.jworks.markdown_to_asciidoc.Converter;
import synapticloop.documentr.bean.ConfigurationBean;
import synapticloop.documentr.bean.StartEndBean;
import synapticloop.documentr.exception.DocumentrException;
import synapticloop.documentr.plugin.DocumentrPluginExtension;
import synapticloop.templar.Parser;
import synapticloop.templar.exception.ParseException;
import synapticloop.templar.exception.RenderException;
import synapticloop.templar.utils.TemplarContext;

public class Generator {
	private static final String KEY_VALUE = "value";
	private static final String KEY_TYPE = "type";

	private static final int TYPE_FILE = 1;
	private static final int TYPE_INBUILT = 0;
	private static final int TYPE_TEMPLAR = 2;
	private static final int TYPE_TEMPLATE = 3;
	private static final int TYPE_MARKUP = 4;
	private static final int TYPE_MARKDOWN = 5;
	private static final int TYPE_TOC = 6;
	private static final int TYPE_TOCBACKTOTOP = 7;
	private static final int TYPE_TOCLINKS= 8;

	private static final String CONTEXT_CONFIGURATION_BEANS = "configurationBeans";
	private static final String CONTEXT_CONFIGURATIONS = "configurations";

	private static final Map<StartEndBean, Integer> HEADER_LOOKUP = new LinkedHashMap<StartEndBean, Integer>();
	private static final Map<Integer, String> SPACING_LOOKUP = new HashMap<Integer, String>();
	private static final Map<String, Integer> TYPE_LOOKUP = new HashMap<String, Integer>();
	static {
		TYPE_LOOKUP.put("inbuilt", TYPE_INBUILT);
		TYPE_LOOKUP.put("file", TYPE_FILE);
		TYPE_LOOKUP.put("templar", TYPE_TEMPLAR);
		TYPE_LOOKUP.put("template", TYPE_TEMPLATE);
		TYPE_LOOKUP.put("markup", TYPE_MARKUP);
		TYPE_LOOKUP.put("markdown", TYPE_MARKDOWN);
		TYPE_LOOKUP.put("toc", TYPE_TOC);
		TYPE_LOOKUP.put("toclinks", TYPE_TOCLINKS);
		TYPE_LOOKUP.put("tocbacktotop", TYPE_TOCBACKTOTOP);

		SPACING_LOOKUP.put(1, " - ");
		SPACING_LOOKUP.put(2, "   - ");
		SPACING_LOOKUP.put(3, "     - ");
		SPACING_LOOKUP.put(4, "       - ");
		SPACING_LOOKUP.put(5, "         - ");
		SPACING_LOOKUP.put(6, "           - ");
	}

	private String documentrFile;
	private final File rootDirectory;
	private final String fileExtension;
	private boolean verbose = false;

	// table of content variables
	private int tocLevel = 6;
	private boolean hasToc = false;
	private boolean hasTocBackToTop = false;
	private String tocBackToTop = " <sup><sup>[top](#)</sup></sup>";
	private boolean hasTocLinks = false;

	private final TemplarContext templarContext = new TemplarContext();
	private List<ConfigurationBean> configurationBeans = new ArrayList<ConfigurationBean>();

	public Generator(Project project, DocumentrPluginExtension extension) {
		this.documentrFile = extension.getDocumentrFile();
		this.rootDirectory = new File(extension.getDirectory());
		this.verbose = extension.getVerbose();
		this.fileExtension = extension.getExtension();

		// now go through and initialise the templar context
		ConfigurationContainer configurations = project.getConfigurations();
		SortedSet<String> configurationNames = configurations.getNames();
		for (String configurationName : configurationNames) {
			ConfigurationBean configurationBean = new ConfigurationBean(configurationName);
			Configuration configuration = configurations.getByName(configurationName);
			DependencySet dependencySet = configuration.getDependencies();
			configurationBean.addDependencies(dependencySet);
			configurationBeans.add(configurationBean);
		}

		templarContext.add(CONTEXT_CONFIGURATIONS, project.getConfigurations());
		templarContext.add(CONTEXT_CONFIGURATION_BEANS, configurationBeans);

		Iterator<String> iterator = project.getProperties().keySet().iterator();
		while (iterator.hasNext()) {
			String key = (String) iterator.next();
			templarContext.add(key, project.getProperties().get(key));
		}

		File rootDirectory = new File(extension.getDirectory());
		File documentrJsonFile = new File(rootDirectory.getAbsolutePath() + "/" + DocumentrPluginExtension.FILE_NAME_DOCUMENTR_JSON);

		// The first thing we are going to do is to see whether there is a documentr.json file
		if(!documentrJsonFile.exists() && DocumentrPluginExtension.FILE_NAME_DOCUMENTR_JSON.equals(documentrFile)) {
			// and the file is missing, we are going to create it
			InputStream resourceAsStream = Generator.class.getResourceAsStream(DocumentrPluginExtension.FILE_NAME_DOCUMENTR_DEFAULT_JSON);
			if(null != resourceAsStream) {
				try {
					FileUtils.write(documentrJsonFile, IOUtils.toString(resourceAsStream));
					project.getLogger().info(String.format("We couldn't find a the default '%s' file, so we created it for you", DocumentrPluginExtension.FILE_NAME_DOCUMENTR_JSON));
				} catch (IOException ex) {
					// ignore this - couldn't create it
					project.getLogger().error(String.format("Could not create the default documentr.json file, message was '%s'", ex.getMessage()), ex);
				}
			}
		}
	}

	public Generator(File rootDirectory, String extension, boolean debug) {
		this.fileExtension = extension;
		this.rootDirectory = rootDirectory;
		this.verbose = debug;
	}

	/**
	 * Generate the documentation.  If we are using the default file location, 
	 * and it doesn't exist, then we will create the file on the fly
	 * 
	 * @throws DocumentrException if there was an error in generating the 
	 *     documentation
	 */
	public void generate() throws DocumentrException {
		//at this point we have a directory - make sure we can find a documentr.json file 
		File documentrJsonFile = new File(rootDirectory.getAbsolutePath() + "/" + documentrFile);

		if(documentrJsonFile.exists() && documentrJsonFile.canRead()) {
			try {
				StringBuilder stringBuilder = new StringBuilder();

				JSONObject jsonObject = new JSONObject(FileUtils.readFileToString(documentrJsonFile));
				JSONArray jsonArray = jsonObject.getJSONArray("templates");

				for (Object object : jsonArray) {
					JSONObject templateObject = (JSONObject)object;

					String type = templateObject.getString(KEY_TYPE);
					if(!TYPE_LOOKUP.containsKey(type)) {
						throw new DocumentrException(String.format("Unknown type of '%s'", type));
					}

					String value = templateObject.optString(KEY_VALUE, "");
					String pathname = documentrJsonFile.getParent() + "/" + value;
					switch(TYPE_LOOKUP.get(type)) {
					case TYPE_FILE:
						stringBuilder.append("{pre\n");
						stringBuilder.append(FileUtils.readFileToString(new File(pathname)));
						stringBuilder.append("\npre}\n");
						break;
					case TYPE_MARKUP:
					case TYPE_MARKDOWN:
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
					case TYPE_TOC:
						hasToc = true;
						stringBuilder.append("\n\n§§TABLE_OF_CONTENTS§§\n\n");
						try {
							tocLevel = Integer.parseInt(value);
							if(tocLevel > 6) {
								tocLevel = 6;
							}

							if(tocLevel <= 0) {
								tocLevel = 1;
							}
						} catch(NumberFormatException ex) {
							// ignore - will stay at 6
						}
						break;
					case TYPE_TOCBACKTOTOP:
						hasTocBackToTop = true;
						if(!value.equalsIgnoreCase("")) {
							tocBackToTop = value;
						}
						break;
					case TYPE_TOCLINKS:
						if(value.equalsIgnoreCase("true")) {
							hasTocLinks = true;
						}
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
				String rendered = parser.render(templarContext);
				File outputFile = new File(documentrJsonFile.getParent() + "/README." + fileExtension);


				if(hasToc) {
					rendered = renderTableOfContents(rendered);
				}

				if("adoc".equals(fileExtension)) {
					FileUtils.writeStringToFile(outputFile, Converter.convertMarkdownToAsciiDoc(rendered));
				} else {
					FileUtils.writeStringToFile(outputFile, rendered);
				}
			} catch (IOException | ParseException | RenderException ex) {
				throw new DocumentrException(String.format("Cannot parse/render the '%s' file, message was: %s", documentrJsonFile, ex.getMessage()), ex);
			}
		} else {
			throw new DocumentrException(String.format("Cannot find the '%s' file.", documentrJsonFile));
		}
	}

	private String renderTableOfContents(String rendered) {
		int numHeader = 0;
		StringBuilder headerStringBuilder = new StringBuilder();
		// go through and parse the markdown, replace the table of contents
		PegDownProcessor pegDownProcessor = new PegDownProcessor();

		String markdownToHtml = pegDownProcessor.markdownToHtml(rendered);

		numHeader = 0;
		Document document = Jsoup.parse(markdownToHtml);
		Elements headings = document.select("h1, h2, h3, h4, h5, h6");
		for (Element heading : headings) {
			int valueOf = Integer.parseInt(heading.nodeName().substring(1));
			if(valueOf <= tocLevel) {
				if(hasTocLinks) {
					headerStringBuilder.append(SPACING_LOOKUP.get(valueOf) + "[" + heading.text() + "](#heading_" + numHeader + ")\n");
				} else {
					headerStringBuilder.append(SPACING_LOOKUP.get(valueOf) + heading.text() + "\n");
				}
			}
			numHeader++;
		}

		headerStringBuilder.append("\n\n");

		rendered = rendered.replace("§§TABLE_OF_CONTENTS§§", headerStringBuilder.toString());

		numHeader = 0;
		// now we need to go through and generate the links to the headers 
		char[] charArray = rendered.toCharArray();
		RootNode rootNode = pegDownProcessor.parseMarkdown(charArray);
		List<Node> children = rootNode.getChildren();

		for (Node node : children) {
			if(node instanceof HeaderNode) {
				HeaderNode headerNode = (HeaderNode)node;
				int level = headerNode.getLevel();
				if(level <= tocLevel) {
					HEADER_LOOKUP.put(new StartEndBean(headerNode.getStartIndex(), headerNode.getEndIndex()), numHeader);
				}
				numHeader++;
			}
		}

		if(hasTocLinks) {
			Iterator<StartEndBean> iterator = HEADER_LOOKUP.keySet().iterator();
			int start = 0;
			StringBuilder renderedStringBuilder = new StringBuilder();

			while (iterator.hasNext()) {
				StartEndBean startEndBean = (StartEndBean) iterator.next();
				int headerStart = startEndBean.getStart();
				int headerEnd = startEndBean.getEnd();
				Integer headerNum = HEADER_LOOKUP.get(startEndBean);
				renderedStringBuilder.append(Arrays.copyOfRange(charArray, start, headerStart));
				renderedStringBuilder.append("\n\n<a name=\"heading_" + headerNum + "\"></a>\n\n");

				if(hasTocBackToTop) {
					renderedStringBuilder.append(Arrays.copyOfRange(charArray, headerStart, headerEnd -1));
					renderedStringBuilder.append(tocBackToTop);
					start = headerEnd -1;
				} else {
					start = headerStart;
				}
			}

			renderedStringBuilder.append(Arrays.copyOfRange(charArray, start, charArray.length));
			rendered = renderedStringBuilder.toString();
		}

		return rendered;
	}

	private String getInbuiltTemplateName(String template) {
		InputStream resourceAsStream = null;
		try {
			String lookForTemplate = String.format("%s.%s.templar", template, fileExtension);
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
		if(null != contextObject) {
			Iterator<String> keys = contextObject.keys();
			while (keys.hasNext()) {
				String key = (String) keys.next();
				templarContext.add(key, contextObject.get(key));
			}
		}
	}
}
