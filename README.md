# GraphLoom

A Java implementation of a RDB to Semantic Graph mapping API. 

[![Build Status](https://travis-ci.org/jiefenn8/graphloom.svg?branch=master)](https://travis-ci.org/jiefenn8/graphloom)[![codecov](https://codecov.io/gh/jiefenn8/graphloom/branch/master/graph/badge.svg)](https://codecov.io/gh/jiefenn8/graphloom)[![Apache 2.0 License](https://img.shields.io/badge/license-apache2-green.svg) ](https://github.com/jiefenn8/graphloom/blob/master/LICENSE.md)

## Description

A relational database to graph mapping API. Mapping relational database data from a provided interface and mapping rules to generate a graph output dataset. 

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See the prerequisties and deployment notes below on how to use the project on a development environment.

### Prerequisites

 * Make sure to have [Java 8 SDK](https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) installed.

### Installation

Get the latest version from [releases](https://github.com/jiefenn8/graphloom/releases)

OR

Clone the repository with cmd (or terminal) with Git installed.
```
cd <install directory of choice>
git clone https://github.com/jiefenn8/graphloom.git
```

### Usage example

Supply the Processor with a implementation of InputDatabase interface and a R2RML mapping document with InputStream.
```
//Quick example

InputSource inputSource = new YourInputSourceImpl();
//Your Code handling InputSource implementation with database.

//Parse the R2RML file
R2RMLParser r2rmlParser = new R2RMLParser();
ConfigMaps r2rmlMap = r2rmlParser.parse("my-rmrl.ttl");

//Map data
RDFMapper rdfMapper = new RDFMapper();
Model output = mapper.mapToGraph(inputSource, r2rmlMap);

//Rest of your code handling output. e.g. To file or graph database.
```

### More information

For more information on GraphLoom such as API usage or planned roadmap, visit the [Wiki](https://github.com/jiefenn8/graphloom/wiki).

Collection of other technology related projects can be found in this [repository](https://github.com/jiefenn8/ws-projects).

## Third Party Software

See [NOTICE](./NOTICE.md) and [THIRD-PARTY-LICENSE](./LICENSE-3RD-PARTY.md) for more information on third party software used.

## License

This project is licensed under the terms of [Apache 2.0 License](./LICENSE.md). 
