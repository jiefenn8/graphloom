# r2graph-api

A Java prototype implementation of a RDB to Graph mapping API.

[![Build status](https://travis-ci.org/jiefenn8/r2graph-api.svg?branch=master)](https://travis-ci.org/jiefenn8/r2graph-api)

## Description

Part of CONVERT component for web and graph parent side project [ws-projects](https://github.com/jiefenn8/ws-projects).

A relational database to graph mapping API. Mapping relational database data from a provided interface and mapping rules to generate a graph output dataset. 

Overview of r2graph-api process:

![r2graph-api](https://user-images.githubusercontent.com/42923689/53843879-c1fcd880-4008-11e9-8919-0b6b5e620f8c.png)

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See the prerequisties and deployment notes below on how to use the project on a development environment.

### Prerequisites

 * Make sure to have [Java 8 SDK](https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) installed.

### Installation

[Download](https://github.com/jiefenn8/r2graph-api/archive/master.zip) and extract the repository zip to install directory

OR

Clone the repository with cmd (or terminal) with Git installed.
```
cd <install directory of choice>
git clone https://github.com/jiefenn8/r2graph-api.git
```

### Usage example

Supply the Processor with a implementation of InputDatabase interface and a R2RML mapping document with InputStream.
```
//Quick example

InputDatabase inputDb = new YourInputDatabaseImpl();
//Your Code handling InputDatabase impl with database.

InputStream io = new FileInputStream("my_rmrml.ttl");

//Map data
Processor processor = new Processor();
processor.mapToGraph(inputDb, io);

//Get graph output dataset
Model output = processor.getGraph();

//Your code handling output. e.g. To file or graph database
```

## Built With

* [Apache Jena](https://jena.apache.org/ "Apache Jena - Java") 

## License

This project is licensed under the Apache License - see the [LICENSE.md](LICENSE.md) file for details
