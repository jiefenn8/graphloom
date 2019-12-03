/*
 *    Copyright (c) 2019 - GraphLoom contributors (github.com/jiefenn8/graphloom)
 *    This software is made available under the terms of Apache License, Version 2.0.
 */

package com.github.jiefenn8.graphloom.rdf.parser;

import com.github.jiefenn8.graphloom.exceptions.ParserException;
import com.github.jiefenn8.graphloom.rdf.r2rml.R2RMLSyntax;
import org.apache.commons.lang3.StringUtils;
import org.apache.jena.rdf.model.*;
import org.apache.jena.util.FileManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * This class defines the base methods that manages the parsing of
 * rdf mapping document to iterable r2rml terms.
 */
public class R2RMLParser {

    private static final Logger logger = LogManager.getLogger();

    private final FileManager fileManager;
    private String filenameOrUri = StringUtils.EMPTY;
    private Model r2rmlGraph = ModelFactory.createDefaultModel();

    /**
     * Constructs a R2RMLParser with default file manager instance.
     */
    protected R2RMLParser() {
        fileManager = FileManager.get();
    }

    /**
     * Constructs a R2RMLParser with the specified file manager instance.
     *
     * @param fileManager the manager to handle the loading of file
     */
    protected R2RMLParser(FileManager fileManager) {
        this.fileManager = checkNotNull(fileManager);
    }

    /**
     * Loads a given r2rml document into a model. Returns true if parsing
     * of document is successful and this parser does not have another
     * document opened. To close the opened document on this parser, refer
     * to the {@link R2RMLParser#close()} method.
     *
     * @param filenameOrUri the filename or URI of the document
     * @param baseUri the base URI of the mapping in the document
     * @return true if the document is loaded successfully otherwise false
     */
    protected boolean parse(String filenameOrUri, String baseUri) {
        if (!r2rmlGraph.isClosed() && !this.filenameOrUri.isEmpty()) {
            logger.warn("Loaded file {} on parser; skipping.", this.filenameOrUri);
            logger.debug("File loaded on parser, use close() to close file to reuse parser.");
            return false;
        }

        this.filenameOrUri = filenameOrUri;
        r2rmlGraph = fileManager.loadModel(this.filenameOrUri, baseUri, "TTL");
        return true;
    }

    /**
     * Close the model of the loaded r2rml document on this parser.
     * Returns true if the model is closed.
     *
     * @return true of the model is closed otherwise false
     */
    protected boolean close() {
        r2rmlGraph.close();
        logger.info("Closing current R2RML file {}.", filenameOrUri);
        return r2rmlGraph.isClosed();
    }

    /**
     * Returns the map of namespace and their prefix from the model.
     *
     * @return map containing prefixes and their namespace
     */
    protected Map<String, String> getNsPrefixMap() {
        return r2rmlGraph.getNsPrefixMap();
    }

    //TriplesMap parsing

    /**
     * Returns a list of triples map represented as resources from the model.
     *
     * @return list of triples map as resources
     */
    protected List<Resource> getTriplesMap() {
        return r2rmlGraph.listResourcesWithProperty(R2RMLSyntax.logicalTable)
                .filterKeep(this::hasSubjectMap)
                .toList();
    }

    /**
     * Returns true if given triples map resource has subject map property.
     *
     * @param triplesMap the resource representing a triples map
     * @return true if the resource contains subject map property
     */
    private boolean hasSubjectMap(Resource triplesMap) {
        return (triplesMap.hasProperty(R2RMLSyntax.subjectMap) || triplesMap.hasProperty(R2RMLSyntax.subject));
    }

    //LogicalTable parsing

    /**
     *  Returns the logical table property in the given triples map resource
     *  as a resource.
     *
     * @param tripleMap the resource representing a triples map
     * @return resource of the logical table property
     */
    protected Resource getLogicalTable(Resource tripleMap) {
        return getPropertyResourceValue(tripleMap, R2RMLSyntax.logicalTable)
                .asResource();
    }

    /**
     * Returns true if given logical table resource is a r2rml view subclass.
     *
     * @param logicalTable the resource representing a logical table
     * @return true if the resource is a r2rml view
     */
    protected boolean isR2RMLView(Resource logicalTable) {
        return (logicalTable.hasProperty(R2RMLSyntax.sqlQuery) && logicalTable.hasProperty(R2RMLSyntax.sqlVersion));
    }

    /**
     * Returns true if given logical table is a base table or view subclass.
     *
     * @param logicalTable the resource representing a logical table
     * @return true if the resource is a base table or view
     */
    protected boolean isBaseTableOrView(Resource logicalTable) {
        return logicalTable.hasProperty(R2RMLSyntax.tableName);
    }

    /**
     * Returns the sql query property in the given logical table resource
     * that is a r2rml view subclass.
     *
     * @param logicalTable the resource representing a logical table
     * @return string of the sql query property
     */
    protected String getSqlQuery(Resource logicalTable) {
        return getPropertyResourceValue(logicalTable, R2RMLSyntax.sqlQuery)
                .asLiteral()
                .getString();
    }

    /**
     * Returns the sql version property in the given logical table resource
     * that is a r2rml view subclass.
     *
     * @param logicalTable the resource representing a logical table
     * @return string of the sql version property
     */
    protected String getVersion(Resource logicalTable) {
        return getPropertyResourceValue(logicalTable, R2RMLSyntax.sqlVersion)
                .asLiteral()
                .getString();
    }

    /**
     * Returns the table name property in the given logical table resource
     * that is a base table or view subclass.
     *
     * @param logicalTable the resource representing a logical table
     * @return string of the table name property
     */
    protected String getTableName(Resource logicalTable) {
        return getPropertyResourceValue(logicalTable, R2RMLSyntax.tableName)
                .asLiteral()
                .getString();
    }

    //SubjectMap parsing

    /**
     * Returns the subject map property in the given triples map resource.
     *
     * @param triplesMap the resource representing a triples map
     * @return resource of the subject map property
     */
    protected Statement getSubjectMap(Resource triplesMap) {
        return getTermMap(triplesMap, R2RMLSyntax.subjectMap, R2RMLSyntax.subject);
    }

    /**
     * Returns a list of entity classes represented as resources from
     * the model.
     *
     * @param subjectMap the resource representing a subject map
     * @return list of entity classes as resources
     */
    protected List<Resource> getEntityClasses(Resource subjectMap) {
        return r2rmlGraph.listObjectsOfProperty(subjectMap, R2RMLSyntax.rrClass)
                .filterKeep(RDFNode::isURIResource)
                .mapWith(RDFNode::asResource)
                .toList();
    }

    //PredicateObjectMap

    /**
     * Returns a list of predicate object maps represented as resources
     * from the model.
     *
     * @param triplesMap the resource representing a triples map
     * @return list of predicate object maps as resources
     */
    protected List<Resource> getPredicateObjectMaps(Resource triplesMap) {
        return r2rmlGraph.listObjectsOfProperty(triplesMap, R2RMLSyntax.predicateObjectMap)
                .filterKeep(RDFNode::isAnon)
                .mapWith(RDFNode::asResource)
                .toList();
    }

    //PredicateMap parsing

    /**
     * Return the predicate map property in the given predicate object
     * map resource.
     *
     * @param predicateObjectMap the resource representing a predicate
     *                           object map
     * @return statement containing the predicate map property
     */
    protected Statement getPredicateMap(Resource predicateObjectMap) {
        return getTermMap(predicateObjectMap, R2RMLSyntax.predicateMap, R2RMLSyntax.predicate);
    }

    //ObjectMap parsing

    /**
     * Return the object map property in the given predicate object map
     * resource.
     *
     * @param resource the resource representing a predicate object map
     * @return statement containing the object map property
     */
    protected Statement getObjectMap(Resource resource) {
        return getTermMap(resource, R2RMLSyntax.objectMap, R2RMLSyntax.object);
    }

    //Constant TermMap related parsing

    /**
     * Returns true if given term map is a constant valued term map by
     * constant shortcut reference or as a constant property.
     *
     * @param termMap the statement containing a term map
     * @return true if the statement is a constant valued otherwise false
     */
    protected boolean isConstant(Statement termMap) {
        return (isPropertyConstant(termMap.getResource()) || isShortcutConstant(termMap));
    }

    /**
     * Returns true if given term map has a constant property meaning that
     * it is a constant valued term map.
     *
     * @param termMap the resource represent a term map
     * @return true if the resource contains a constant property
     *              otherwise false
     */
    private boolean isPropertyConstant(Resource termMap) {
        return termMap.hasProperty(R2RMLSyntax.constant);
    }

    /**
     * Returns true if given term map is a shortcut term meaning that
     * it is a constant valued term map.
     *
     * @param termMap the statement containing a term map
     * @return true if the term map property is a constant shortcut
     *              term otherwise false
     */
    private boolean isShortcutConstant(Statement termMap) {
        return R2RMLSyntax.getConstantShortcuts()
                .contains(termMap.getPredicate());
    }

    /**
     * Returns the constant property in the given term map statement.
     *
     * @param termMap the statement containing a term map
     * @return node of the constant property
     */
    protected RDFNode getConstantValue(Statement termMap) {
        if (isShortcutConstant(termMap)) {
            return termMap.getObject();
        }

        Resource termMapRes = termMap.getResource();
        return getPropertyResourceValue(termMapRes, R2RMLSyntax.constant);
    }

    //Template TermMap related parsing

    /**
     * Returns true if given term map is a template valued term map.
     *
     * @param termMap the resource representing a term map
     * @return true if the resource is a template valued term map
     *              otherwise false
     */
    protected boolean isTemplate(Resource termMap) {
        return termMap.hasProperty(R2RMLSyntax.template);
    }

    /**
     * Returns the value of the template property in the given template
     * valued term map.
     *
     * @param termMap the resource representing a term map
     * @return string of the template property
     */
    protected String getTemplateValue(Resource termMap) {
        return getPropertyResourceValue(termMap, R2RMLSyntax.template)
                .asLiteral()
                .getString();
    }

    //Column TermMap related parsing

    /**
     * Returns true if given term map is a column valued term map.
     *
     * @param termMap the resource representing a term map
     * @return true if the resource is a column valued term map
     *              otherwise false
     */
    protected boolean isColumn(Resource termMap) {
        return termMap.hasProperty(R2RMLSyntax.column);
    }

    /**
     * Returns the value of the column property in the given column
     * valued term map.
     *
     * @param termMap the resource representing a term map
     * @return string of the column property
     */
    protected Literal getColumnName(Resource termMap) {
        return getPropertyResourceValue(termMap, R2RMLSyntax.column)
                .asLiteral();
    }

    //Helper methods 2.0

    /**
     * Returns the node of the given property of a resource otherwise throws an
     * {@code ParserException} if the property does not exist in the given resource.
     *
     * @param resource the resource representing a r2rml term
     * @param property the property to search the resource for
     * @return node object of the property if found in resource
     * @throws ParserException if no property exist in given resource
     */
    private RDFNode getPropertyResourceValue(Resource resource, Property property) {
        Statement result = r2rmlGraph.getProperty(resource, property);
        if (result == null) {
            throw new ParserException("%s property not found in %s.", property, resource);
        }

        return result.getObject();
    }

    /**
     * Returns the statement containing the reference to the term map object otherwise
     * throws an {@code ParserException} if either given term map properties is not found
     * in the given resource.
     *
     * @param resource the resource containing a term map
     * @param termMap the term map property to search with
     * @param constTermMap the shortcut term of term map to search with
     * @return statement containing the term map if found
     * @throws ParserException if no term map is found in given resource
     */
    private Statement getTermMap(Resource resource, Property termMap, Property constTermMap) {
        Statement result = r2rmlGraph.getProperty(resource, termMap);
        if (result == null) {
            result = r2rmlGraph.getProperty(resource, constTermMap);
        }

        if (result == null) {
            throw new ParserException("%s or %s term map not found in %s.", termMap, constTermMap, resource);
        }

        return result;
    }
}
