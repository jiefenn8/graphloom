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

    //Recommended to use FileManager.get() to get instance.
    public R2RMLParser() {
        fileManager = FileManager.get();
    }

    public R2RMLParser(FileManager manager) {
        fileManager = manager;
    }

    public R2RMLMap parse(String filename) {
        return parse(filename, null);
    }

    public R2RMLMap parse(String filename, String base) {
        return parse(fileManager.loadModel(filename, base, "TTL"));
    }

    public R2RMLMap parse(Model r2rmlGraph) {
        return mapToR2RMLMap(r2rmlGraph);
    }

    private R2RMLMap mapToR2RMLMap(Model r2rmlGraph) {
        r2rmlPrefixUri = r2rmlGraph.getNsPrefixURI(r2rmlPrefix);
        if (r2rmlPrefixUri == null) throw new ParserException("'rr' prefix uri not found.");

        R2RMLMap r2rmlMap = R2RMLFactory.createR2RMLMap(r2rmlGraph.getNsPrefixMap());
        findTriplesMap(r2rmlGraph).forEach(
                (tm) -> r2rmlMap.addTriplesMap(mapToTriplesMap(tm)));

        return r2rmlMap;
    }

    private TriplesMap mapToTriplesMap(Resource tmRes) {
        LogicalTable logicalTable = mapToLogicalTable(findLogicalTable(tmRes));
        SubjectMap subjectMap = mapToSubjectMap(findSubjectMap(tmRes));
        TriplesMap triplesMap = R2RMLFactory.createTriplesMap(logicalTable, subjectMap);
        findPredicateObjectMaps(tmRes).forEach(
                (pom) -> {
                    Pair<PredicateMap, ObjectMap> pomPair = mapToPredicateObjectMap(pom);
                    triplesMap.addRelationNodePair(pomPair.getKey(), pomPair.getValue());
                });

        return triplesMap;
    }

    //TriplesMap MUST have a logicalTable property so search for any with that
    private List<Resource> findTriplesMap(Model root) {
        Property landmark = ResourceFactory.createProperty(r2rmlPrefixUri, "logicalTable");
        List<Resource> triplesMaps = root.listResourcesWithProperty(landmark).toList();
        if (triplesMaps.isEmpty()) throw new ParserException("No valid Triples Map with rr:logicalTable found.");

        return triplesMaps;
    }

    private Resource findLogicalTable(Resource tmNode) {
        Property logicalTable = ResourceFactory.createProperty(r2rmlPrefixUri, "logicalTable");
        if (!tmNode.hasProperty(logicalTable)) throw new ParserException("No Logical Table found.");

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
        //todo: 5.0 : sql view, r2rml view, logical table row, column name, sql identifier.
        if (!ltNode.hasProperty(tableName)) throw new ParserException("No table name found.");
        String tableSource = ltNode.getProperty(tableName).getLiteral().getString();

        return R2RMLFactory.createLogicalTableBaseTableOrView(tableSource);
    }

    private SubjectMap mapToSubjectMap(Statement subjectMapStmt) {
        Resource stmtObj = subjectMapStmt.getObject().asResource();
        if (isShortcutConstant(subjectMapStmt, "subject")) {
            return R2RMLFactory.createConstSubjectMap(stmtObj);
        }

        if (isConstant(subjectMapStmt)) {
            Property constProp = getResourceProperty(stmtObj, r2rmlPrefixUri, "constant");
            return R2RMLFactory.createConstSubjectMap(stmtObj.getPropertyResourceValue(constProp));
        }

        Property templateProp = ResourceFactory.createProperty(r2rmlPrefixUri, "template");
        if (!stmtObj.hasProperty(templateProp)) throw new ParserException(stmtObj + " is not a TermMap.");
        String template = stmtObj.getProperty(templateProp).getLiteral().getString();
        SubjectMap subjectMap = R2RMLFactory.createTmplSubjectMap(template);

        //todo: Check if subjectMap supports column term map.
        //todo: 6.2 : A subject map MAY have one or more class IRIs.
        Property classProp = ResourceFactory.createProperty(r2rmlPrefixUri, "class");
        if (!stmtObj.hasProperty(classProp)) throw new ParserException("Class type not found.");
        subjectMap.addClass(subjectMapStmt.getProperty(classProp).getResource());

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
        if (isShortcutConstant(pmStmt, "predicate")) {
            return R2RMLFactory.createConstPredicateMap(resAsProp);
        }

        if (isConstant(pmStmt)) {
            Property constProp = getResourceProperty(stmtObj, r2rmlPrefixUri, "constant");
            Resource constRes = stmtObj.getPropertyResourceValue(constProp).asResource();
            return R2RMLFactory.createConstPredicateMap(constRes.as(Property.class));
        }

        //todo: verify subject map restriction.
        throw new ParserException("Predicate Map restricted to constant only.");
    }

    private ObjectMap mapToObjectMap(Statement omStmt) {
        Resource stmtObj = omStmt.getObject().asResource();
        if (isShortcutConstant(omStmt, "object")) return R2RMLFactory.createConstObjectMap(stmtObj);

        if (isConstant(omStmt)) {
            Property constProp = getResourceProperty(stmtObj, r2rmlPrefixUri, "constant");
            Resource constNode = stmtObj.getPropertyResourceValue(constProp);
            return R2RMLFactory.createConstObjectMap(constNode);
        }

        Property templateProp = ResourceFactory.createProperty(r2rmlPrefixUri, "template");
        if (stmtObj.hasProperty(templateProp)) {
            String template = stmtObj.getProperty(templateProp).getLiteral().getString();
            return R2RMLFactory.createTmplObjectMap(template);
        }

        Property columnProp = ResourceFactory.createProperty(r2rmlPrefixUri, "column");
        if (!stmtObj.hasProperty(columnProp)) throw new ParserException(stmtObj + " is not a TermMap.");
        String column = stmtObj.getProperty(columnProp).getLiteral().getString();

        return R2RMLFactory.createColObjectMap(column);
    }

    //Helper methods

    //Creates property to search against Resource. Returns true if there is a match.
    private boolean resourceHasProperty(Resource res, String namespace, String localName) {
        Property property = ResourceFactory.createProperty(namespace, localName);
        return res.hasProperty(property);
    }

    //Get property from a Resource statement. Check before call else NullPointerException if property doesn't exist.
    private Property getResourceProperty(Resource res, String namespace, String localName) {
        Property property = ResourceFactory.createProperty(namespace, localName);
        return res.getProperty(property).getPredicate();
    }

    //Find a TermMap from a Resource.
    private Statement findTermMap(Resource tmRes, String tmName, String tmConstName) {
        Property targetProperty = ResourceFactory.createProperty(r2rmlPrefixUri, tmName);
        if (!tmRes.hasProperty(targetProperty)) {
            targetProperty = ResourceFactory.createProperty(r2rmlPrefixUri, tmConstName);
        }

        Statement termMapStmt = tmRes.getProperty(targetProperty);
        if (termMapStmt == null) throw new ParserException(tmName + " or " + tmConstName + " not found.");

        return termMapStmt;
    }

    //Check if Statement Object contains a constant Property.
    private boolean isConstant(Statement stmt) {
        Resource stmtObj = stmt.getObject().asResource();
        return resourceHasProperty(stmtObj, r2rmlPrefixUri, "constant");
    }

    //Check if Statement Predicate is a constant shortcut.
    private boolean isShortcutConstant(Statement stmt, String shortcutName) {
        String stmtPredicate = stmt.getPredicate().getLocalName();
        return stmtPredicate.equals(shortcutName);
    }
}
