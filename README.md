MISSING variable `project` in the context

MISSING variable `group` in the context

# FATAL: `badge-travis-ci.templar`

Missing variables, the template cannot be rendered

You __MUST__ include the above listed variables in the `context` section of the `documentr.json` file:

```
"context": {
	"key": "value"
}
```

If the project has a `gradle.build` or `pom.xml` then they will be attempted to be parsed and certain variables will be automatically bound in the context.

--

MISSING variable `project` in the context

MISSING variable `group` in the context

# FATAL: `badge-bintray.templar`

Missing variables, the template cannot be rendered

You __MUST__ include the above listed variables in the `context` section of the `documentr.json` file:

```
"context": {
	"key": "value"
}
```

If the project has a `gradle.build` or `pom.xml` then they will be attempted to be parsed and certain variables will be automatically bound in the context.

--

MISSING variable `project` in the context

MISSING variable `group` in the context

# FATAL: `badge-shield-io-github-release.templar`

Missing variables, the template cannot be rendered

You __MUST__ include the above listed variables in the `context` section of the `documentr.json` file:

```
"context": {
	"key": "value"
}
```

If the project has a `gradle.build` or `pom.xml` then they will be attempted to be parsed and certain variables will be automatically bound in the context.

--

MISSING variable `project` in the context

# FATAL: `project-name.templar`

Missing variables, the template cannot be rendered

You __MUST__ include the above listed variables in the `context` section of the `documentr.json` file:

```
"context": {
	"key": "value"
}
```

If the project has a `gradle.build` or `pom.xml` then they will be attempted to be parsed and certain variables will be automatically bound in the context.

--



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

MISSING variable `project` in the context

MISSING variable `group` in the context

MISSING variable `version` in the context

# FATAL: `dependency-management.templar`

Missing variables, the template cannot be rendered

You __MUST__ include the above listed variables in the `context` section of the `documentr.json` file:

```
"context": {
	"key": "value"
}
```

If the project has a `gradle.build` or `pom.xml` then they will be attempted to be parsed and certain variables will be automatically bound in the context.

--

# License

```
The MIT License (MIT)

Copyright (c) 

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

> `Hand-crafted with care utilising synapticloop` [`templar`](https://github.com/synapticloop/templar/) `->` [`documentr`](https://github.com/synapticloop/documentr/)

--

