/*
 *    Copyright (c) 2019 - GraphLoom contributors (github.com/jiefenn8/graphloom)
 *    This software is made available under the terms of Apache License, Version 2.0.
 */

package com.github.jiefenn8.graphloom.rdf.r2rml;

import com.google.common.collect.ImmutableList;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.ResourceFactory;

import java.util.List;

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
    private static final List<Property> CONSTANT_SHORTCUTS = ImmutableList.of(subject, predicate, object);

    public static String getURI() {
        return URI;
    }

    public static List<Property> getConstantShortcuts() {
        return CONSTANT_SHORTCUTS;
    }
}
