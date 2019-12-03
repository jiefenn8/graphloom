/*
 *    Copyright (c) 2019 - GraphLoom contributors (github.com/jiefenn8/graphloom)
 *    This software is made available under the terms of Apache License, Version 2.0.
 */

package com.github.jiefenn8.graphloom.rdf.parser;

import com.github.jiefenn8.graphloom.exceptions.ParserException;
import com.github.jiefenn8.graphloom.rdf.r2rml.*;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;

import java.util.function.Function;

public class R2RMLBuilder {

    private final R2RMLParser r2rmlParser;

    public R2RMLBuilder() {
        r2rmlParser = new R2RMLParser();
    }

    public R2RMLBuilder(R2RMLParser r2rmlParser) {
        this.r2rmlParser = r2rmlParser;
    }

    public R2RMLMap parse(String filename) {
        return parse(filename, null);
    }

    public R2RMLMap parse(String filename, String baseUri) {
        if (r2rmlParser.parse(filename, baseUri)) {
            return buildR2RMLMap();
        }

        throw new ParserException("Failed to read R2RML file %s.", filename);
    }

    private R2RMLMap buildR2RMLMap() {
        R2RMLMap.Builder r2rmlMapBuilder = new R2RMLMap.Builder();

        r2rmlParser.getNsPrefixMap()
                .forEach(r2rmlMapBuilder::addNsPrefix);

        r2rmlParser.getTriplesMap()
                .stream()
                .map(this::buildTriplesMap)
                .forEach(r2rmlMapBuilder::addTriplesMap);

        return r2rmlMapBuilder.build();
    }

    private TriplesMap buildTriplesMap(Resource triplesMap) {
        LogicalTable logicalTable = buildLogicalTable(r2rmlParser.getLogicalTable(triplesMap));
        SubjectMap subjectMap = buildSubjectMap(r2rmlParser.getSubjectMap(triplesMap));
        TriplesMap.Builder builder = new TriplesMap.Builder(logicalTable, subjectMap);

        r2rmlParser.getPredicateObjectMaps(triplesMap)
                .stream()
                .map(this::buildPredicateObjectMap)
                .forEach(builder::addPredicateObjectMap);

        return builder.build();
    }

    private LogicalTable buildLogicalTable(Resource logicalTable) {
        if (r2rmlParser.isBaseTableOrView(logicalTable)) {
            String tableName = r2rmlParser.getTableName(logicalTable);
            return R2RMLFactory.createBaseTableOrView(tableName);
        }

        if (r2rmlParser.isR2RMLView(logicalTable)) {
            String sqlQuery = r2rmlParser.getSqlQuery(logicalTable);
            String sqlVersion = r2rmlParser.getVersion(logicalTable);
            return R2RMLFactory.createR2RMLView(sqlQuery, sqlVersion);
        }

        throw new ParserException("No BaseTableOrView or R2RMLView property found.");
    }

    private SubjectMap buildSubjectMap(Statement statement) {
        SubjectMap subjectMap = buildTermMap(statement, R2RMLFactory::createSubjectMap, TermMap.TermType.IRI);
        r2rmlParser.getEntityClasses(statement.getResource())
                .forEach(subjectMap::addEntityClass);

        return subjectMap;
    }

    private Pair<PredicateMap, ObjectMap> buildPredicateObjectMap(Resource resource) {
        PredicateMap predicateMap = buildTermMap(r2rmlParser.getPredicateMap(resource),
                R2RMLFactory::createPredicateMap,
                TermMap.TermType.IRI);

        ObjectMap objectMap = buildTermMap(r2rmlParser.getObjectMap(resource),
                R2RMLFactory::createObjectMap,
                TermMap.TermType.LITERAL);

        return new ImmutablePair<>(predicateMap, objectMap);
    }

    private <R> R buildTermMap(Statement statement, Function<TermMap, ? extends R> action, TermMap.TermType termType) {
        if (r2rmlParser.isConstant(statement)) {
            return action.apply(buildConstantTermMap(statement));
        }

        Resource resource = statement.getResource();
        if (r2rmlParser.isTemplate(resource)) {
            return action.apply(buildTemplateTermMap(resource, termType));
        }

        if (r2rmlParser.isColumn(resource)) {
            return action.apply(buildColumnTermMap(resource, termType));
        }

        throw new ParserException("%s is not a TermMap.", statement);
    }

    private TermMap buildConstantTermMap(Statement statement) {
        return R2RMLFactory.createConstantTermMap(r2rmlParser.getConstantValue(statement));
    }

    private TermMap buildTemplateTermMap(Resource resource, TermMap.TermType termType) {
        String templateString = r2rmlParser.getTemplateValue(resource);
        return R2RMLFactory.createTemplateTermMap(templateString, termType);
    }

    private TermMap buildColumnTermMap(Resource resource, TermMap.TermType termType) {
        String columnName = r2rmlParser.getColumnName(resource).getString();
        return R2RMLFactory.createColumnTermMap(columnName, termType);
    }
}
