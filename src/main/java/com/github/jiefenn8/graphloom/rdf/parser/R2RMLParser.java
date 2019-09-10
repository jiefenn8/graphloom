/*
 *    Copyright (c) 2019 - Javen Liu (github.com/jiefenn8)
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.github.jiefenn8.graphloom.rdf.parser;

import com.github.jiefenn8.graphloom.exceptions.ParserException;
import com.github.jiefenn8.graphloom.rdf.r2rml.*;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.jena.rdf.model.*;
import org.apache.jena.util.FileManager;

import java.util.List;
import java.util.stream.Collectors;

/**
 * R2RML Parser
 * <p>
 * This class defines the base methods that manages the mapping
 * configuration of predicate and any objects related to parent entity
 * by the specified predicate.
 */
public class R2RMLParser {

    private final String r2rmlPrefix = "rr";
    private FileManager fileManager;
    private String r2rmlPrefixUri;

    public R2RMLParser() {
        fileManager = new FileManager();
    }

    public R2RMLParser(FileManager manager) {
        fileManager = manager;
    }

    public R2RMLMap parse(String filename) {
        return parse(filename, null);
    }

    public R2RMLMap parse(String filename, String base) {
        Model graph = fileManager.loadModel(filename, base, "TTL");
        return parse(graph);
    }

    public R2RMLMap parse(Model r2rmlGraph) {
        return mapToR2RMLMap(r2rmlGraph);
    }

    private R2RMLMap mapToR2RMLMap(Model r2rmlGraph) {

        r2rmlPrefixUri = r2rmlGraph.getNsPrefixURI(r2rmlPrefix);
        if (r2rmlPrefixUri == null) {
            throw new ParserException("'rr' prefix uri not found.");
        }

        //TriplesMap MUST have a logicalTable property so search for any with that
        R2RMLMap r2rmlMap = new R2RMLMap(r2rmlGraph.getNsPrefixMap());
        findTriplesMap(r2rmlGraph).forEachRemaining(
                (tm) -> r2rmlMap.addTriplesMap(mapToTriplesMap(tm)));

        return r2rmlMap;
    }

    private TriplesMap mapToTriplesMap(Resource tmRes) {
        LogicalTable logicalTable = mapToLogicalTable(findLogicalTable(tmRes));
        SubjectMap subjectMap = mapToSubjectMap(findSubjectMap(tmRes));
        TriplesMap triplesMap = new TriplesMap(logicalTable, subjectMap);
        findPredicateObjectMaps(tmRes).forEach(
                (pom) -> {
                    Pair<PredicateMap, ObjectMap> pomPair = mapToPredicateObjectMap(pom);
                    triplesMap.addRelationNodePair(pomPair.getKey(), pomPair.getValue());
                });

        return triplesMap;
    }

    private ResIterator findTriplesMap(Model root) {
        Property landmark = root.getProperty("rr", "logicalTable");
        if (landmark == null) {
            throw new ParserException("No valid Triples Map with rr:logicalTable found.");
        }

        return root.listResourcesWithProperty(landmark);
    }

    private List<Resource> findPredicateObjectMaps(Resource tmNode) {
        Property predicateObjectMap = ResourceFactory.createProperty(r2rmlPrefixUri, "predicateObjectMap");
        return tmNode.listProperties(predicateObjectMap)
                .toList().stream()
                .map((i) -> i.getObject().asResource())
                .collect(Collectors.toList());
    }

    private Pair<PredicateMap, ObjectMap> mapToPredicateObjectMap(Resource pomNode) {
        //todo: 6.3 : using the rr:predicateMap property, whose value MUST be a predicate map, or..
        Property predicate = ResourceFactory.createProperty(r2rmlPrefixUri, "predicate");
        //todo: 6.3 : using the constant shortcut property rr:object.
        Property objectMap = ResourceFactory.createProperty(r2rmlPrefixUri, "objectMap");

        if (!pomNode.hasProperty(predicate) || !pomNode.hasProperty(objectMap)) {
            throw new ParserException("Predicate or Object Map not found.");
        }

        String predicateUri = pomNode
                .getPropertyResourceValue(predicate).getURI();

        Property predicateProp = ResourceFactory.createProperty(predicateUri);

        Resource objNode = pomNode
                .getProperty(objectMap)
                .getResource();

        return new ImmutablePair<>(new PredicateMap(predicateProp), mapToObjectMap(objNode));
    }

    private ObjectMap mapToObjectMap(Resource omNode) {
        Property column = ResourceFactory.createProperty(r2rmlPrefixUri, "column");
        if (!omNode.hasProperty(column)) {
            //todo: 8.0 : A referencing object map is represented by a resource that: ..
            throw new ParserException("No column found.");
        }

        return new ObjectMap(omNode.getProperty(column).getLiteral().getString());
    }

    private Resource findLogicalTable(Resource tmNode) {
        Property logicalTable = ResourceFactory.createProperty(r2rmlPrefixUri, "logicalTable");
        if (!tmNode.hasProperty(logicalTable)) {
            throw new ParserException("No Logical Table found.");
        }

        //return object : tmNode -> rr:logicalTable -> (object)
        return tmNode.getPropertyResourceValue(logicalTable);
    }

    private Resource findSubjectMap(Resource tmNode) {
        Property subjectMap = ResourceFactory.createProperty(r2rmlPrefixUri, "subjectMap");
        if (!tmNode.hasProperty(subjectMap)) {
            throw new ParserException("No Subject Map found");
        }

        //return object : tmNode -> rr:subjectMap -> (object)
        return tmNode.getPropertyResourceValue(subjectMap);
    }

    private LogicalTable mapToLogicalTable(Resource ltNode) {
        Property tableName = ResourceFactory.createProperty(r2rmlPrefixUri, "tableName");
        if (!ltNode.hasProperty(tableName)) {
            //todo: 5.0 : sql view, r2rml view, logical table row, column name, sql identifier.
            throw new ParserException("No table name found.");
        }

        return new LogicalTable(ltNode.getProperty(tableName).getLiteral().getString());
    }

    private SubjectMap mapToSubjectMap(Resource smNode) {
        Property template = ResourceFactory.createProperty(r2rmlPrefixUri, "template");
        Property classType = ResourceFactory.createProperty(r2rmlPrefixUri, "class");
        if (!smNode.hasProperty(template) && !smNode.hasProperty(classType)) {
            //todo: 6.2 : A subject map MAY have one or more class IRIs.
            throw new ParserException("Template or class type not found.");
        }

        SubjectMap subjectMap = new SubjectMap(smNode.getProperty(template).getLiteral().getString());
        subjectMap.addClass(smNode.getProperty(classType).getResource());
        return subjectMap;
    }
}
