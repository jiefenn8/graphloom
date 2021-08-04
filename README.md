# GraphLoom

A Java implementation of an RDB to Semantic Graph mapping library. 

[![CI](https://github.com/jiefenn8/graphloom/workflows/CI/badge.svg)](https://github.com/jiefenn8/graphloom/actions?query=workflow%3ACI)[![codecov](https://codecov.io/gh/jiefenn8/graphloom/branch/master/graph/badge.svg)](https://codecov.io/gh/jiefenn8/graphloom)[![Apache 2.0 License](https://img.shields.io/badge/license-apache2-green.svg) ](https://github.com/jiefenn8/graphloom/blob/master/LICENSE.md)

[![Release](https://img.shields.io/github/v/release/jiefenn8/graphloom)](https://github.com/jiefenn8/graphloom/releases/latest)[![Maven Central](https://img.shields.io/maven-central/v/io.github.jiefenn8.graphloom/graphloom-core?color=blue)](https://search.maven.org/artifact/io.github.jiefenn8.graphloom/graphloom-core)[![javadoc](https://javadoc.io/badge2/io.github.jiefenn8.graphloom/graphloom-core/javadoc.svg)](https://javadoc.io/doc/io.github.jiefenn8.graphloom/graphloom-core) 

```
30/06/2021
Due to the closure of Bintray, our artifact repository has now been migrated to Maven Central. 
This also means that any releases before this statement date will be unavailable to download. 
```

## Description

A relational database to semantic graph mapping library. Mapping relational database data from a provided interface and mapping rules to generate a semantic graph dataset. 

## Getting Started

To get started on using GraphLoom on your project, ensure you have met all prerequisites and follow any instructions below to get the library in your build up and running for development or testing purposes.

### Prerequisites

GraphLoom target compatibility is currently Java 15 (17 for LTS soon). Your Java SDK must have compatibility for Java 15 for this library to work properly. To obtain Java SDK, go to either [OpenJDK](https://openjdk.java.net/) or [OracleJDK](https://www.oracle.com/java/technologies/javase/jdk15-archive-downloads.html) depending on your own project license requirement.
 
### Adding GraphLoom to your build

#### Maven

```
<dependency>
  <groupId>io.github.jiefenn8.graphloom</groupId>
  <artifactId>graphloom-core</artifactId>
  <version>0.6.0</version>
</dependency>
```

#### Gradle

```
dependencies {
  implementation 'io.github.jiefenn8.graphloom:graphloom-core:0.6.0'
}
```

### Usage example

Supply the Processor with an implementation of InputSource interface and the R2RML mapping document.
```
//Quick example

InputSource inputSource = new YourInputSourceImpl();
//Your Code handling InputSource implementation with database.

//Parse the R2RML file
R2RMLParser r2rmlParser = new R2RMLParser();
ConfigMaps r2rmlMap = r2rmlParser.parse("my-r2rml-file.ttl");

//Map data
RDFMapper rdfMapper = new RDFMapper();
Model output = mapper.mapToGraph(inputSource, r2rmlMap);

//Rest of your code handling output. e.g. To file or graph database.
```

## More information

For more information on GraphLoom such as API usage or planned roadmap, visit the [Wiki](https://github.com/jiefenn8/graphloom/wiki).

Collection of other technology related projects can be found in this [repository](https://github.com/jiefenn8/ws-projects).

## Third Party Software

See [notice](./NOTICE.md) and [third party license](./LICENSE-3RD-PARTY.md) for more information on third party software used.

## License

This project is licensed under the terms of [Apache 2.0 License](./LICENSE.md). 
