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
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * This class defines the base methods that manages the parsing
 * of rdf mapping document to iterable r2rml terms.
 */
public class R2RMLParser {

    private static final Logger logger = LogManager.getLogger();

    private final FileManager fileManager;
    private String filenameOrUri = StringUtils.EMPTY;
    private Model r2rmlGraph = ModelFactory.createDefaultModel();

    /**
     * Constructs a R2RMLParser with default file manager
     * instance.
     */
    protected R2RMLParser() {
        fileManager = FileManager.get();
    }

    /**
     * Constructs a R2RMLParser with the specified file manager
     * instance.
     *
     * @param fileManager the manager to handle the file
     */
    protected R2RMLParser(FileManager fileManager) {
        this.fileManager = checkNotNull(fileManager);
    }

    /**
     * Loads a given r2rml document into a model. Returns true if
     * parsing of document is successful and this parser does not
     * have another document opened. To close the opened document
     * on this parser, refer to the {@link R2RMLParser#close()}
     * method.
     *
     * @param filenameOrUri the filename or URI of the document
     * @param baseUri       the base URI to use for the mapping
     * @return true if the document is loaded successfully
     *         otherwise false
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
     * Close the model of the loaded r2rml document on this
     * parser. Returns true if the model is closed.
     *
     * @return true of the model is closed otherwise false
     */
    protected boolean close() {
        r2rmlGraph.close();
        logger.info("Closing current R2RML file {}.", filenameOrUri);
        return r2rmlGraph.isClosed();
    }

    /**
     * Returns the map of namespace and their prefix from the
     * model.
     *
     * @return map containing prefixes and their namespace
     */
    protected Map<String, String> getNsPrefixMap() {
        return r2rmlGraph.getNsPrefixMap();
    }

    //TriplesMap parsing

    /**
     * Returns a set of triples map represented as resource
     * from the model.
     *
     * @return set of triples map as resources
     */
    protected Set<Resource> getTriplesMaps() {
        return r2rmlGraph.listResourcesWithProperty(R2RMLSyntax.logicalTable)
                .filterKeep(this::hasSubjectMap)
                .toSet();
    }

    /**
     * Returns the id name that uniquely identify a triples map.
     *
     * @param subject the triples map to get id
     * @return the name of the triples map
     */
    protected String getTriplesMapIdName(Resource subject) {
        return subject.getLocalName();
    }

    /**
     * Returns true if given triples map object in statement has
     * a subject map property.
     *
     * @param subject the statement containing the triples map
     * @return true if the statement contains subject map property
     */
    private boolean hasSubjectMap(Resource subject) {
        return (r2rmlGraph.contains(subject, R2RMLSyntax.subjectMap)
                || r2rmlGraph.contains(subject, R2RMLSyntax.subject));
    }

    //LogicalTable parsing

    /**
     * Returns the logical table property in the given triples
     * map resource as a resource./
     *
     * @param subject the resource representing a triples map
     * @return resource of the logical table property
     */
    protected Resource getLogicalTable(Resource subject) {
        return getPropertyResourceValue(subject, R2RMLSyntax.logicalTable)
                .asResource();
    }

    /**
     * Returns true if given logical table resource is a r2rml
     * view subclass.
     *
     * @param subject the resource representing a logical table
     * @return true if the resource is a r2rml view
     */
    protected boolean isR2RMLView(Resource subject) {
        return (r2rmlGraph.contains(subject, R2RMLSyntax.sqlQuery)
                && r2rmlGraph.contains(subject, R2RMLSyntax.sqlVersion));
    }

    /**
     * Returns true if given logical table is a base table or
     * view subclass.
     *
     * @param subject the resource representing a logical table
     * @return true if the resource is a base table or view
     */
    protected boolean isBaseTableOrView(Resource subject) {
        return r2rmlGraph.contains(subject, R2RMLSyntax.tableName);
    }

    /**
     * Returns the sql query property in the given logical table
     * resource that is a r2rml view subclass.
     *
     * @param subject the resource representing a logical table
     * @return string of the sql query property
     */
    protected String getSqlQuery(Resource subject) {
        return getPropertyResourceValue(subject, R2RMLSyntax.sqlQuery)
                .asLiteral()
                .getString();
    }

    /**
     * Returns the sql version property in the given logical table
     * resource that is a r2rml view subclass.
     *
     * @param subject the resource representing a logical table
     * @return string of the sql version property
     */
    protected String getVersion(Resource subject) {
        return getPropertyResourceValue(subject, R2RMLSyntax.sqlVersion)
                .asResource()
                .getLocalName();
    }

    /**
     * Returns the table name property in the given logical table
     * resource that is a base table or view subclass.
     *
     * @param subject the resource representing a
     *                logical table
     * @return string of the table name property
     */
    protected String getTableName(Resource subject) {
        return getPropertyResourceValue(subject, R2RMLSyntax.tableName)
                .asLiteral()
                .getString();
    }

    //SubjectMap parsing

    /**
     * Returns the subject map property in the given triples map
     * resource.
     *
     * @param subject the resource representing a triples map
     * @return resource of the subject map property
     */
    protected Statement getSubjectMap(Resource subject) {
        return getTermMap(subject, R2RMLSyntax.subjectMap, R2RMLSyntax.subject);
    }

    /**
     * Returns a list of entity classes represented as resources
     * from the model.
     *
     * @param subject the resource representing a subject map
     * @return list of entity classes as resources
     */
    protected List<Resource> getEntityClasses(Resource subject) {
        return r2rmlGraph.listObjectsOfProperty(subject, R2RMLSyntax.rrClass)
                .filterKeep(RDFNode::isURIResource)
                .mapWith(RDFNode::asResource)
                .toList();
    }

    //PredicateObjectMap

    /**
     * Returns a list of predicate object maps represented as
     * statement from the model.
     *
     * @param subject the statement containing the predicate
     *                object map
     * @return list of predicate object maps as resources
     */
    protected List<Statement> listPredicateObjectMaps(Resource subject) {
        return r2rmlGraph.listStatements(subject, R2RMLSyntax.predicateObjectMap, (RDFNode) null)
                .filterKeep(this::containsAnon)
                .toList();
    }

    /**
     * Returns true if given statement contains an object that
     * is an anon/blank type object.
     *
     * @param triple the statement to check for anon object
     * @return true if statement contains object that is an anon
     */
    private boolean containsAnon(Statement triple) {
        return triple.getObject().isAnon();
    }

    //PredicateMap parsing

    /**
     * Return the predicate map property in the given predicate
     * object map resource.
     *
     * @param subject the resource representing a
     *                predicate object map
     * @return statement containing the predicate map property
     */
    protected Statement getPredicateMap(Resource subject) {
        return getTermMap(subject, R2RMLSyntax.predicateMap, R2RMLSyntax.predicate);
    }

    //ObjectMap parsing

    /**
     * Return the object map property in the given predicate
     * object map resource.
     *
     * @param subject the resource representing a predicate
     *                object map
     * @return statement containing the object map property
     */
    protected Statement getObjectMap(Resource subject) {
        return getTermMap(subject, R2RMLSyntax.objectMap, R2RMLSyntax.object);
    }

    //Constant TermMap related parsing

    /**
     * Returns true if given term map is a constant valued term
     * map by constant shortcut reference or as a constant property.
     *
     * @param triple the statement containing a term map
     * @return true if the statement is a constant valued
     *         otherwise false
     */
    protected boolean isConstant(Statement triple) {
        return (isPropertyConstant(triple.getResource()) || isShortcutConstant(triple));
    }

    /**
     * Returns true if given term map has a constant property
     * meaning that it is a constant valued term map.
     *
     * @param subject the resource representing a term map
     * @return true if the resource contains a constant property
     *         otherwise false
     */
    private boolean isPropertyConstant(Resource subject) {
        return r2rmlGraph.contains(subject, R2RMLSyntax.constant);
    }

    /**
     * Returns true if given term map is a shortcut term meaning
     * that it is a constant valued term map.
     *
     * @param triple the statement containing a term map
     * @return true if the term map property is a constant
     *         shortcut term otherwise false
     */
    private boolean isShortcutConstant(Statement triple) {
        return R2RMLSyntax.getConstantShortcuts()
                .contains(triple.getPredicate());
    }

    /**
     * Returns the constant property in the given term map
     * statement.
     *
     * @param triple the statement containing a term map
     * @return node of the constant property
     */
    protected RDFNode getConstantValue(Statement triple) {
        if (isShortcutConstant(triple)) {
            return triple.getObject();
        }

        Resource subject = triple.getResource();
        return getPropertyResourceValue(subject, R2RMLSyntax.constant);
    }

    //Template TermMap related parsing

    /**
     * Returns true if given term map is a template valued term
     * map.
     *
     * @param subject the resource representing a term map
     * @return true if the resource is a template valued term
     *         map otherwise false
     */
    protected boolean isTemplate(Resource subject) {
        return r2rmlGraph.contains(subject, R2RMLSyntax.template);
    }

    /**
     * Returns the value of the template property in the given
     * template valued term map.
     *
     * @param subject the resource representing a term map
     * @return string of the template property
     */
    protected String getTemplateValue(Resource subject) {
        return getPropertyResourceValue(subject, R2RMLSyntax.template)
                .asLiteral()
                .getString();
    }

    //Column TermMap related parsing

    /**
     * Returns true if given term map is a column valued term map.
     *
     * @param termMap the resource representing a term map
     * @return true if the resource is a column valued term map
     *         otherwise false
     */
    protected boolean isColumn(Resource termMap) {
        return r2rmlGraph.contains(termMap, R2RMLSyntax.column);
    }

    /**
     * Returns the value of the column property in the given
     * column valued term map.
     *
     * @param subject the resource representing a term map
     * @return string of the column property
     */
    protected Literal getColumnName(Resource subject) {
        return getPropertyResourceValue(subject, R2RMLSyntax.column)
                .asLiteral();
    }

    //Helper methods 2.0

    /**
     * Returns the node of the given property of a resource
     * otherwise throws an {@code ParserException} if the
     * property does not exist in the given resource.
     *
     * @param subject   the resource representing a r2rml term
     * @param predicate the property to search the resource for
     * @return node object of the property if found in resource
     * @throws ParserException if no property exist in resource
     */
    private RDFNode getPropertyResourceValue(Resource subject, Property predicate) {
        Statement result = r2rmlGraph.getProperty(subject, predicate);
        if (result == null) {
            String message = "%s property not found in %s.";
            throw new ParserException(message, predicate, subject);
        }

        return result.getObject();
    }

    /**
     * Returns the statement containing the reference to the term
     * map object otherwise throws an {@code ParserException} if
     * either given term map properties is not found in the given
     * resource.
     *
     * @param subject   the resource containing a term map
     * @param term      the term map property to search with
     * @param constTerm the shortcut term of term map to search
     * @return statement containing the term map if found
     * @throws ParserException if no term map is found in resource
     */
    private Statement getTermMap(Resource subject, Property term, Property constTerm) {
        Statement result = r2rmlGraph.getProperty(subject, term);
        if (result == null) {
            result = r2rmlGraph.getProperty(subject, constTerm);
        }

        if (result == null) {
            String message = "%s or %s term map not found in %s.";
            throw new ParserException(message, term, constTerm, subject);
        }

        return result;
    }

    /**
     * Returns true if the given object map is a reference object
     * map.
     *
     * @param subject the resource representing the object map
     * @return true if object map is a reference object map
     *         otherwise false
     */
    public boolean isRefObjectMap(Resource subject) {
        return r2rmlGraph.contains(subject, R2RMLSyntax.parentTriplesMap);
    }

    /**
     * Returns parent triples map property in the given object map
     * resources.
     *
     * @param subject the resource representing an object map
     * @return resource of the parent triples map property
     */
    public Resource getParentTriplesMap(Resource subject) {
        return getPropertyResourceValue(subject, R2RMLSyntax.parentTriplesMap)
                .asResource();
    }

    /**
     * Returns true if given object map has join condition property.
     *
     * @param subject the resource representing an object map
     * @return return if resource has join condition property
     */
    public boolean hasJoinCondition(Resource subject) {
        return r2rmlGraph.contains(subject, R2RMLSyntax.joinCondition);
    }

    /**
     * Returns join condition property in the given object map.
     *
     * @param subject the resource representing an object map
     * @return resource of the object map property
     */
    public Resource getJoinCondition(Resource subject) {
        return getPropertyResourceValue(subject, R2RMLSyntax.joinCondition)
                .asResource();
    }

    /**
     * Returns child query property in the given join condition.
     *
     * @param subject the resource representing the join condition
     * @return string of the child query property
     */
    public String getChildQuery(Resource subject) {
        return getPropertyResourceValue(subject, R2RMLSyntax.child)
                .asLiteral()
                .getString();
    }

    /**
     * Returns parent query property in the given join condition.
     *
     * @param subject the resource representing the join condition
     * @return string of the parent query property
     */
    public String getParentQuery(Resource subject) {
        return getPropertyResourceValue(subject, R2RMLSyntax.parent)
                .asLiteral()
                .getString();
    }
}
