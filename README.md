[![Build Status](https://travis-ci.org/synapticloop/documentr.svg?branch=master)](https://travis-ci.org/synapticloop/documentr)[![Download](https://api.bintray.com/packages/synapticloop/maven/documentr/images/download.svg) ](https://bintray.com/synapticloop/maven/documentr/_latestVersion)[![GitHub Release](https://img.shields.io/github/release/synapticloop/documentr.svg)](https://github.com/synapticloop/documentr/releases)

# documentr



> documentation (README.md) generator for projects - this utilises the templar templating language



# Usage

This project generates a `README.md` file pulling together information from a variety of sources.  A lot of project documentation is repetitive, hard to generate, contains information that is hard-coded, or is just plain missing.

The only files that are hand-written is this file `pre-usage.md.templar`, and the subsequent one `post-usage.md.templar`, all other information is generated from the `documentr.json` file in the root of this project.


## The `documentr.json` file

This is a simple JSON file as show below:

*in fact - the following file is included from the file system `documentr.json`, so that it is always up-to-date with the correct information...*


```
{
	"context": {
		"dependencyLocation": "bintray"
	},
	"templates": [
		{ "type":"inbuilt", "value":"badge-travis-ci" },
		{ "type":"inbuilt", "value":"badge-bintray" },
		{ "type":"inbuilt", "value":"badge-shield-io-github-release" },

		{ "type":"inbuilt", "value":"project-name" },
		{ "type":"inbuilt", "value":"project-description" },

		{ "type":"template", "value":"pre-usage.md.templar" },

		{ "type":"markup", "value":"\n```\n" },
		{ "type":"file", "value":"documentr.json" },
		{ "type":"markup", "value":"\n```\n" },

		{ "type":"template", "value":"post-usage.md.templar" },

		{ "type":"markup", "value":"#Java command line usage\n\n```\n" },
		{ "type":"file", "value":"src/main/resources/USAGE.txt" },
		{ "type":"markup", "value":"\n```\n" },

		{ "type":"inbuilt", "value":"gradle-build" },

		{ "type":"inbuilt", "value":"logging-slf4j" },

		{ "type":"inbuilt", "value":"dependency-management" },

		{ "type":"inbuilt", "value":"license-mit" },

		{ "type":"inbuilt", "value":"attribution" }
	]
}
```

The above file generated this complete `README.md` file, while only requiring 2 files to be created by hand

Looking at the `documentr.json` file in closer detail, There 

#Java command line usage

```
Generate a README.md file for projects utilising the 'templar' templating 
engine.

Usage:
    java -jar documentr-all.jar <directory>

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
  - file - the file will be included as is with no parsing done on it
  - markup - any valid markdown, with '\n' being replaced with a new line 
        character.  No templar parsing is done on this.
  - inbuilt - one of the in-built templates (see src/main/resources/*.templar
        for a complete list of in-built templates).  Note that with the inbuilt
        templates you do __NOT__ include the '.templar' extension

The list of inbuilt templates:

  - attribution - a nice attribution to synapticloop for generating this 
        README.md file
  - badge-bintray - generation of a bintray download badge with version number
  - badge-shield-io-github-release - generation of a github release version 
        number
  - badge-travis-ci - build status from travis-ci
  - dependency-management - dependency management information with instructions
        for maven and gradle
  - gradle-build - gradle build instructions
  - gradle-test - gradle test instructions
  - test-warn - warning about running tests, which may consume resources, which
        may lead to a cost
  - license-apache-2.0 - the standard Apache 2.0 license
  - license-bsd-2-clause - the BSD 2 Clause license
  - license-bsd-3-clause - the BSD 3 Clause license
  - license-mit - the standard MIT license
  - logging-slf4j - informing users that slf4j is being used within the project 
        and information on how to set up various other loggers to utilise it 
  - project-description - the description of the project
  - project-name - the name of the project as an h1 markdown



```
# Building the Package

## *NIX/Mac OS X

From the root of the project, simply run

`./gradlew build`


## Windows

`./gradlew.bat build`


This will compile and assemble the artefacts into the `build/libs/` directory.

Note that this may also run tests (if applicable see the Testing notes)

# Logging

slf4j is the logging framework used for this project.  In order to use a logging framework with this project, sample configurations are below:

## Log4j

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

# Dependency Management

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

And now for the dependency

```
<dependency>
	<groupId>synapticloop</groupId>
	<artifactId>documentr</artifactId>
	<version>v1.1.0</version>
	<type>jar</type>
</dependency>
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

and then include the dependency:

```
dependencies {
	runtime(group: 'synapticloop', name: 'documentr', version: 'v1.1.0', ext: 'jar')

	compile(group: 'synapticloop', name: 'documentr', version: 'v1.1.0', ext: 'jar')
}
```

or, more simply for versions of gradle greater than 2.4

```
dependencies {
	runtime 'synapticloop:documentr:v1.1.0'

	compile 'synapticloop:documentr:v1.1.0'
}
```


You may either download the files from [https://bintray.com/synapticloop/maven/documentr/](https://bintray.com/synapticloop/maven/documentr/) or from [https://github.com/synapticloop/documentr/releases](https://github.com/synapticloop/documentr/releases)

You will also need the dependencies:

### runtime dependencies

  - synapticloop, simpleusage, v1.0.0: (It may be available on: [bintray](https://bintray.com/synapticloop/maven/simpleusage/v1.0.0/view#files/synapticloop/simpleusage/v1.0.0) [mvn central](http://search.maven.org/#artifactdetails|synapticloop|simpleusage|v1.0.0|jar) [mvn repository](http://mvnrepository.com/artifact/synapticloop/simpleusage/v1.0.0) )
  - synapticloop, simplelogger, v1.0.7: (It may be available on: [bintray](https://bintray.com/synapticloop/maven/simplelogger/v1.0.7/view#files/synapticloop/simplelogger/v1.0.7) [mvn central](http://search.maven.org/#artifactdetails|synapticloop|simplelogger|v1.0.7|jar) [mvn repository](http://mvnrepository.com/artifact/synapticloop/simplelogger/v1.0.7) )
  - synapticloop, templar, v1.1.3: (It may be available on: [bintray](https://bintray.com/synapticloop/maven/templar/v1.1.3/view#files/synapticloop/templar/v1.1.3) [mvn central](http://search.maven.org/#artifactdetails|synapticloop|templar|v1.1.3|jar) [mvn repository](http://mvnrepository.com/artifact/synapticloop/templar/v1.1.3) )
  - commons-io, commons-io, 2.4: (It may be available on: [bintray](https://bintray.com/commons-io/maven/commons-io/2.4/view#files/commons-io/commons-io/2.4) [mvn central](http://search.maven.org/#artifactdetails|commons-io|commons-io|2.4|jar) [mvn repository](http://mvnrepository.com/artifact/commons-io/commons-io/2.4) )
  - org.json, json, 20160212: (It may be available on: [bintray](https://bintray.com/org.json/maven/json/20160212/view#files/org.json/json/20160212) [mvn central](http://search.maven.org/#artifactdetails|org.json|json|20160212|jar) [mvn repository](http://mvnrepository.com/artifact/org.json/json/20160212) )


### compile dependencies

  - synapticloop, simpleusage, v1.0.0: (It may be available on: [bintray](https://bintray.com/synapticloop/maven/simpleusage/v1.0.0/view#files/synapticloop/simpleusage/v1.0.0) [mvn central](http://search.maven.org/#artifactdetails|synapticloop|simpleusage|v1.0.0|jar) [mvn repository](http://mvnrepository.com/artifact/synapticloop/simpleusage/v1.0.0) )
  - synapticloop, simplelogger, v1.0.7: (It may be available on: [bintray](https://bintray.com/synapticloop/maven/simplelogger/v1.0.7/view#files/synapticloop/simplelogger/v1.0.7) [mvn central](http://search.maven.org/#artifactdetails|synapticloop|simplelogger|v1.0.7|jar) [mvn repository](http://mvnrepository.com/artifact/synapticloop/simplelogger/v1.0.7) )
  - synapticloop, templar, v1.1.3: (It may be available on: [bintray](https://bintray.com/synapticloop/maven/templar/v1.1.3/view#files/synapticloop/templar/v1.1.3) [mvn central](http://search.maven.org/#artifactdetails|synapticloop|templar|v1.1.3|jar) [mvn repository](http://mvnrepository.com/artifact/synapticloop/templar/v1.1.3) )
  - commons-io, commons-io, 2.4: (It may be available on: [bintray](https://bintray.com/commons-io/maven/commons-io/2.4/view#files/commons-io/commons-io/2.4) [mvn central](http://search.maven.org/#artifactdetails|commons-io|commons-io|2.4|jar) [mvn repository](http://mvnrepository.com/artifact/commons-io/commons-io/2.4) )
  - org.json, json, 20160212: (It may be available on: [bintray](https://bintray.com/org.json/maven/json/20160212/view#files/org.json/json/20160212) [mvn central](http://search.maven.org/#artifactdetails|org.json|json|20160212|jar) [mvn repository](http://mvnrepository.com/artifact/org.json/json/20160212) )



**NOTE:** You may need to download any dependencies of the above dependencies in turn

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

> `Hand-crafted with care utilising synapticloop` [`templar`](https://github.com/synapticloop/templar/) `->`[`documentr`](https://github.com/synapticloop/documentr/)

--

