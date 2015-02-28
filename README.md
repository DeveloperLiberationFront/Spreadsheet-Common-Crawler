#Fuse spreadsheet corpus tools

This repository contains all the software used to extract the Fuse corpus from [CommonCrawl](http://commoncrawl.org/).
It is primarily designed to be run in a Hadoop environment, however, the spreadsheet-analyzer project has been configured to run on any Java 1.7+ environment.

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


####About the projects

- **spreadsheet-crawler** : contains many tasks useful for extracting spreadsheets from CommonCrawl.

- **spreadsheet-analyzer** : A standalone analyzer which uses a slightly modified version of Apache POI (located under custom-poi) to generate summary analysis of a directory of spreadsheets.  After being compiled with all its dependencies, it can be run via `java -cp analyzer-with-dependencies.jar net.barik.spreadsheet.analysis.JSONScanner dir/to/scan/`

- **spreadsheet-anlyzer** : A wrapper for spreadsheet-analyzer that allows it to be run in a Hadoop map-reduce environment.


####Example Configurations for running in AWS EMR

We have found some tweaks to the EMR configurations help the jobs complete faster and we list those here:

**Extracting spreadsheets from Common Crawl**:
 - Hadoop configuration: `-m mapred.map.tasks.speculative.execution=false -m mapred.reduce.tasks=0 -c fs.s3n.ssl.enabled=false -m mapreduce.map.java.opts=-Xmx4096m -m mapreduce.map.memory.mb=4096 -m io.file.buffer.size=65536 -m mapreduce.task.timeout=1200000 -y yarn.scheduler.maximum-allocation-mb=4096 -y yarn.nodemanager.resource.cpu-vcores=1 -y yarn.scheduler.minimum-allocation-mb=4096`  
 - Custom Jar arguments: `net.barik.spreadsheet.WATExtractJob -D export.bucket="out-bucket" -D export.keyprefix=output/dir/ s3n://location/to/wat/path s3n://location/for/logs`


 **Analyzing spreadsheets**: 
 - Hadoop configuration: `-m mapred.map.tasks.speculative.execution=false -m mapred.reduce.tasks=0 -c fs.s3n.ssl.enabled=false -m io.file.buffer.size=65536 -m mapreduce.task.timeout=2400000 -m mapreduce.map.memory.mb=5500 -m mapreduce.map.java.opts=-Xmx5200m -y yarn.scheduler.maximum-allocation-mb=6000 -y yarn.nodemanager.resource.cpu-vcores=1 -y yarn.nodemanager.resource.memory-mb=7000`
 - Custom Jar arguments: `-D import.bucket="myBucket" -D export.bucket="myBucket" -D import.keyprefix=dir/with/spreadsheets/ -D export.keyprefix=analysis/output/ -D corpus.name=Fuse s3n://location/to/list/of/spreadsheets s3n://location/for/logs`
