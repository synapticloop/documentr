[![Build Status](https://travis-ci.org/synapticloop/documentr.svg?branch=master)](https://travis-ci.org/synapticloop/documentr) [![Download](https://api.bintray.com/packages/synapticloop/maven/documentr/images/download.svg)](https://bintray.com/synapticloop/maven/documentr/_latestVersion) [![GitHub Release](https://img.shields.io/github/release/synapticloop/documentr.svg)](https://github.com/synapticloop/documentr/releases) [![Gradle Plugin Release](https://img.shields.io/badge/gradle%20plugin-1.2.1-blue.svg)](https://plugins.gradle.org/plugin/synapticloop.documentr) 

> **This project requires JVM version of at least 1.7**




# documentr



> documentation (README.md) generator for projects - this utilises the templar templating language



A lot of project documentation is just plain incorrect, mainly due to the fact that it is hard to keep up to date.  Furthermore, there are different levels of documentation required for the novice to the expert.  Making your project as easy as possible to set up and integrate helps everybody.

Making documentation hard to update reduces the likelihood that it will be kept up-to-date at all. Consequently, this project easily generates a `README.md` file pulling together information from a variety of sources (files, templates, in-built resources and in-line mark-up), making it easier to include a raft of information without having to generate multiple files by hand.

For this `README.md` file, the only files that are hand-written are:

  - `src/docs/pre-usage.md.templar`,
  - `src/docs/post-usage.md.templar`

files. All other information is generated from the `documentr.json` file in the root of this project.

# Getting Started

  1. Create a `documentr.json` file
  1. Generate the `README.md` file either through the gradle plugin, or through the command line
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


The above file generated this complete `README.md` file, while only requiring 2 files to be created by hand.


## Step 2 - generate the `README.md` file
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
		classpath "gradle.plugin.synapticloop.documentr:documentr:1.2.1"
	}
}

apply plugin: "synapticloop.documentr"

```
### if you are using gradle >= 2.1 - you may use the shorthand format to apply the plugin

```
plugins {
	id 'synapticloop.documentr' version '1.2.1'
}
```

### Defaults

By default the plugin looks for a `documentr.json` file in the current directory, you may override this by doing the following:

```
documentr {
	directory = '../some/directory/'
}
```


Note that this will generate the `README.md` file in the same directory (i.e. `../some/directory/README.md`)

### Java command line usage

simply run


```
java -jar documentr-1.2.1-all.jar
```

By default this will generate the `README.md` file looking at the current directory for a `documentr.json` file.


Running:

```
java -jar documentr-1.2.1-all.jar --help
```

Will yield the following information:




```
Generate a README.md file for projects utilising the 'templar' templating 
engine.

Usage:
    java -jar documentr-all.jar [ <directory> | --help ]

Where:
    <directory> is an optional argument for which directory to look for the
        documentr.json file and generate the README.md.  This defaults to 
        the current working directory
    --help will print out a longer version of the usage message.

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
		{ "type":"template-type", "value":"template-name" },
		{ "type":"template-type", "value":"template-name" },
		...
	]
}

The "context" keyed object is a JSONObject of key value pairs, and can be valid 
value apart from a JSONArray or another JSONObject (i.e., long, boolean, string, 
int).

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
        README.md file.
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
	runtime(group: 'synapticloop', name: 'documentr', version: '1.2.1', ext: 'jar')

	compile(group: 'synapticloop', name: 'documentr', version: '1.2.1', ext: 'jar')
}
```

or, more simply for versions of gradle greater than 2.1

```
dependencies {
	runtime 'synapticloop:documentr:1.2.1'

	compile 'synapticloop:documentr:1.2.1'
}
```

## Dependencies - Maven

```
<dependency>
	<groupId>synapticloop</groupId>
	<artifactId>documentr</artifactId>
	<version>1.2.1</version>
	<type>jar</type>
</dependency>
```

## Dependencies - Downloads


You will also need to download the following dependencies:



### compile dependencies

  - synapticloop:simpleusage:1.1.1: (It may be available on one of: [bintray](https://bintray.com/synapticloop/maven/simpleusage/1.1.1/view#files/synapticloop/simpleusage/1.1.1) [mvn central](http://search.maven.org/#artifactdetails|synapticloop|simpleusage|1.1.1|jar)
  - synapticloop:simplelogger:1.1.0: (It may be available on one of: [bintray](https://bintray.com/synapticloop/maven/simplelogger/1.1.0/view#files/synapticloop/simplelogger/1.1.0) [mvn central](http://search.maven.org/#artifactdetails|synapticloop|simplelogger|1.1.0|jar)
  - synapticloop:templar:v1.1.3: (It may be available on one of: [bintray](https://bintray.com/synapticloop/maven/templar/v1.1.3/view#files/synapticloop/templar/v1.1.3) [mvn central](http://search.maven.org/#artifactdetails|synapticloop|templar|v1.1.3|jar)
  - commons-io:commons-io:2.4: (It may be available on one of: [bintray](https://bintray.com/commons-io/maven/commons-io/2.4/view#files/commons-io/commons-io/2.4) [mvn central](http://search.maven.org/#artifactdetails|commons-io|commons-io|2.4|jar)
  - org.json:json:20160212: (It may be available on one of: [bintray](https://bintray.com/org.json/maven/json/20160212/view#files/org.json/json/20160212) [mvn central](http://search.maven.org/#artifactdetails|org.json|json|20160212|jar)


### runtime dependencies

  - synapticloop:simpleusage:1.1.1: (It may be available on one of: [bintray](https://bintray.com/synapticloop/maven/simpleusage/1.1.1/view#files/synapticloop/simpleusage/1.1.1) [mvn central](http://search.maven.org/#artifactdetails|synapticloop|simpleusage|1.1.1|jar)
  - synapticloop:simplelogger:1.1.0: (It may be available on one of: [bintray](https://bintray.com/synapticloop/maven/simplelogger/1.1.0/view#files/synapticloop/simplelogger/1.1.0) [mvn central](http://search.maven.org/#artifactdetails|synapticloop|simplelogger|1.1.0|jar)
  - synapticloop:templar:v1.1.3: (It may be available on one of: [bintray](https://bintray.com/synapticloop/maven/templar/v1.1.3/view#files/synapticloop/templar/v1.1.3) [mvn central](http://search.maven.org/#artifactdetails|synapticloop|templar|v1.1.3|jar)
  - commons-io:commons-io:2.4: (It may be available on one of: [bintray](https://bintray.com/commons-io/maven/commons-io/2.4/view#files/commons-io/commons-io/2.4) [mvn central](http://search.maven.org/#artifactdetails|commons-io|commons-io|2.4|jar)
  - org.json:json:20160212: (It may be available on one of: [bintray](https://bintray.com/org.json/maven/json/20160212/view#files/org.json/json/20160212) [mvn central](http://search.maven.org/#artifactdetails|org.json|json|20160212|jar)

**NOTE:** You may need to download any dependencies of the above dependencies in turn
# Artefact Publishing - gradle plugin portal

This project publishes artefacts to [the gradle plugin portal](https://plugins.gradle.org/)

> Note that the latest version can be found [https://plugins.gradle.org/plugin/synapticloop.documentr](https://plugins.gradle.org/plugin/synapticloop.documentr)


# All-In-One

This project's artefact output is an 'all in one' jar which includes all runtime dependencies.

This should appear in the artefact repository along with the compiled code, as a convention, this is usually appended with an `-all` classifier

For example:

```documentr-1.2.1.jar -> documentr-1.2.1-all.jar```


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

