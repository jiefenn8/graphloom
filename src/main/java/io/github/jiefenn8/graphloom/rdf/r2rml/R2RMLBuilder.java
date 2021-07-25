/*
 * Copyright (c) 2019 - GraphLoom contributors (github.com/jiefenn8/graphloom)
 * This software is made available under the terms of Apache License, Version 2.0.
 */

package io.github.jiefenn8.graphloom.rdf.r2rml;

import io.github.jiefenn8.graphloom.api.NodeMap;
import io.github.jiefenn8.graphloom.exceptions.MapperException;
import io.github.jiefenn8.graphloom.exceptions.ParserException;
import io.github.jiefenn8.graphloom.rdf.r2rml.AbstractTermMap.ValuedType;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * This class defines the base methods that manages the building
 * and grouping of subcomponents of a valid R2RMLMap.
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
     * will force the file manager to generate a default URI for
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
            R2RMLMap.Builder builder = new R2RMLMap.Builder();
            r2rmlParser.getNsPrefixMap()
                    .forEach(builder::addNsPrefix);
            r2rmlParser.getTriplesMaps()
                    .stream()
                    .map(this::buildTriplesMap)
                    .forEach(builder::addTriplesMap);

            return builder.build();
        }
        throw new ParserException("Failed to read file " + filenameOrUri + ".");
    }

    /**
     * Builds and returns a TriplesMap with the properties and
     * value defined in the given triples map resource.
     *
     * @param triplesMap the resource with the triples map
     * @return instance of TriplesMap with mapped values
     * @throws MapperException if circular dependency is detected
     */
    private TriplesMap buildTriplesMap(Resource triplesMap) {
        String id = r2rmlParser.getTriplesMapIdName(triplesMap);
        if (!triplesMapRegister.containsKey(id)) {
            triplesMapRegister.put(id, null);
            LogicalTable logicalTable = buildLogicalTable(r2rmlParser.getLogicalTable(triplesMap));
            SubjectMap subjectMap = buildSubjectMap(r2rmlParser.getSubjectMap(triplesMap));
            TriplesMap.Builder builder = new TriplesMap.Builder(id, logicalTable, subjectMap);
            r2rmlParser.listPredicateObjectMaps(triplesMap)
                    .stream()
                    .map(this::buildPredicateObjectMap)
                    .forEach(builder::addPredicateObjectMap);
            TriplesMap tm = builder.build();
            triplesMapRegister.put(id, tm);

            return tm;
        }
        if (triplesMapRegister.get(id) == null) {
            //Triples map should never store null unless there is some form of circular dependency,
            throw new MapperException("Potential circular dependency found in " + id + ".");
        }

        return Objects.requireNonNull(triplesMapRegister.get(id), id + " is null.");
    }

    /**
     * Builds and returns a LogicalTable with the properties and
     * value defined in the given logical table resource.
     *
     * @param logicalTable the resource with the logical table
     * @return instance of LogicalTable with mapped values
     */
    private LogicalTable buildLogicalTable(Resource logicalTable) {
        if (r2rmlParser.isBaseTableOrView(logicalTable)) {
            String tableName = r2rmlParser.getTableName(logicalTable);
            return R2RMLFactory.createLogicalBaseTableOrView(tableName);
        }
        if (r2rmlParser.isR2RMLView(logicalTable)) {
            String sqlQuery = r2rmlParser.getSqlQuery(logicalTable);
            String sqlVersion = r2rmlParser.getVersion(logicalTable);

            return R2RMLFactory.createLogicalR2RMLView(sqlQuery, sqlVersion);
        }
        throw new ParserException("No BaseTableOrView or R2RMLView property found.");
    }

    /**
     * Builds and returns a SubjectMap with the properties and
     * values defined in the given subject map resource located
     * in the given statement.
     *
     * @param sm the statement containing the subject map
     *           resource to map over
     * @return instance of SubjectMap with mapped values
     */
    private SubjectMap buildSubjectMap(Statement sm) {
        ValuedType valuedType = getTermMapValuedType(sm);
        return new SubjectMap.Builder(getTermMapValue(sm, valuedType), valuedType)
                .addEntityClasses(r2rmlParser.listEntityClasses(sm.getResource()))
                .build();
    }

    /**
     * Returns the RDFNode of a TermMap statement with it specified ValuedType.
     *
     * @param tm   the statement containing a TermMap to extract value
     * @param type the type the TermMap and value is
     * @return the node of the TermMap value
     */
    private RDFNode getTermMapValue(Statement tm, ValuedType type) {
        return switch (type) {
            case CONSTANT -> r2rmlParser.getConstantValue(tm);
            case TEMPLATE -> r2rmlParser.getTemplateNode(tm);
            case COLUMN -> r2rmlParser.getColumnNode(tm);
        };
    }

    /**
     * Returns the ValuedType for this TermMap located in this statement.
     *
     * @param tm the statement containing a TermMap to identify
     * @return ValuedType of the TermMap located in statement
     */
    private ValuedType getTermMapValuedType(Statement tm) {
        if (r2rmlParser.isConstant(tm)) {
            return ValuedType.CONSTANT;
        }
        Resource resource = tm.getResource();
        if (r2rmlParser.isTemplate(resource)) {
            return ValuedType.TEMPLATE;
        }
        if (r2rmlParser.isColumn(resource)) {
            return ValuedType.COLUMN;
        }

        throw new ParserException(tm + " is not a TermMap.");
    }

    /**
     * Returns a PredicateMap and ObjectMap pair using the statement
     * containing the predicate object map property to build both
     * predicate map and object map to return as a pair.
     *
     * @param pom the statement with the predicate object map property
     * @return pair of both PredicateMap and ObjectMap built
     */
    private Pair<PredicateMap, NodeMap> buildPredicateObjectMap(Statement pom) {
        Resource predicateObjectMap = pom.getResource();
        PredicateMap predicateMap = buildPredicateMap(r2rmlParser.getPredicateMap(predicateObjectMap));
        NodeMap objectMap = buildObjectMap(pom.getSubject(), r2rmlParser.getObjectMap(predicateObjectMap));

        return new ImmutablePair<>(predicateMap, objectMap);
    }

    /**
     * Returns a predicate map instance using the statement containing the
     * predicate map resource. As this is a term map, the statement is required
     * to determine the valued term map type to create the object map as.
     *
     * @param pm the statement containing the predicate map resource
     * @return instance of predicate map with mapped values
     */
    private PredicateMap buildPredicateMap(Statement pm) {
        ValuedType type = getTermMapValuedType(pm);
        RDFNode base = getTermMapValue(pm, type);
        return new PredicateMap.Builder(base, type).build();
    }

    /**
     * Returns either an object map or referencing object map instance
     * depending on the evaluation carried out in this function.
     * The triples map given will be used to check if the referencing object
     * map does not reference to its own root triples map.
     *
     * @param triplesMap the resource of the root triples map
     * @param om         the statement containing the object map resource
     * @return instance of either object map or referencing object map with
     * mapped values
     */
    private NodeMap buildObjectMap(Resource triplesMap, Statement om) {
        Resource objectMap = om.getResource();
        if (r2rmlParser.isRefObjectMap(objectMap)) {
            return buildRefObjectMap(triplesMap, objectMap);
        }
        return buildDefaultObjectMap(om);
    }

    /**
     * Returns an object map instance using the statement that contains the
     * object map resource. As this is a term map, the statement is required
     * to determine the valued term map type to create the object map as.
     *
     * @param om the statement containing the object map resource
     * @return instance of object map with mapped values
     */
    private ObjectMap buildDefaultObjectMap(Statement om) {
        ValuedType type = getTermMapValuedType(om);
        RDFNode base = getTermMapValue(om, type);
        return new ObjectMap.Builder(base, type).build();
    }

    /**
     * Returns a referencing object map instance using the object map resource.
     * The triples map given will be used to check if the referencing object
     * map does not reference to its own root triples map.
     *
     * @param triplesMap the resource to compare against reference
     * @param objectMap  the resource to extract data to build instance
     * @return instance of referencing objet map with mapped values
     */
    private RefObjectMap buildRefObjectMap(Resource triplesMap, Resource objectMap) {
        Resource triplesMapRef = r2rmlParser.getParentTriplesMap(objectMap);
        if (triplesMap.equals(triplesMapRef)) {
            throw new ParserException("RefObjectMap must not reference own parent " + triplesMap + ".");
        }
        TriplesMap parent = buildTriplesMap(triplesMapRef);
        RefObjectMap.Builder builder = new RefObjectMap.Builder(parent);
        if (r2rmlParser.hasJoinCondition(objectMap)) {
            Set<Resource> joins = r2rmlParser.listJoinConditions(objectMap);
            joins.forEach((j) -> {
                String parentQuery = r2rmlParser.getParentQuery(j);
                String childQuery = r2rmlParser.getChildQuery(j);
                builder.addJoinCondition(parentQuery, childQuery);
            });
        }

        return builder.build();
    }
}
