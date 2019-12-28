/*
 *    Copyright (c) 2019 - GraphLoom contributors (github.com/jiefenn8/graphloom)
 *    This software is made available under the terms of Apache License, Version 2.0.
 */

package com.github.jiefenn8.graphloom.rdf.r2rml;

import com.github.jiefenn8.graphloom.rdf.r2rml.TermMap.TermType;
import org.apache.jena.rdf.model.RDFNode;

/**
 * This class defines the base methods that manages the setup
 * and creation of R2RML related classes.
 */
public class R2RMLFactory {

    //LogicalTable

    public static LogicalTable createLogicalBaseTableOrView(String source) {
        return new LogicalTable.Builder(new BaseTableOrView(source)).build();
    }

    public static LogicalTable createLogicalR2RMLView(String source, String version) {
        R2RMLView r2rmlView = R2RMLFactory.createR2RMLView(source, version);
        return new LogicalTable.Builder(r2rmlView).build();
    }

    protected static R2RMLView createR2RMLView(String source, String version) {
        R2RMLView r2rmlView = new R2RMLView(source);
        r2rmlView.setProperty("sqlVersion", version);
        return r2rmlView;
    }

    //PredicateMap

    public static PredicateMap createPredicateMap(TermMap termMap) {
        return new PredicateMap(termMap);
    }

    //SubjectMap

    public static SubjectMap createSubjectMap(TermMap termMap) {
        return new SubjectMap(termMap);
    }

    //ObjectMap

    public static ObjectMap createObjectMap(TermMap termMap) {
        return new ObjectMap(termMap);
    }

    //TermMap

    public static ConstTermMap createConstantTermMap(RDFNode constant) {
        return new ConstTermMap(constant);
    }

    public static TmplTermMap createTemplateTermMap(String template, TermType termType) {
        return new TmplTermMap(template, termType);
    }

    public static ColTermMap createColumnTermMap(String column, TermType termType) {
        return new ColTermMap(column, termType);
    }
}
