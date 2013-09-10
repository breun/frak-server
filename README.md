frak-server
===========

POST some (space-separated) strings to receive a regular expression matching those strings, courtesy of the [frak](https://github.com/noprompt/frak) library.

Regex as a Service (RaaS).

Build Requirements
------------------

* [Java JDK](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
* [Maven](http://maven.apache.org/)

Build
--------

    $ mvn package
    
Run
---

Execute the assembled jar:

    $ java -jar target/frak-server-1.0-SNAPSHOT-jar-with-dependencies.jar
    (...)
    INFO: FrakServer listening on port 8080, POST some strings!

Supply a port number as an argument to start the server on an alternative port:

    $ java -jar target/frak-server-1.0-SNAPSHOT-jar-with-dependencies.jar 8888
    (...)
    INFO: FrakServer listening on port 8888, POST some strings!
    
Usage
-----

POST some (space-separated) strings to receive a regular expression matching those strings:

    $ curl -X POST -d "test one two" localhost:8080
    (?:t(?:est|wo)|one)