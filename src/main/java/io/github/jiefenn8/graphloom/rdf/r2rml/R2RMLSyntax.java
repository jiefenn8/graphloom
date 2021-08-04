/*
 * Copyright (c) 2019 - GraphLoom contributors (github.com/jiefenn8/graphloom)
 * This software is made available under the terms of Apache License, Version 2.0.
 */

package io.github.jiefenn8.graphloom.rdf.r2rml;

import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.ResourceFactory;

import java.util.List;

/**
 * The class defines the base methods that manages the definition of R2RML
 * vocabularies and their groups. The base R2RML {@code URI} is hardcoded
 * to refer to the W3C R2RML namespace resource; Any matching must be refer
 * to this uri as well.
 */
public class R2RMLSyntax {

    private static final String URI = "http://www.w3.org/ns/r2rml#";
    public static final Property logicalTable = ResourceFactory.createProperty(URI, "logicalTable");
    public static final Property tableName = ResourceFactory.createProperty(URI, "tableName");
    public static final Property sqlQuery = ResourceFactory.createProperty(URI, "sqlQuery");
    public static final Property sqlVersion = ResourceFactory.createProperty(URI, "sqlVersion");
    public static final Property subjectMap = ResourceFactory.createProperty(URI, "subjectMap");
    public static final Property subject = ResourceFactory.createProperty(URI, "subject");
    public static final Property predicateObjectMap = ResourceFactory.createProperty(URI, "predicateObjectMap");
    public static final Property rrClass = ResourceFactory.createProperty(URI, "class");
    public static final Property constant = ResourceFactory.createProperty(URI, "constant");
    public static final Property template = ResourceFactory.createProperty(URI, "template");
    public static final Property column = ResourceFactory.createProperty(URI, "column");
    public static final Property predicateMap = ResourceFactory.createProperty(URI, "predicateMap");
    public static final Property predicate = ResourceFactory.createProperty(URI, "predicate");
    public static final Property objectMap = ResourceFactory.createProperty(URI, "objectMap");
    public static final Property object = ResourceFactory.createProperty(URI, "object");
    private static final List<Property> CONSTANT_SHORTCUTS = List.of(subject, predicate, object);
    public static final Property parentTriplesMap = ResourceFactory.createProperty(URI, "parentTriplesMap");
    public static final Property joinCondition = ResourceFactory.createProperty(URI, "joinCondition");
    public static final Property parent = ResourceFactory.createProperty(URI, "parent");
    public static final Property child = ResourceFactory.createProperty(URI, "child");

    /**
     * Returns the uri that is used for the definition of the R2RML
     * vocabulary properties.
     *
     * @return the uri used to create the vocabulary
     */
    public static String getURI() {
        return URI;
    }

    /**
     * Returns list of all R2RML vocabulary properties defined in this class.
     *
     * @return the list of R2RML properties
     */
    public static List<Property> getConstantShortcuts() {
        return CONSTANT_SHORTCUTS;
    }
}
