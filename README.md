###Build

[![Build Status](https://travis-ci.org/DeveloperLiberationFront/Spreadsheet-Common-Crawler.svg?branch=master)](https://travis-ci.org/DeveloperLiberationFront/Spreadsheet-Common-Crawler)

To build:   
1. Install Maven.  
2. Clone repository.  
3. Run `mvn package`, which will download the dependencies  
4. Run `mvn eclipse:eclipse` to build the Eclipse project  
5. Import this project into Eclipse.  

**Travis CI**
Initially we configured our build to run on 
[Travis CI](https://travis-ci.org/DeveloperLiberationFront/Spreadsheet-Common-Crawler)

**Jenkins**
Since Travis CI did not meet the project requirements, we created a Jenkins server running on a local vagrant machine. We used Maven to specify our dependencies and the Jenkins server runs the build using Maven. We have included a screenshot of a successful build notification. 

![image](https://cloud.githubusercontent.com/assets/5032534/6063888/789df68c-ad28-11e4-82f3-62788cf0002a.png)


We created a slave vm (also using vagrant) that the master connects to via SSH to setup and execute tasks:  
![image](https://cloud.githubusercontent.com/assets/6819944/6066396/d1dc4d4a-ad3a-11e4-9742-f4be53e9a58a.png)