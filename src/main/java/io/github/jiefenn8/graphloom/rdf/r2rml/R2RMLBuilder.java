/*
 * Copyright (c) 2019 - GraphLoom contributors (github.com/jiefenn8/graphloom)
 * This software is made available under the terms of Apache License, Version 2.0.
 */

package io.github.jiefenn8.graphloom.rdf.r2rml;

import io.github.jiefenn8.graphloom.api.NodeMap;
import io.github.jiefenn8.graphloom.exceptions.ParserException;
import io.github.jiefenn8.graphloom.rdf.r2rml.AbstractTermMap.ValuedType;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * This class defines the base methods that manages the building
 * and grouping of sub components of a valid R2RMLMap.
 */
public class R2RMLBuilder {

    private final R2RMLParser r2rmlParser;
    private final Map<String, TriplesMap> triplesMapRegister = new HashMap<>();

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
        ValuedType valuedType = getTermMapValuedType(triple);
        return new SubjectMap.Builder(getTermMapValue(triple, valuedType), valuedType)
                .addEntityClasses(r2rmlParser.listEntityClasses(triple.getResource()))
                .build();
    }

    /**
     * Returns the RDFNode of a TermMap statement with it specified ValuedType.
     *
     * @param triple     the statement containing a TermMap to extract value
     * @param valuedType the type the TermMap and value is
     * @return the node of the TermMap value
     */
    private RDFNode getTermMapValue(Statement triple, ValuedType valuedType) {
        return switch (valuedType) {
            case CONSTANT -> r2rmlParser.getConstantValue(triple);
            case TEMPLATE -> r2rmlParser.getTemplateNode(triple);
            case COLUMN -> r2rmlParser.getColumnNode(triple);
        };
    }

    /**
     * Returns the ValuedType for this TermMap located in this statement.
     *
     * @param triple the statement containing a TermMap to identify
     * @return ValuedType of the TermMap located in statement
     */
    private ValuedType getTermMapValuedType(Statement triple) {
        if (r2rmlParser.isConstant(triple)) {
            return ValuedType.CONSTANT;
        }

        Resource resource = triple.getResource();
        if (r2rmlParser.isTemplate(resource)) {
            return ValuedType.TEMPLATE;
        }

        if (r2rmlParser.isColumn(resource)) {
            return ValuedType.COLUMN;
        }

        throw new ParserException("%s is not a TermMap.", triple);
    }

    /**
     * Returns a PredicateMap and ObjectMap pair using the statement
     * containing the predicate object map property to build both
     * predicate map and object map to return as a pair.
     *
     * @param triple the statement with the predicate object map property
     * @return pair of both PredicateMap and ObjectMap built
     */
    private Pair<PredicateMap, NodeMap> buildPredicateObjectMap(Statement triple) {
        Resource parentTriplesMap = triple.getSubject();
        Resource predicateObjectMapRes = triple.getResource();

        Statement predicateMapTriple = r2rmlParser.getPredicateMap(predicateObjectMapRes);
        ValuedType pmValuedType = getTermMapValuedType(predicateMapTriple);
        RDFNode predicateMapBase = getTermMapValue(predicateMapTriple, pmValuedType);
        PredicateMap predicateMap = new PredicateMap.Builder(predicateMapBase, pmValuedType).build();

        Statement objectMapTriple = r2rmlParser.getObjectMap(predicateObjectMapRes);
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

        ValuedType objectMapValuedType = getTermMapValuedType(objectMapTriple);
        RDFNode objectMapBase = getTermMapValue(objectMapTriple, objectMapValuedType);
        ObjectMap objectMap = new ObjectMap.Builder(objectMapBase, objectMapValuedType).build();
        return new ImmutablePair<>(predicateMap, objectMap);
    }
}
