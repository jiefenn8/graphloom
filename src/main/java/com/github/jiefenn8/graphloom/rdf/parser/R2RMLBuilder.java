/*
 *    Copyright (c) 2019 - GraphLoom contributors (github.com/jiefenn8/graphloom)
 *    This software is made available under the terms of Apache License, Version 2.0.
 */

package com.github.jiefenn8.graphloom.rdf.parser;

import com.github.jiefenn8.graphloom.exceptions.ParserException;
import com.github.jiefenn8.graphloom.rdf.r2rml.*;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;

import java.util.function.Function;

/**
 * This class defines the base methods that manages the
 * building and grouping of sub components of a valid R2RMLMap.
 */
public class R2RMLBuilder {

    private final R2RMLParser r2rmlParser;

    /**
     * Constructs a R2RMLBuilder with default parser.
     */
    public R2RMLBuilder() {
        r2rmlParser = new R2RMLParser();
    }

    /**
     * Constructs a R2RMLBuilder with the specified parser.
     *
     * @param r2rmlParser the parser to handle the parsing
     * @see #parse(String, String)
     */
    public R2RMLBuilder(R2RMLParser r2rmlParser) {
        this.r2rmlParser = r2rmlParser;
    }

    /**
     * Parse a given r2rml document and returns an R2RMLMap with
     * the mapping configurations defined in the r2rml document
     * or throws a {@code ParserException} if fails to read the
     * r2rml document. This method sets the base URI null which
     * will force the file manger to generate a default URI for
     * the parser to use.
     *
     * @param filenameOrUri the filename or URI of the document
     * @return instance of R2RMLMap with the loaded r2rml configs
     * @throws ParserException if r2rml document fails to be parsed
     */
    public R2RMLMap parse(String filenameOrUri) {
        return parse(filenameOrUri, null);
    }

    /**
     * Parse a given r2rml document and returns an R2RMLMap with
     * the mapping configurations defined in the r2rml document
     * or throws a {@code ParserException} if fails to read the
     * r2rml document.
     *
     * @param filenameOrUri the filename or URI of the document
     * @param baseUri       the base URI to use for the mapping
     * @return instance of R2RMLMap after parsing the file
     * @throws ParserException if r2rml document fails to be parsed
     */
    public R2RMLMap parse(String filenameOrUri, String baseUri) {
        if (r2rmlParser.parse(filenameOrUri, baseUri)) {
            return buildR2RMLMap();
        }

        throw new ParserException("Failed to read file %s.", filenameOrUri);
    }

    /**
     * Builds and returns a complete R2RMLMap that fully with the
     * customised mapping defined in the parser.
     *
     * @return instance of R2RMLMap with mapped values
     */
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

    /**
     * Builds and returns a TriplesMap with the properties and value
     * defined in the given triples map resource.
     *
     * @param triplesMap the resource with the triples map to map over
     * @return instance of TriplesMap with mapped values
     */
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

    /**
     * Builds and returns a LogicalTable with the properties and
     * value defined in the given logical table resource.
     *
     * @param logicalTable the resource with the logical table
     *                     to map over
     * @return instance of LogicalTable with mapped values
     */
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

    /**
     * Builds and returns a SubjectMap with the properties and values
     * defined in the given subject map resource located in
     * the given statement.
     *
     * @param subjectMapStmt the statement containing the subject
     *                       map resource to map over
     * @return instance of SubjectMap with mapped values
     */
    private SubjectMap buildSubjectMap(Statement subjectMapStmt) {
        SubjectMap subjectMap = buildTermMap(subjectMapStmt,
                R2RMLFactory::createSubjectMap,
                TermMap.TermType.IRI);

        r2rmlParser.getEntityClasses(subjectMapStmt.getResource())
                .forEach(subjectMap::addEntityClass);

        return subjectMap;
    }

    /**
     * Returns a PredicateMap and ObjectMap pair using the resource
     * identified as predicate object map to build both predicate map
     * and object map to return as a pair.
     *
     * @param predicateObjectMap the resource with the predicate and
     *                           object map properties
     * @return pair of both PredicateMap and ObjectMap built
     */
    private Pair<PredicateMap, ObjectMap> buildPredicateObjectMap(Resource predicateObjectMap) {
        PredicateMap predicateMap = buildTermMap(r2rmlParser.getPredicateMap(predicateObjectMap),
                R2RMLFactory::createPredicateMap,
                TermMap.TermType.IRI);

        ObjectMap objectMap = buildTermMap(r2rmlParser.getObjectMap(predicateObjectMap),
                R2RMLFactory::createObjectMap,
                TermMap.TermType.LITERAL);

        return new ImmutablePair<>(predicateMap, objectMap);
    }

    /**
     * Builds and returns a TermMap with the properties and values
     * defined in the given term map resource located in
     * the given statement; or throws a {@code ParserException}
     * if the statement is not a TermMap.
     *
     * @param termMapStmt the statement containing the term map
     *                    resource to map over
     * @param action      the function to create and populate the related
     *                    R2RMLMap term map
     * @param termType    the default term type of this term map
     * @param <R>         the type of TermMap returned
     * @return instance of TermMap with mapped values
     * @throws ParserException if statement is not a term map
     */
    private <R> R buildTermMap(Statement termMapStmt, Function<TermMap, ? extends R> action, TermMap.TermType termType) {
        if (r2rmlParser.isConstant(termMapStmt)) {
            return action.apply(buildConstantTermMap(termMapStmt));
        }

        Resource resource = termMapStmt.getResource();
        if (r2rmlParser.isTemplate(resource)) {
            return action.apply(buildTemplateTermMap(resource, termType));
        }

        if (r2rmlParser.isColumn(resource)) {
            return action.apply(buildColumnTermMap(resource, termType));
        }

        throw new ParserException("%s is not a TermMap.", termMapStmt);
    }

    /**
     * Builds and returns a TermMap with the properties and values
     * defined in the given term map that is a constant valued
     * term map.
     *
     * @param termMapStmt the statement containing the term map
     *                    resource to map over
     * @return instance of TermMap with mapped values
     */
    private TermMap buildConstantTermMap(Statement termMapStmt) {
        RDFNode constantValue = r2rmlParser.getConstantValue(termMapStmt);
        return R2RMLFactory.createConstantTermMap(constantValue);
    }

    /**
     * Builds and returns a TermMap with the properties and values
     * defined in the given term map that is a template valued
     * term map.
     *
     * @param termMap  the resource with the term map to map over
     * @param termType the default term type of this term map
     * @return instance of TermMap with mapped values
     */
    private TermMap buildTemplateTermMap(Resource termMap, TermMap.TermType termType) {
        String templateString = r2rmlParser.getTemplateValue(termMap);
        return R2RMLFactory.createTemplateTermMap(templateString, termType);
    }

    /**
     * Builds and returns a TermMap with the properties and values
     * defined in the given term map that is a column valued term map.
     *
     * @param termMap  the resource with the term map to map over
     * @param termType the default term type of this term map
     * @return instance of TermMap with mapped values
     */
    private TermMap buildColumnTermMap(Resource termMap, TermMap.TermType termType) {
        String columnName = r2rmlParser.getColumnName(termMap).getString();
        return R2RMLFactory.createColumnTermMap(columnName, termType);
    }
}
