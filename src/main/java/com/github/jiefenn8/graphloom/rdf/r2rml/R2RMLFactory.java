/*
 *    Copyright (c) 2019 - GraphLoom contributors (github.com/jiefenn8/graphloom)
 *    This software is made available under the terms of Apache License, Version 2.0.
 */

package com.github.jiefenn8.graphloom.rdf.r2rml;

import com.github.jiefenn8.graphloom.rdf.r2rml.TermMap.TermType;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;

import java.util.Map;

public class R2RMLFactory {

    //R2RMLMap
    public static R2RMLMap createR2RMLMap(Map<String, String> nsMap) {
        return new R2RMLMap(nsMap);
    }

    //LogicalTable
    public static LogicalTable createLogicalTableBaseTableOrView(String source) {
        R2RMLView r2rmlView = new R2RMLView(source);

        return new LogicalTable(r2rmlView);
    }

    public static LogicalTable createLogicalTableR2RMLView(String source, String version) {
        BaseTableOrView baseTableOrView = new BaseTableOrView(source);
        baseTableOrView.setProperty("sqlVersion", version);

        return new LogicalTable(baseTableOrView);
    }

    //PredicateMap
    public static PredicateMap createConstPredicateMap(Property constant) {
        return createPredicateMap(new ConstTermMap(constant));
    }

    public static PredicateMap createTmplPredicateMap(String template) {
        return createTmplPredicateMap(template, TermType.IRI);
    }

    public static PredicateMap createTmplPredicateMap(String template, TermType termType) {
        return createPredicateMap(new TmplTermMap(template, termType));
    }

    private static PredicateMap createPredicateMap(TermMap termMap) {
        return new PredicateMap(termMap);
    }

    //SubjectMap
    public static SubjectMap createConstSubjectMap(RDFNode constant) {
        return createSubjectMap(new ConstTermMap(constant));
    }

    public static SubjectMap createTmplSubjectMap(String template) {
        return createTmplSubjectMap(template, TermType.IRI);
    }

    public static SubjectMap createTmplSubjectMap(String template, TermType termType) {
        return createSubjectMap(new TmplTermMap(template, termType));
    }

    public static SubjectMap createColSubjectMap(String column) {
        return createColSubjectMap(column, TermType.LITERAL);
    }

    public static SubjectMap createColSubjectMap(String column, TermType termType) {
        return createSubjectMap(new ColTermMap(column, termType));
    }

    private static SubjectMap createSubjectMap(TermMap termMap) {
        return new SubjectMap(termMap);
    }

    //ObjectMap
    public static ObjectMap createConstObjectMap(RDFNode constant) {
        return createObjectMap(new ConstTermMap(constant));
    }

    public static ObjectMap createTmplObjectMap(String template) {
        return createTmplObjectMap(template, TermType.IRI);
    }

    public static ObjectMap createTmplObjectMap(String template, TermType termType) {
        return createObjectMap(new TmplTermMap(template, termType));
    }

    public static ObjectMap createColObjectMap(String column) {
        return createColObjectMap(column, TermType.LITERAL);
    }

    public static ObjectMap createColObjectMap(String column, TermType termType) {
        return new ObjectMap(new ColTermMap(column, termType));
    }

    private static ObjectMap createObjectMap(TermMap termMap) {
        return new ObjectMap(termMap);
    }
}
