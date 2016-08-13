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


Whilst documentation is seen as the thing that developers love to read, but hate 
to write, documentation for any project/module/framework/extension helps:

  - Increase adoption - that's right, if you want people to use your project, documentation makes is _so_ much easier
  - You to understand your code better - if you can explain it in clear English (or whatever your language of preference is), then it is probably well-thought out code. Furthermore, when documenting, you will inevitably come across things that would be good to add to the code-base
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

files. All other information is generated from the `documentr.json` file in 
the root of this project.

# Getting Started

  1. Create a `documentr.json` file (if one doesn't exist - we will create one automatically for you)
  1. Generate the `README` file either through the gradle plugin, or through the command line
  1. ... There is no step 3


## Step 1 - create the `documentr.json` file


This is a simple JSON formatted file:

