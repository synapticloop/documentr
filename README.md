[![Build Status](https://travis-ci.org/synapticloop/documentr.svg?branch=master)](https://travis-ci.org/synapticloop/documentr) [![Download](https://api.bintray.com/packages/synapticloop/maven/documentr/images/download.svg)](https://bintray.com/synapticloop/maven/documentr/_latestVersion) [![GitHub Release](https://img.shields.io/github/release/synapticloop/documentr.svg)](https://github.com/synapticloop/documentr/releases) [![Gradle Plugin Release](https://img.shields.io/badge/gradle%20plugin-2.1.1-blue.svg)](https://plugins.gradle.org/plugin/synapticloop.documentr) 

> **This project requires JVM version of at least 1.7**




# documentr



> documentation (README) generator for projects in both markdown and asciidoc - this utilises the templar templating language


# IMPORTANT


Staring at version 2, the `build.gradle` task configuration has changed, from `documentrSetting` to just plain `documentr`

## NEW


```
documentr {
	directory = '../some/directory/'
	verbose = 'false'
	extension = 'md' // this is the default
	// extension = 'adoc' // perhaps you want asciidoc?
	documentrFile = 'documentr.json' // perhaps you want to use a different JSON file?
}
```


## OLD

```
documentrSetting {
	directory = '../some/directory/'
	verbose = 'false'
	extension = 'md' // this is the default
	// extension = 'adoc' // perhaps you want asciidoc?
	documentrFile = 'documentr.json' // perhaps you want to use a different JSON file?
}
```




# Overview


## Why document anything?

Ever duckduckgo, google, bing or yahoo searched for an answer to your question? You are searching the world's largest index of documentation - all of which was written by somebody else (or maybe you)


Whilst documentation is seen as the thing that developers love to read, but hate to write, documentation for any project/module/framework/extension helps:

  - Increase adoption - that's right, if you want people to use your project, documentation makies is _so_ much easier
  - You to understand your code better - if you can explain it in clear english (or whatever your language of preference is), then it is probably well-thought out code. Furthermore, when documenting, you will inevitably come across things that would be good to add to the code-base
  - Give an all-round happy feeling - and we all like this, don't we!


## Do's and Don'ts of documentation

### Do

  - Give a quick example of how to get up and running
  - Provide a cut and paste-able example (including import statements if applicable) - This is what your users will do
  - Provide examples for integration points with other libraries - not everybody knows how to use a technology that you have chosen
  - Keep it up to date - old, out of date documentation is almost as bad as no documentation
  - Make it as easy as possible to get your software up and running as quickly as possible


### Don't

  - Tell people to read the test cases
    - People want to use your software, not understand your how you test your code
    - Yes, your audience is technical - but you are probably mocking so many things that they will have to delve through so many test cases just to find the one that they want - just to get up and running
    - If you are using a BDD framework like JBehave or Cucumber, then your audience will have to go through so many levels of indirection just to attempt to figure out what to do
  - Let your documentation get out of date


> The above Do's and Don'ts were the basis for why `documentr` was created, minimising the hand created stuff and maximising the information


For this `README` file, the only files that are hand-written are:

  - `src/docs/pre-usage.md.templar`,
  - `src/docs/post-usage.md.templar`

files. All other information is generated from the `documentr.json` file in the root of this project.

# Getting Started

  1. Create a `documentr.json` file
  1. Generate the `README` file either through the gradle plugin, or through the command line
  1. ... There is no step 3


## Step 1 - create the `documentr.json` file


This is a simple JSON formatted file:

### The `documentr.json` file

This is a simple JSON file as show below:


```
{
	"context": {
		"pluginId": "synapticloop.documentr"
	},
	"templates": [
		{ "type":"inbuilt", "value":"badge-travis-ci" },
		{ "type":"inbuilt", "value":"badge-bintray" },
		{ "type":"inbuilt", "value":"badge-shield-io-github-release" },
		{ "type":"inbuilt", "value":"badge-shield-io-gradle-plugin" },

		{ "type":"inbuilt", "value":"jvm-compatability" },
		{ "type":"inbuilt", "value":"project-name" },
		{ "type":"inbuilt", "value":"project-description" },

		{ "type":"template", "value":"src/docs/pre-usage.md.templar" },

		{ "type": "markup", "value": "### The `documentr.json` file\n\n" },
		{ "type": "markup", "value": "This is a simple JSON file as show below:\n\n" },
		{ "type": "markup", "value":"\n```\n" },
		{ "type": "file", "value":"documentr.json" },
		{ "type": "markup", "value":"\n```\n" },
		{ "type": "markup", "value":"> *in fact - the above file is included from the file system `documentr.json`, so that it is always up-to-date with the correct information...*\n\n" },

		{ "type":"template", "value":"src/docs/post-usage.md.templar" },

		{ "type":"markup", "value":"\n\n```\n" },
		{ "type":"file", "value":"src/main/resources/USAGE.txt" },
		{ "type":"file", "value":"src/main/resources/HELP.txt" },
		{ "type":"markup", "value":"\n```\n" },

		{ "type":"inbuilt", "value":"gradle-build" },

		{ "type":"inbuilt", "value":"logging-slf4j" },

		{ "type":"inbuilt", "value":"publishing-github" },
		{ "type":"inbuilt", "value":"publishing-bintray" },

		{ "type":"inbuilt", "value":"dependencies" },

		{ "type":"inbuilt", "value":"publishing-gradle-plugin" },

		{ "type":"inbuilt", "value":"publishing-all-in-one-jar" },

		{ "type":"inbuilt", "value":"license-mit" },

		{ "type":"inbuilt", "value":"attribution" }
	]
}
```
> *in fact - the above file is included from the file system `documentr.json`, so that it is always up-to-date with the correct information...*


The above file generated this complete `README` file, while only requiring 2 files to be created by hand.


## Step 2 - generate the `README` file
### Gradle plugin usage


Include the plugin and simply run:

`gradle documentr`

This will also parse the build file and place various objects into the context that are then available to use in the templar templates.

### For all versions of gradle - you may use the following to apply the plugin

```
buildscript {
	repositories {
		maven {
			url "https://plugins.gradle.org/m2/"
		}
	}
	dependencies {
		classpath "gradle.plugin.synapticloop:documentr:2.1.1"
	}
}

apply plugin: "synapticloop.documentr"

```
### if you are using gradle >= 2.1 - you may use the shorthand format to apply the plugin

```
plugins {
	id 'synapticloop.documentr' version '2.1.1'
}
```

### Defaults

By default the plugin looks for a `documentr.json` file in the current directory, you may override this by doing the following:

```
documentr {
	directory = '../some/directory/'
	verbose = 'false'
	extension = 'md' // this is the default
	// extension = 'adoc' // perhaps you want asciidoc?
	documentrFile = 'documentr.json' // perhaps you want to use a different JSON file?
}
```


Note that this will generate the `README` file in the same directory (i.e. `../some/directory/README.md` or `../some/directory/README.adoc`)

The `verbose` setting will output the pre-parsed and rendered templar template to the console

### Java command line usage

simply run


```
java -jar documentr-2.1.1-all.jar
```

By default this will generate the `README` file looking at the current directory for a `documentr.json` file.


> note that you will need to place everything in the `context` object manually when running from the command line


Running:

```
java -jar documentr-2.1.1-all.jar --help
```

Will yield the following information:




```
Generate a README.md file for projects utilising the 'templar' templating 
engine.

Usage:
    java -jar documentr-all.jar [OPTIONS}

All OPTIONS are optional

Where OPTIONS are one of 

 -h, --help              will print out a longer version of the usage message.
                         Note: If you use this parameter all other parameters 
                         are ignored

 ~ OR ~

 -d, --directory <arg>   The directory in which the 'documentr.json' file 
                         resides, default the current working directory
                         (i.e. '.')
 -e, --extension <arg>   The extension for the README file, default '.md'. Only
                         '.md' and '.adoc' are supported by the inbuilt 
                         templates, only other extension __MUST__ not use the 
                         'inbuilt' template type
 -v, --verbose <arg>     Output more verbose information

This will look for a documentr.json file in the directory, parse it, collate 
the associated resources and generate the documentation.

The format of the documentr.json file is as follows:

{
	"context": {
		"key": "value",
		"key2": "value2",
		...
	},
	"templates": [
		{ "type": "template-type", "value": "template-name" },
		{ "type": "template-type", "value": "template-name" },
		...
	]
}

The "context" keyed object is a JSONObject of key value pairs, and can be any 
valid JSON values apart from a JSONArray or another JSONObject (i.e., long, 
boolean, string, int).

The "templates" keyed array is a JSONArray of JSONObjects.  Each of the 
JSONObjects, __MUST__ have a key of "type" and "value".  The "type" can only 
be one of the following:

  - template - this is a 'templar' formatted template that will be used and 
        parsed
  - templar - inline templar format tokens - a useful debugging one is:
        {dumpcontext} - which dumps all available context key/value pairs to
        the output
  - file - the file will be included as is with no parsing done on it
  - markup - any valid markdown, with '\n' being replaced with a new line 
        character.  No templar parsing is done on this.
  - inbuilt - one of the in-built templates (see below for a list of the 
        inbuilt templates).

The list of inbuilt templates:

  - attribution - a nice attribution to synapticloop for generating this 
        README file.
  - badge-bintray - generation of a bintray download badge with version number
  - badge-shield-io-github-release - generation of a github release version 
        number
  - badge-shield-io-gradle-plugin - generation of a gradle plugin version release
        number
  - badge-travis-ci - build status from travis-ci
  - dependencies - Listing out all of the dependencies for the project
  - dumpcontext - for debugging, this will dump the available context items to
        the output
  - gradle-build - gradle build instructions
  - gradle-plugin-usage - print out the default gradle plugin usage instructions
  - gradle-test - gradle test instructions
  - jvm-compatability - Output a JVM compatability notice
  - license-apache-2.0 - the standard Apache 2.0 license
  - license-bsd-2-clause - the BSD 2 Clause license
  - license-bsd-3-clause - the BSD 3 Clause license
  - license-mit - the standard MIT license
  - logging-slf4j - informing users that slf4j is being used within the project 
        and information on how to set up various other loggers to utilise it 
  - project-description - the description of the project
  - project-name - the name of the project as an h1 markdown
  - publishing-all-in-one-jar - where an artefact is generated with all 
        dependencies contained within the jar
  - publishing-bintray - Information about the publishing of artefacts to the
        jcenter bintray repository
  - publishing-github - Information about the publishing of artefacts to the
        github releases page
  - publishing-gradle-plugin - Information about the publishing of artefacts to 
        the gradle plugins repository
  - publishing-jitpack - Information about the publishing of artefacts to the
        jitpack repository
  - publishing-maven - Information about the publishing of artefacts to the
        maven central repository
  - test-warn - warning about running tests, which may consume resources, which
        may lead to a cost


```


# Building the Package

## *NIX/Mac OS X

From the root of the project, simply run

`./gradlew build`


## Windows

`./gradlew.bat build`


This will compile and assemble the artefacts into the `build/libs/` directory.

Note that this may also run tests (if applicable see the Testing notes)

# Logging - slf4j

slf4j is the logging framework used for this project.  In order to set up a logging framework with this project, sample configurations are below:

## Log4j


You will need to include dependencies for this - note that the versions may need to be updated.

### Maven

```
<dependency>
	<groupId>org.apache.logging.log4j</groupId>
	<artifactId>log4j-slf4j-impl</artifactId>
	<version>2.5</version>
	<scope>runtime</scope>
</dependency>

<dependency>
	<groupId>org.apache.logging.log4j</groupId>
	<artifactId>log4j-core</artifactId>
	<version>2.5</version>
	<scope>runtime</scope>
</dependency>

```

### Gradle &lt; 2.1

```
dependencies {
	...
	runtime(group: 'org.apache.logging.log4j', name: 'log4j-slf4j-impl', version: '2.5', ext: 'jar')
	runtime(group: 'org.apache.logging.log4j', name: 'log4j-core', version: '2.5', ext: 'jar')
	...
}
```
### Gradle &gt;= 2.1

```
dependencies {
	...
	runtime 'org.apache.logging.log4j:log4j-slf4j-impl:2.5'
	runtime 'org.apache.logging.log4j:log4j-core:2.5'
	...
}
```


### Setting up the logging:

A sample `log4j2.xml` is below:

```
<Configuration status="WARN">
	<Appenders>
		<Console name="Console" target="SYSTEM_OUT">
			<PatternLayout pattern="%d{HH:mm:ss.SSS} [%t] %-5level %logger{36} - %msg%n"/>
		</Console>
	</Appenders>
	<Loggers>
		<Root level="trace">
			<AppenderRef ref="Console"/>
		</Root>
	</Loggers>
</Configuration>
```

# Artefact Publishing - Github

This project publishes artefacts to [GitHib](https://github.com/)

> Note that the latest version can be found [https://github.com/synapticloop/documentr/releases](https://github.com/synapticloop/documentr/releases)

As such, this is not a repository, but a location to download files from.

# Artefact Publishing - Bintray

This project publishes artefacts to [bintray](https://bintray.com/)

> Note that the latest version can be found [https://bintray.com/synapticloop/maven/documentr/view](https://bintray.com/synapticloop/maven/documentr/view)

## maven setup

this comes from the jcenter bintray, to set up your repository:

```
<?xml version="1.0" encoding="UTF-8" ?>
<settings xsi:schemaLocation='http://maven.apache.org/SETTINGS/1.0.0 http://maven.apache.org/xsd/settings-1.0.0.xsd' xmlns='http://maven.apache.org/SETTINGS/1.0.0' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'>
  <profiles>
    <profile>
      <repositories>
        <repository>
          <snapshots>
            <enabled>false</enabled>
          </snapshots>
          <id>central</id>
          <name>bintray</name>
          <url>http://jcenter.bintray.com</url>
        </repository>
      </repositories>
      <pluginRepositories>
        <pluginRepository>
          <snapshots>
            <enabled>false</enabled>
          </snapshots>
          <id>central</id>
          <name>bintray-plugins</name>
          <url>http://jcenter.bintray.com</url>
        </pluginRepository>
      </pluginRepositories>
      <id>bintray</id>
    </profile>
  </profiles>
  <activeProfiles>
    <activeProfile>bintray</activeProfile>
  </activeProfiles>
</settings>
```

## gradle setup

Repository

```
repositories {
	maven {
		url  "http://jcenter.bintray.com" 
	}
}
```

or just

```
repositories {
	jcenter()
}
```

## Dependencies - Gradle

```
dependencies {
	runtime(group: 'synapticloop', name: 'documentr', version: '2.1.1', ext: 'jar')

	compile(group: 'synapticloop', name: 'documentr', version: '2.1.1', ext: 'jar')
}
```

or, more simply for versions of gradle greater than 2.1

```
dependencies {
	runtime 'synapticloop:documentr:2.1.1'

	compile 'synapticloop:documentr:2.1.1'
}
```

## Dependencies - Maven

```
<dependency>
	<groupId>synapticloop</groupId>
	<artifactId>documentr</artifactId>
	<version>2.1.1</version>
	<type>jar</type>
</dependency>
```

## Dependencies - Downloads


You will also need to download the following dependencies:



### compile dependencies

  - synapticloop:simpleusage:1.1.2: (It may be available on one of: [bintray](https://bintray.com/synapticloop/maven/simpleusage/1.1.2/view#files/synapticloop/simpleusage/1.1.2) [mvn central](http://search.maven.org/#artifactdetails|synapticloop|simpleusage|1.1.2|jar))
  - synapticloop:simplelogger:1.1.0: (It may be available on one of: [bintray](https://bintray.com/synapticloop/maven/simplelogger/1.1.0/view#files/synapticloop/simplelogger/1.1.0) [mvn central](http://search.maven.org/#artifactdetails|synapticloop|simplelogger|1.1.0|jar))
  - synapticloop:templar:1.2.1: (It may be available on one of: [bintray](https://bintray.com/synapticloop/maven/templar/1.2.1/view#files/synapticloop/templar/1.2.1) [mvn central](http://search.maven.org/#artifactdetails|synapticloop|templar|1.2.1|jar))
  - commons-io:commons-io:2.4: (It may be available on one of: [bintray](https://bintray.com/commons-io/maven/commons-io/2.4/view#files/commons-io/commons-io/2.4) [mvn central](http://search.maven.org/#artifactdetails|commons-io|commons-io|2.4|jar))
  - org.json:json:20160212: (It may be available on one of: [bintray](https://bintray.com/org.json/maven/json/20160212/view#files/org.json/json/20160212) [mvn central](http://search.maven.org/#artifactdetails|org.json|json|20160212|jar))
  - commons-cli:commons-cli:1.3.1: (It may be available on one of: [bintray](https://bintray.com/commons-cli/maven/commons-cli/1.3.1/view#files/commons-cli/commons-cli/1.3.1) [mvn central](http://search.maven.org/#artifactdetails|commons-cli|commons-cli|1.3.1|jar))
  - nl.jworks.markdown_to_asciidoc:markdown_to_asciidoc:1.0: (It may be available on one of: [bintray](https://bintray.com/nl.jworks.markdown_to_asciidoc/maven/markdown_to_asciidoc/1.0/view#files/nl.jworks.markdown_to_asciidoc/markdown_to_asciidoc/1.0) [mvn central](http://search.maven.org/#artifactdetails|nl.jworks.markdown_to_asciidoc|markdown_to_asciidoc|1.0|jar))


### runtime dependencies

  - synapticloop:simpleusage:1.1.2: (It may be available on one of: [bintray](https://bintray.com/synapticloop/maven/simpleusage/1.1.2/view#files/synapticloop/simpleusage/1.1.2) [mvn central](http://search.maven.org/#artifactdetails|synapticloop|simpleusage|1.1.2|jar))
  - synapticloop:simplelogger:1.1.0: (It may be available on one of: [bintray](https://bintray.com/synapticloop/maven/simplelogger/1.1.0/view#files/synapticloop/simplelogger/1.1.0) [mvn central](http://search.maven.org/#artifactdetails|synapticloop|simplelogger|1.1.0|jar))
  - synapticloop:templar:1.2.1: (It may be available on one of: [bintray](https://bintray.com/synapticloop/maven/templar/1.2.1/view#files/synapticloop/templar/1.2.1) [mvn central](http://search.maven.org/#artifactdetails|synapticloop|templar|1.2.1|jar))
  - commons-io:commons-io:2.4: (It may be available on one of: [bintray](https://bintray.com/commons-io/maven/commons-io/2.4/view#files/commons-io/commons-io/2.4) [mvn central](http://search.maven.org/#artifactdetails|commons-io|commons-io|2.4|jar))
  - org.json:json:20160212: (It may be available on one of: [bintray](https://bintray.com/org.json/maven/json/20160212/view#files/org.json/json/20160212) [mvn central](http://search.maven.org/#artifactdetails|org.json|json|20160212|jar))
  - commons-cli:commons-cli:1.3.1: (It may be available on one of: [bintray](https://bintray.com/commons-cli/maven/commons-cli/1.3.1/view#files/commons-cli/commons-cli/1.3.1) [mvn central](http://search.maven.org/#artifactdetails|commons-cli|commons-cli|1.3.1|jar))
  - nl.jworks.markdown_to_asciidoc:markdown_to_asciidoc:1.0: (It may be available on one of: [bintray](https://bintray.com/nl.jworks.markdown_to_asciidoc/maven/markdown_to_asciidoc/1.0/view#files/nl.jworks.markdown_to_asciidoc/markdown_to_asciidoc/1.0) [mvn central](http://search.maven.org/#artifactdetails|nl.jworks.markdown_to_asciidoc|markdown_to_asciidoc|1.0|jar))

**NOTE:** You may need to download any dependencies of the above dependencies in turn (i.e. the transitive dependencies)
# Artefact Publishing - gradle plugin portal

This project publishes artefacts to [the gradle plugin portal](https://plugins.gradle.org/)

> Note that the latest version can be found [https://plugins.gradle.org/plugin/synapticloop.documentr](https://plugins.gradle.org/plugin/synapticloop.documentr)


# All-In-One

This project's artefact output is an 'all in one' jar which includes all runtime dependencies.

This should appear in the artefact repository along with the compiled code, as a convention, this is usually appended with an `-all` classifier

For example:

`documentr-2.1.1.jar -> documentr-2.1.1-all.jar`




# License

```
The MIT License (MIT)

Copyright (c) 2016 synapticloop

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```


--

> `This README.md file was hand-crafted with care utilising synapticloop`[`templar`](https://github.com/synapticloop/templar/)`->`[`documentr`](https://github.com/synapticloop/documentr/)

--

