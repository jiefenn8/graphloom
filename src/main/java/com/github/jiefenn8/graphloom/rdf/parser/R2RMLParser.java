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

import java.util.Map;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * This class defines the base methods that manages the parsing
 * of rdf mapping document to iterable r2rml terms.
 * <p>
 * All methods in this parser will likely to cross check with
 * this instance r2rml model in most cases to complete its call;
 * If a given resource (subject/object) or statement (triple)
 * origin is from another source and not from this instance,
 * unintended side effect is possible.
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
     * Returns the map of namespace and their prefix defined
     * in the model.
     *
     * @return map containing prefixes and their namespace
     */
    protected Map<String, String> getNsPrefixMap() {
        return r2rmlGraph.getNsPrefixMap();
    }

    //TriplesMap parsing

    /**
     * Returns a set of resources (subjects) that contains all
     * valid triples maps defined in the the model. A triples
     * map must have a logical table and a subject map property
     * to be considered valid.
     *
     * @return set of all valid triples map as resources
     */
    protected Set<Resource> getTriplesMaps() {
        return r2rmlGraph.listResourcesWithProperty(R2RMLSyntax.logicalTable)
                .filterKeep(this::hasSubjectMap)
                .toSet();
    }

    /**
     * Returns the id name of the given resource (subject).
     *
     * @param subject the triples map resource
     * @return the id name of the triples map
     */
    protected String getTriplesMapIdName(Resource subject) {
        return subject.getLocalName();
    }

    /**
     * Returns true if given triples map resource (subject) in
     * has either a subject map or a shortcut constant subject
     * map property (predicate).
     *
     * @param subject the triples map resource
     * @return true if the resource contains a subject map
     */
    private boolean hasSubjectMap(Resource subject) {
        return (r2rmlGraph.contains(subject, R2RMLSyntax.subjectMap)
                || r2rmlGraph.contains(subject, R2RMLSyntax.subject));
    }

    //LogicalTable parsing

    /**
     * Returns the resource (object) associated to the logical
     * table property (predicate) from the given triples map
     * resource (subject).
     *
     * @param subject the triples map resource
     * @return resource of the logical table property
     */
    protected Resource getLogicalTable(Resource subject) {
        return getPropertyResourceValue(subject, R2RMLSyntax.logicalTable)
                .asResource();
    }

    /**
     * Returns true if the given logical table resource (subject)
     * is a r2rml view subclass.
     *
     * @param subject the logical table resource
     * @return true if the resource is a r2rml view
     */
    protected boolean isR2RMLView(Resource subject) {
        return (r2rmlGraph.contains(subject, R2RMLSyntax.sqlQuery)
                && r2rmlGraph.contains(subject, R2RMLSyntax.sqlVersion));
    }

    /**
     * Returns true if the given logical table resource (subject)
     * is a base table or view subclass.
     *
     * @param subject the logical table resource
     * @return true if the resource is a base table or view
     */
    protected boolean isBaseTableOrView(Resource subject) {
        return r2rmlGraph.contains(subject, R2RMLSyntax.tableName);
    }

    /**
     * Returns the Literal (object) value of the sql query
     * property (predicate) in the given logical table resource
     * (subject) that is a r2rml view subclass.
     *
     * @param subject the logical table resource
     * @return string of the sql query property
     */
    protected String getSqlQuery(Resource subject) {
        return getPropertyResourceValue(subject, R2RMLSyntax.sqlQuery)
                .asLiteral()
                .getString();
    }

    /**
     * Returns the Literal (object) value of the sql version
     * property in the given logical table resource (subject)
     * that is a r2rml view subclass.
     *
     * @param subject the logical table resource
     * @return string of the sql version property
     */
    protected String getVersion(Resource subject) {
        return getPropertyResourceValue(subject, R2RMLSyntax.sqlVersion)
                .asResource()
                .getLocalName();
    }

    /**
     * Returns the Literal (object) value of the table name
     * property (predicate) in the given logical table
     * resource (subject) that is a base table or view sub-class.
     *
     * @param subject the logical table resource
     * @return string of the table name property
     */
    protected String getTableName(Resource subject) {
        return getPropertyResourceValue(subject, R2RMLSyntax.tableName)
                .asLiteral()
                .getString();
    }

    //SubjectMap parsing

    /**
     * Returns the statement (triple) containing the given
     * triples map resource (subject) and the resource (object)
     * associated to the subject map property (predicate).
     *
     * @param subject the triples map resource
     * @return resource of the subject map property
     */
    protected Statement getSubjectMap(Resource subject) {
        return getTermMap(subject, R2RMLSyntax.subjectMap, R2RMLSyntax.subject);
    }

    /**
     * Returns a set of entity classes resources (objects)
     * associated with the class property (predicate) of the
     * given subject map resource (subject).
     *
     * @param subject the subject map resource
     * @return set of entity classes resources
     */
    protected Set<Resource> listEntityClasses(Resource subject) {
        return r2rmlGraph.listObjectsOfProperty(subject, R2RMLSyntax.rrClass)
                .filterKeep(RDFNode::isURIResource)
                .mapWith(RDFNode::asResource)
                .toSet();
    }

    //PredicateObjectMap

    /**
     * Returns a set of statements (triples) containing the given
     * triples map resource (subject) and the resource (object)
     * associated to the predicate object map property (predicate).
     *
     * @param subject the triples map resource
     * @return set of statement with resource associated to
     *         predicate object map property
     */
    protected Set<Statement> listPredicateObjectMaps(Resource subject) {
        return r2rmlGraph.listStatements(subject, R2RMLSyntax.predicateObjectMap, (RDFNode) null)
                .filterKeep(this::containsAnon)
                .toSet();
    }

    /**
     * Returns true if given statement (triple) contains an
     * object that is an anon/blank type object.
     *
     * @param triple the statement to check for anon object
     * @return true if statement contains an anon object
     */
    private boolean containsAnon(Statement triple) {
        return triple.getObject().isAnon();
    }

    //PredicateMap parsing

    /**
     * Return the statement (triple) containing the given
     * predicate map resource (subject) and the resource (object)
     * associated to the predicate map property (predicate).
     *
     * @param subject the predicate object map resource
     * @return statement with resource associated to the predicate
     *         map property
     */
    protected Statement getPredicateMap(Resource subject) {
        return getTermMap(subject, R2RMLSyntax.predicateMap, R2RMLSyntax.predicate);
    }

    //ObjectMap parsing

    /**
     * Return the statement (triple) containing the given
     * predicate object map resource (subject) and the resource
     * (object) associated to the object map property (predicate).
     *
     * @param subject the predicate object map resource
     * @return statement with resource associated to the object
     *         map property
     */
    protected Statement getObjectMap(Resource subject) {
        return getTermMap(subject, R2RMLSyntax.objectMap, R2RMLSyntax.object);
    }

    //Constant TermMap related parsing

    /**
     * Returns true if given term map is a constant valued term
     * map by constant shortcut reference or as a constant
     * property (predicate).
     *
     * @param triple the term map statement
     * @return true if the statement is a constant term map
     */
    protected boolean isConstant(Statement triple) {
        return (isPropertyConstant(triple.getResource()) || isShortcutConstant(triple));
    }

    /**
     * Returns true if given term map has a constant property
     * (predicate) meaning that it is a constant valued term map.
     *
     * @param subject the term map resource
     * @return true if the resource contains a constant property
     */
    private boolean isPropertyConstant(Resource subject) {
        return r2rmlGraph.contains(subject, R2RMLSyntax.constant);
    }

    /**
     * Returns true if given term map is a shortcut term property
     * (predicate) meaning that it is a constant valued term map.
     *
     * @param triple the term map statement
     * @return true if the statement is a constant shortcut term
     */
    private boolean isShortcutConstant(Statement triple) {
        return R2RMLSyntax.getConstantShortcuts()
                .contains(triple.getPredicate());
    }

    /**
     * Returns the node (object) associated with the constant
     * property (predicate) in the given term map statement
     * (triple).
     *
     * @param triple the term map statement
     * @return node associated with the constant property
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
     * Returns true if given term map resource (subject) is a
     * template valued term map.
     *
     * @param subject the term map resource
     * @return true if the resource is a template term map
     */
    protected boolean isTemplate(Resource subject) {
        return r2rmlGraph.contains(subject, R2RMLSyntax.template);
    }

    /**
     * Returns the Literal (object) value of the template property
     * (predicate) in the given template valued term map resource
     * (subject).
     *
     * @param subject the term map resource
     * @return string associated with the template property
     */
    protected String getTemplateValue(Resource subject) {
        return getPropertyResourceValue(subject, R2RMLSyntax.template)
                .asLiteral()
                .getString();
    }

    //Column TermMap related parsing

    /**
     * Returns true if given term map resource (subject) is a
     * column valued term map.
     *
     * @param termMap the term map resource
     * @return true if the resource is a column term map
     */
    protected boolean isColumn(Resource termMap) {
        return r2rmlGraph.contains(termMap, R2RMLSyntax.column);
    }

    /**
     * Returns the Literal (object) value of the column property
     * (predicate) in the given column valued term map.
     *
     * @param subject the resource representing a term map
     * @return string associated with the column property
     */
    protected String getColumnName(Resource subject) {
        return getPropertyResourceValue(subject, R2RMLSyntax.column)
                .asLiteral()
                .getString();
    }

    //Helper methods 2.0

    /**
     * Returns the node (object) of the given property (predicate)
     * of a resource (subject) otherwise throws an
     * {@code ParserException} if the property does not exist
     * in the given resource.
     *
     * @param subject   the r2rml term resource
     * @param predicate the property to search the resource for
     * @return node associated with the property if found
     * @throws ParserException if no property and value exist
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
     * Returns the statement (triple) containing the reference
     * to the term map object otherwise throws an
     * {@code ParserException} if neither of the given term map
     * properties is found in the given resource.
     *
     * @param subject   the term map resource
     * @param term      the term map property to search with
     * @param constTerm the shortcut term to search with
     * @return statement containing the term map if found
     * @throws ParserException if no term map is found
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
     * Returns true if the given object map resource (subject) is
     * a reference object map.
     *
     * @param subject the object map resource
     * @return true if object map is a reference object map
     */
    public boolean isRefObjectMap(Resource subject) {
        return r2rmlGraph.contains(subject, R2RMLSyntax.parentTriplesMap);
    }

    /**
     * Returns the resource (object) associated to the parent
     * triples map property (predicate) in the given object
     * map resource (subject).
     *
     * @param subject the object map resource
     * @return resource of the parent triples map property
     */
    public Resource getParentTriplesMap(Resource subject) {
        return getPropertyResourceValue(subject, R2RMLSyntax.parentTriplesMap)
                .asResource();
    }

    /**
     * Returns true if given object map resource (subject) has a
     * join condition property (predicate).
     *
     * @param subject the resource representing an object map
     * @return return if resource has join condition property
     */
    public boolean hasJoinCondition(Resource subject) {
        return r2rmlGraph.contains(subject, R2RMLSyntax.joinCondition);
    }

    /**
     * Returns a set of resources (object) associated to the join
     * condition property (predicate) in the given object map
     * resource (subject).
     *
     * @param subject the object map resource
     * @return set of resources associated with join condition
     *         property
     */
    public Set<Resource> listJoinConditions(Resource subject) {
        return r2rmlGraph.listObjectsOfProperty(subject, R2RMLSyntax.joinCondition)
                .mapWith(RDFNode::asResource)
                .toSet();
    }

    /**
     * Returns the Literal (object) value of the child query
     * property (predicate) in the given join condition resource
     * (subject).
     *
     * @param subject the join condition resource
     * @return string of the child query property
     */
    public String getChildQuery(Resource subject) {
        return getPropertyResourceValue(subject, R2RMLSyntax.child)
                .asLiteral()
                .getString();
    }

    /**
     * Returns the Literal (object) value of the parent query
     * property (predicate) in the given join condition resource
     * (subject).
     *
     * @param subject the join condition resource
     * @return string of the parent query property
     */
    public String getParentQuery(Resource subject) {
        return getPropertyResourceValue(subject, R2RMLSyntax.parent)
                .asLiteral()
                .getString();
    }
}
