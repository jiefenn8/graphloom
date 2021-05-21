# GraphLoom

A Java implementation of a RDB to Semantic Graph mapping library. 

[![Build Status](https://travis-ci.org/jiefenn8/graphloom.svg?branch=master)](https://travis-ci.org/jiefenn8/graphloom)[![codecov](https://codecov.io/gh/jiefenn8/graphloom/branch/master/graph/badge.svg)](https://codecov.io/gh/jiefenn8/graphloom)[![Apache 2.0 License](https://img.shields.io/badge/license-apache2-green.svg) ](https://github.com/jiefenn8/graphloom/blob/master/LICENSE.md)

```
21/05/2021
Download is currently unavailable as we migrate our artifact repository home from Bintray to JFrog Platform. 
Repository build information below are no longer valid and will be updated once we finish our migration.
```

## Description

A relational database to semantic graph mapping library. Mapping relational database data from a provided interface and mapping rules to generate a semantic graph dataset. 

## Getting Started

To get started on using GraphLoom on your project, ensure you have met all prerequisites and follow any instructions below to get the library in your build up and running for development or testing purposes.

### Prerequisites

GraphLoom target compatibility is Java 8. Your Java SDK must have compatibility for Java 8 for this library to work properly. To obtain Java SDK, go to either [OpenJDK](https://openjdk.java.net/) or [OracleJDK](https://www.oracle.com/technetwork/java/javase/downloads/index.html) depending on your own project license requirement.
 
### Adding GraphLoom to your build

#### Maven

Repository:
```
<repository>
    <snapshots>
        <enabled>false</enabled>
    </snapshots>
    <id>bintray-jiefenn8-graphloom</id>
    <name>bintray</name>
    <url>https://dl.bintray.com/jiefenn8/graphloom</url>
</repository>
```

Dependency:
```
<dependency>
  <groupId>com.github.jiefenn8.graphloom</groupId>
  <artifactId>graphloom-core</artifactId>
  <version>0.4.5</version>
</dependency>
```

#### Gradle

Repository:
```
repositories {
    maven {
        url  "https://dl.bintray.com/jiefenn8/graphloom" 
    }
}
```

Dependency:
```
dependencies {
  implementation 'com.github.jiefenn8.graphloom:graphloom-core:0.4.5'
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
ConfigMaps r2rmlMap = r2rmlParser.parse("my-rmrl.ttl");

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
