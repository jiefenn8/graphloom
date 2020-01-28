/*
 * Copyright (c) 2019 - GraphLoom contributors (github.com/jiefenn8/graphloom
 * This software is made available under the terms of Apache License, Version 2.0.
 */

package com.github.jiefenn8.graphloom.rdf.parser;

import com.github.jiefenn8.graphloom.api.NodeMap;
import com.github.jiefenn8.graphloom.exceptions.ParserException;
import com.github.jiefenn8.graphloom.rdf.r2rml.*;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import static com.github.jiefenn8.graphloom.rdf.r2rml.TermMap.TermType;

/**
 * This class defines the base methods that manages the building
 * and grouping of sub components of a valid R2RMLMap.
 */
public class R2RMLBuilder {

    private final R2RMLParser r2rmlParser;
    private Map<String, TriplesMap> triplesMapRegister = new HashMap<>();

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
            R2RMLMap.Builder r2rmlMapBuilder = new R2RMLMap.Builder();

            r2rmlParser.getNsPrefixMap()
                    .forEach(r2rmlMapBuilder::addNsPrefix);

            r2rmlParser.getTriplesMaps()
                    .stream()
                    .map(this::buildTriplesMap)
                    .forEach(r2rmlMapBuilder::addTriplesMap);

            return r2rmlMapBuilder.build();
        }

        throw new ParserException("Failed to read file %s.", filenameOrUri);
    }

    /**
     * Builds and returns a TriplesMap with the properties and
     * value defined in the given triples map resource.
     *
     * @param subject the resource with the triples map to map
     * @return instance of TriplesMap with mapped values
     * @throws NullPointerException if the resource is null
     */
    private TriplesMap buildTriplesMap(Resource subject) {
        String triplesMapIdName = r2rmlParser.getTriplesMapIdName(subject);
        if (triplesMapRegister.containsKey(triplesMapIdName)) {
            TriplesMap triplesMap = triplesMapRegister.get(triplesMapIdName);
            if (triplesMap == null) {
                throw new ParserException("Potential circular dependency found for %s.", subject);
            }

            return triplesMap;
        }

        triplesMapRegister.put(triplesMapIdName, null);
        LogicalTable logicalTable = buildLogicalTable(r2rmlParser.getLogicalTable(subject));
        SubjectMap subjectMap = buildSubjectMap(r2rmlParser.getSubjectMap(subject));
        TriplesMap.Builder builder = new TriplesMap.Builder(triplesMapIdName, logicalTable, subjectMap);

        r2rmlParser.listPredicateObjectMaps(subject)
                .stream()
                .map(this::buildPredicateObjectMap)
                .forEach(builder::addPredicateObjectMap);

        TriplesMap triplesMap = builder.build();
        triplesMapRegister.put(triplesMapIdName, triplesMap);

        return triplesMap;
    }

    /**
     * Builds and returns a LogicalTable with the properties and
     * value defined in the given logical table resource.
     *
     * @param subject the resource with the logical table to map
     * @return instance of LogicalTable with mapped values
     */
    private LogicalTable buildLogicalTable(Resource subject) {
        if (r2rmlParser.isBaseTableOrView(subject)) {
            String tableName = r2rmlParser.getTableName(subject);
            return R2RMLFactory.createLogicalBaseTableOrView(tableName);
        }

        if (r2rmlParser.isR2RMLView(subject)) {
            String sqlQuery = r2rmlParser.getSqlQuery(subject);
            String sqlVersion = r2rmlParser.getVersion(subject);

            return R2RMLFactory.createLogicalR2RMLView(sqlQuery, sqlVersion);
        }

        throw new ParserException("No BaseTableOrView or R2RMLView property found.");
    }

    /**
     * Builds and returns a SubjectMap with the properties and
     * values defined in the given subject map resource located
     * in the given statement.
     *
     * @param triple the statement containing the subject map
     *               resource to map over
     * @return instance of SubjectMap with mapped values
     */
    private SubjectMap buildSubjectMap(Statement triple) {
        SubjectMap subjectMap = buildTermMap(triple,
                R2RMLFactory::createSubjectMap,
                TermType.IRI);

        r2rmlParser.listEntityClasses(triple.getResource())
                .forEach(subjectMap::addEntityClass);

        return subjectMap;
    }

    /**
     * Returns a PredicateMap and ObjectMap pair using the statement
     * containing the predicate object map property to build both
     * predicate map and object map to return as a pair.
     *
     * @param triple the statement with the predicate object map
     *               property
     * @return pair of both PredicateMap and ObjectMap built
     */
    private Pair<PredicateMap, NodeMap> buildPredicateObjectMap(Statement triple) {
        Resource parentTriplesMap = triple.getSubject();
        Resource predicateObjectMapResource = triple.getResource();

        Statement predicateMapTriple = r2rmlParser.getPredicateMap(predicateObjectMapResource);
        PredicateMap predicateMap = buildTermMap(predicateMapTriple, R2RMLFactory::createPredicateMap, TermType.IRI);

        Statement objectMapTriple = r2rmlParser.getObjectMap(predicateObjectMapResource);
        Resource objectMapResource = objectMapTriple.getResource();
        if (r2rmlParser.isRefObjectMap(objectMapResource)) {
            Resource refTriplesMapResource = r2rmlParser.getParentTriplesMap(objectMapResource);
            if (parentTriplesMap.equals(refTriplesMapResource)) {
                throw new ParserException("RefObjectMap must not reference own parent %s.", parentTriplesMap);
            }

            String refTriplesMapId = r2rmlParser.getTriplesMapIdName(refTriplesMapResource);
            TriplesMap refTriplesMap = triplesMapRegister.get(refTriplesMapId);
            if (refTriplesMap == null) {
                refTriplesMap = buildTriplesMap(refTriplesMapResource);
            }

            RefObjectMap.Builder refObjectMapBuilder = new RefObjectMap.Builder(refTriplesMap);
            if (r2rmlParser.hasJoinCondition(objectMapResource)) {
                Set<Resource> joins = r2rmlParser.listJoinConditions(objectMapResource);
                for (Resource joinConditionResource : joins) {
                    String parent = r2rmlParser.getParentQuery(joinConditionResource);
                    String child = r2rmlParser.getChildQuery(joinConditionResource);
                    refObjectMapBuilder.addJoinCondition(parent, child);
                }
            }

            return new ImmutablePair<>(predicateMap, refObjectMapBuilder.build());
        }

        ObjectMap objectMap = buildTermMap(objectMapTriple, R2RMLFactory::createObjectMap, TermType.LITERAL);
        return new ImmutablePair<>(predicateMap, objectMap);
    }

    /**
     * Builds and returns a TermMap with the properties and
     * values defined in the given term map resource located in
     * the given statement; or throws a {@code ParserException}
     * if the statement is not a TermMap.
     *
     * @param triple   the statement containing the term map
     *                 resource to map over
     * @param action   the function to create and populate the
     *                 related R2RMLMap term map
     * @param termType the default term type of this term map
     * @param <R>      the type of TermMap returned
     * @return instance of TermMap with mapped values
     * @throws ParserException if statement is not a term map
     */
    private <R> R buildTermMap(Statement triple, Function<TermMap, ? extends R> action, TermType termType) {
        if (r2rmlParser.isConstant(triple)) {
            return action.apply(buildConstantTermMap(triple));
        }

        Resource resource = triple.getResource();
        if (r2rmlParser.isTemplate(resource)) {
            return action.apply(buildTemplateTermMap(resource, termType));
        }

        if (r2rmlParser.isColumn(resource)) {
            return action.apply(buildColumnTermMap(resource, termType));
        }

        throw new ParserException("%s is not a TermMap.", triple);
    }

    /**
     * Builds and returns a TermMap with the properties and
     * values defined in the given term map that is a constant
     * valued term map.
     *
     * @param triple the statement containing the term map
     *               resource to map over
     * @return instance of TermMap with mapped values
     */
    private TermMap buildConstantTermMap(Statement triple) {
        RDFNode constantValue = r2rmlParser.getConstantValue(triple);
        return R2RMLFactory.createConstantTermMap(constantValue);
    }

    /**
     * Builds and returns a TermMap with the properties and values
     * defined in the given term map that is a template valued
     * term map.
     *
     * @param subject  the resource with the term map to map over
     * @param termType the default term type of this term map
     * @return instance of TermMap with mapped values
     */
    private TermMap buildTemplateTermMap(Resource subject, TermType termType) {
        String templateString = r2rmlParser.getTemplateValue(subject);
        return R2RMLFactory.createTemplateTermMap(templateString, termType);
    }

    /**
     * Builds and returns a TermMap with the properties and values
     * defined in the given term map that is a column valued
     * term map.
     *
     * @param subject  the resource with the term map to map over
     * @param termType the default term type of this term map
     * @return instance of TermMap with mapped values
     */
    private TermMap buildColumnTermMap(Resource subject, TermType termType) {
        String columnName = r2rmlParser.getColumnName(subject);
        return R2RMLFactory.createColumnTermMap(columnName, termType);
    }
}
