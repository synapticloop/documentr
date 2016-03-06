[![Build Status](https://travis-ci.org/group/project.svg?branch=master)](https://travis-ci.org/group/project)[![Download](https://api.bintray.com/packages/group/maven/project/images/download.svg) ](https://bintray.com/group/maven/project/_latestVersion)[![GitHub Release](https://img.shields.io/github/release/group/project.svg)](https://github.com/group/project/releases)
# project



> description


#Java command line usage

```
Usage:
    java -jar documentr-all.jar <directory>

This will look for a documentr.json file in the directory, parse it and build
the documentation.
```
# Building the Package

## *NIX/Mac OS X

From the root of the project, simply run

`./gradlew build`


## Windows

`./gradlew.bat build`


This will compile and assemble the artefacts into the `build/libs/` directory.

Note that this may also run tests (if applicable see the Testing notes)

# Running the Tests

`gradle --info test`

Which will also print out the logging

if you do not have gradle installed, try:

`gradlew --info test`

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

> Note that the latest version can be found [https://bintray.com/group/maven/project/view](https://bintray.com/group/maven/project/view)

Include the dependency

## maven

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
      <groupId>group</groupId>
      <artifactId>project</artifactId>
      <version>version</version>
      <type>jar</type>
    </dependency>
```


## gradle

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
	runtime(group: 'group', name: 'project', version: 'version', ext: 'jar')

	compile(group: 'group', name: 'project', version: 'version', ext: 'jar')
}
```

or, more simply for versions of gradle greater than 2.4

```
dependencies {
	runtime 'group:project:version'

	compile 'group:project:version'
}
```


You may either download the files from [https://bintray.com/group/maven/project/](https://bintray.com/group/maven/project/) or from [https://github.com/group/project/releases](https://github.com/group/project/releases)

# License

```
The MIT License (MIT)

Copyright (c) 2015 Synapticloop

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

