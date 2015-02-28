###Build

[![Build Status](https://travis-ci.org/DeveloperLiberationFront/Spreadsheet-Common-Crawler.svg?branch=master)](https://travis-ci.org/DeveloperLiberationFront/Spreadsheet-Common-Crawler)

Dependencies (and the rest of the build process) are managed by Maven.  To build:   
1. Install Maven.  
2. Clone repository.  Navigate a command prompt to the folder.  
3. Run `mvn clean install`, which will build and install all three projects.

Useful Maven Commands:  
- Run `mvn clean install` from parent directory to do a full build.  
- Run `mvn eclipse:eclipse` to build an Eclipse project for one of the modules.  
- Run `mvn eclipse:eclipse -DdownloadSources=true  -DdownloadJavadocs=true` to build an Eclipse project with javadocs and sources linked.  
- Run `mvn clean compile assembly:single` in one of the module folders to build a jar with dependencies included [source](http://stackoverflow.com/a/574650/1447621)
