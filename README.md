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

**Travis CI**
Initially we configured our build to run on 
[Travis CI](https://travis-ci.org/DeveloperLiberationFront/Spreadsheet-Common-Crawler)

**Jenkins**
Since Travis CI did not meet the project requirements, we created a Jenkins server running on a local vagrant machine.  Here is a screenshot of the web interface:   
![image](https://cloud.githubusercontent.com/assets/6819944/6066907/9461437c-ad3e-11e4-9915-1f8e286b3131.png)

As indicated above, Maven manages our dependencies and the Jenkins server uses Maven to run the build process using. Here is a screenshot of a successful build notification. 

![image](https://cloud.githubusercontent.com/assets/5032534/6063888/789df68c-ad28-11e4-82f3-62788cf0002a.png)

Because we are running this locally, that is, without a public ip address, we could not setup a git hook.  However, we set up a Git poll that simply pings the GitHub repository every 15 minutes (this is adjustable) and makes a build if there are changes

![image](https://cloud.githubusercontent.com/assets/6819944/6066588/2fe9c9d4-ad3c-11e4-9d09-bcf6879ac020.png)


We created a slave vm (also using vagrant) that the master connects to via SSH to setup and execute tasks:  
![image](https://cloud.githubusercontent.com/assets/6819944/6066396/d1dc4d4a-ad3a-11e4-9742-f4be53e9a58a.png)

Here is a screenshot of the log when "slave 3" picked up a build job:
![image](https://cloud.githubusercontent.com/assets/6819944/6066633/8d4c50a6-ad3c-11e4-88dd-c6b029c5cf69.png)

Our Jenkins config file and the config file for our job are found under `jenkins/`.

