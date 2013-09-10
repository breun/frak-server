frak-server
===========

Regex as a Service. Based on the [frak](https://github.com/noprompt/frak) library and the [Simple](http://www.simpleframework.org/) framework.

Requirements
------------

* Java
* Maven

Clone
-----

    $ git clone https://github.com/breun/frak-server.git

Building
--------

    $ cd frak-server
    $ mvn package
    
Running
-------

Run on default port 8080:

    $ java -jar target/frak-server-1.0-SNAPSHOT-jar-with-dependencies.jar
    (...)
    INFO: FrakServer listening on port 8080, POST some strings!

Supply a port number as an argument to start the server on an alternative port:

    $ java -jar target/frak-server-1.0-SNAPSHOT-jar-with-dependencies.jar 8888
    (...)
    INFO: FrakServer listening on port 8888, POST some strings!
    
Usage
-----

POST some strings separated by spaces and frak-server will return a regular expression that matches those strings:

    $ curl -X POST -d "test one two" localhost:8080
    (?:t(?:est|wo)|one)