frak-server
===========

Regex as a Service.

Requirements
------------

* Java
* Maven

Building
--------

Build [frak](https://github.com/noprompt/frak) 0.1.2 and install it into your local Maven repository. You can use my fork of frak that includes a Maven POM.

    $ git clone https://github.com/breun/frak.git
    $ cd frak
    $ mvn package
    $ mvn install:install-file -Dfile=target/frak-0.1.2.jar -DgroupId=frak -DartifactId=frak -Dversion=0.1.2 -Dpackaging=jar

Instead of installing the jar into your local Maven repository you could also add it to your repository manager if you use one. Frak is not available from Maven Central at this time.

Then build frak-server:

    $ git clone https://github.com/breun/frak-server.git
    $ cd frak-server
    $ mvn package
    
Running
-------

    $ java -jar target/frak-server-1.0-SNAPSHOT-jar-with-dependencies.jar
    FrakServer listening on port 8080, POST some strings!
    
Usage
-----

POST some strings separated by spaces and frak-server will return a regular expression that matches those strings:

    $ curl -X POST -d "test one two" localhost:8080
    (?:t(?:est|wo)|one)
