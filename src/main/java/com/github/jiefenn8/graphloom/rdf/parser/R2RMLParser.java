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
import com.github.jiefenn8.graphloom.rdf.r2rml.TermMap.TermMapType;
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

    private Resource findLogicalTable(Resource tmNode) {
        Property logicalTable = ResourceFactory.createProperty(r2rmlPrefixUri, "logicalTable");
        if (!tmNode.hasProperty(logicalTable)) {
            throw new ParserException("No Logical Table found.");
        }

        //return object : tmNode -> rr:logicalTable -> (object)
        return tmNode.getPropertyResourceValue(logicalTable);
    }

    //Return Statement containing SubjectMap property.
    private Statement findSubjectMap(Resource triplesMapRes) {
        return findTermMap(triplesMapRes, "subjectMap", "subject");
    }

    //Return Statement containing PredicateMap property.
    private Statement findPredicateMap(Resource predicateObjectMapRes) {
        return findTermMap(predicateObjectMapRes, "predicateMap", "predicate");
    }

    //Return Statement containing ObjectMap property
    private Statement findObjectMap(Resource predicateObjectMapRes) {
        return findTermMap(predicateObjectMapRes, "objectMap", "object");
    }

    private List<Resource> findPredicateObjectMaps(Resource tmNode) {
        Property predicateObjectMap = ResourceFactory.createProperty(r2rmlPrefixUri, "predicateObjectMap");
        return tmNode.listProperties(predicateObjectMap)
                .toList().stream()
                .map((i) -> i.getObject().asResource())
                .collect(Collectors.toList());
    }

    private LogicalTable mapToLogicalTable(Resource ltNode) {
        Property tableName = ResourceFactory.createProperty(r2rmlPrefixUri, "tableName");
        if (!ltNode.hasProperty(tableName)) {
            //todo: 5.0 : sql view, r2rml view, logical table row, column name, sql identifier.
            throw new ParserException("No table name found.");
        }

        return new LogicalTable(ltNode.getProperty(tableName).getLiteral().getString());
    }

    private SubjectMap mapToSubjectMap(Statement subjectMapStmt) {
        Resource stmtObj = subjectMapStmt.getObject().asResource();
        if (isShortcutConstant(subjectMapStmt, "subject")) {
            return new SubjectMap(TermMapType.CONSTANT, stmtObj);
        }

        Model root = subjectMapStmt.getModel();
        if (isConstant(subjectMapStmt)) {
            Property constProp = root.getProperty(r2rmlPrefixUri, "constant");
            return new SubjectMap(TermMapType.CONSTANT, stmtObj.getPropertyResourceValue(constProp));
        }

        SubjectMap subjectMap = null;
        Property templateProp = root.getProperty(r2rmlPrefixUri, "template");
        if (templateProp != null && stmtObj.hasProperty(templateProp)) {
            String template = stmtObj.getProperty(templateProp).getLiteral().getString();
            subjectMap = new SubjectMap(TermMapType.TEMPLATE, template);
        }

        //todo: Check if subjectMap supports column term map.

        if (subjectMap == null) throw new ParserException("SubjectMap not matched to any TermMap.");
        //todo: 6.2 : A subject map MAY have one or more class IRIs.
        Property classType = root.getProperty(r2rmlPrefixUri, "class");
        if (classType == null || !stmtObj.hasProperty(classType)) throw new ParserException("Class type not found.");
        subjectMap.addClass(subjectMapStmt.getProperty(classType).getResource());

        return subjectMap;
    }

    private Pair<PredicateMap, ObjectMap> mapToPredicateObjectMap(Resource pomNode) {
        PredicateMap predicateMap = mapToPredicateMap(findPredicateMap(pomNode));
        ObjectMap objectMap = mapToObjectMap(findObjectMap(pomNode));
        return new ImmutablePair<>(predicateMap, objectMap);
    }

    private PredicateMap mapToPredicateMap(Statement pmStmt) {
        Resource stmtObj = pmStmt.getObject().asResource();
        Property resAsProp = stmtObj.as(Property.class);
        if (isShortcutConstant(pmStmt, "subject")) return new PredicateMap(TermMapType.CONSTANT, resAsProp);

        Model root = pmStmt.getModel();
        if (isConstant(pmStmt)) {
            Property constProp = root.getProperty(r2rmlPrefixUri, "constant");
            Resource constRes = stmtObj.getPropertyResourceValue(constProp).asResource();
            return new PredicateMap(TermMapType.CONSTANT, constRes.as(Property.class));
        }

        //todo: verify subject map restriction.
        throw new ParserException("Predicate Map restricted to constant only.");
    }

    private ObjectMap mapToObjectMap(Statement omStmt) {
        Resource stmtObj = omStmt.getObject().asResource();
        if (isShortcutConstant(omStmt, "object")) return new ObjectMap(TermMapType.CONSTANT, stmtObj);

        Model root = omStmt.getModel();
        if (isConstant(omStmt)) {
            Property constProp = root.getProperty(r2rmlPrefixUri, "constant");
            Resource constNode = stmtObj.getPropertyResourceValue(constProp);
            return new ObjectMap(TermMapType.CONSTANT, constNode);
        }

        ObjectMap objectMap = null;
        Property templateProp = root.getProperty(r2rmlPrefixUri, "template");
        if (templateProp != null && stmtObj.hasProperty(templateProp)) {
            String template = stmtObj.getProperty(templateProp).getLiteral().getString();
            objectMap = new ObjectMap(TermMapType.TEMPLATE, template);
        }

        Property columnProp = root.getProperty(r2rmlPrefixUri, "column");
        if (columnProp != null && stmtObj.hasProperty(columnProp)) {
            String column = stmtObj.getProperty(columnProp).getLiteral().getString();
            objectMap = new ObjectMap(TermMapType.COLUMN, column);
        }

        if (objectMap == null) throw new ParserException("ObjectMap not matched to any TermMap.");

        return objectMap;
    }

    //Helper methods

    private Statement findTermMap(Resource tmRes, String tmName, String tmConstName) {
        Model root = tmRes.getModel();
        Property termMapProp = root.getProperty(r2rmlPrefixUri, tmName);
        if (termMapProp == null || !tmRes.hasProperty(termMapProp)) {
            termMapProp = root.getProperty(r2rmlPrefixUri, tmConstName);
        }

        Statement termMapStmt = tmRes.getProperty(termMapProp);
        if (termMapStmt == null) throw new ParserException(tmName + " or " + tmConstName + " not found.");

        return termMapStmt;
    }

    public boolean isConstant(Statement stmt) {
        Model root = stmt.getModel();
        Property constProperty = root.getProperty(r2rmlPrefixUri, "constant");
        //If Model doesn't contain any; assume there is none is this node too.
        if (constProperty == null) return false;
        Resource stmtObj = stmt.getObject().asResource();
        return stmtObj.hasProperty(constProperty);
    }

    public boolean isShortcutConstant(Statement stmt, String shortcutName) {
        String stmtPredicate = stmt.getPredicate().getLocalName();
        return stmtPredicate.equals(shortcutName);
    }
}
