# live demo

- http://doodle.bochenek.ch/
- http://doodle.bochenek.ch/json/polls?email=mh%2Bsample@doodle.com&keyword=badminton&preset=this_year

# tech stack

- Java 8 (i.e. 1.8.0_31,)
- [maven](http://maven.apache.org/install.html) (i.e. 3.3.3) - see pom.xml for java libs used
- MySQL (i.e. 5.5.54)
- [jquery 1.12.4](http://jquery.com/)
- [bootstrap 3.3.7](http://getbootstrap.com/components/)
- Angular JS 1.0.4

# get it running

- manually (carefully!) execute src/main/resources/config/schema_init.sql 
- compile and execute using one of the following commands 

```
mvn clean compile assembly:single
mvn clean test compile
java -jar target/spark-doodle-1.0-SNAPSHOT-jar-with-dependencies.jar 
java -jar target/spark-doodle-1.0-SNAPSHOT-jar-with-dependencies.jar /home/mike/Dropbox/doodle/polls
mvn clean compile assembly:single && java -jar target/spark-doodle-1.0-SNAPSHOT-jar-with-dependencies.jar /home/mike/Dropbox/doodle/polls
```
(there is only one executable, which can optionally load the JSON polls file)

# example API calls

- http://localhost:9989/json/polls?email=mh%2Bsample@doodle.com
- http://localhost:9989/json/polls?email=mh%2Bsample@doodle.com&keyword=badminton
- http://localhost:9989/json/polls?email=mh%2Bsample@doodle.com&keyword=badminton&preset=this_year

# TODOs

- I assumed that initiator email is a required search field, so API returns nothing if email is not found
- UTF-8 encoding on html
- grep TODO and FIXME comments
- pagination would have been cool

# self-criticism

Or things I wish I had done differently

- Sql2o vs. hibernate: Somehow I assumed it could be done with one table and I wanted to use a super-light persistence framework, and this forces me to manage the relatonships and schema myself.
- I wish I had left more time for testing and code cleanup
- Packaging with assembly:single is kinda weird because it skips files with same path (when they have duplicate paths).  This is indeterministic

# Based on: spark-intercooler

This repo is based on the source code for the tutorial found at https://sparktutorials.github.io/2016/06/26/ajax-without-writing-javascript.html  
The spark-intercooler codebase demostrates how to create a single page AJAX application that can create new todos, edit existing todos, filter todos on status, and more, all on the server side without writing any JavaScript.
