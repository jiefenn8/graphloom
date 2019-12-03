/*
 *    Copyright (c) 2019 - GraphLoom contributors (github.com/jiefenn8/graphloom)
 *    This software is made available under the terms of Apache License, Version 2.0.
 */

package com.github.jiefenn8.graphloom.rdf.r2rml;

import com.github.jiefenn8.graphloom.rdf.r2rml.TermMap.TermType;
import org.apache.jena.rdf.model.RDFNode;

public class R2RMLFactory {

    //LogicalTable
    public static LogicalTable createBaseTableOrView(String source) {
        R2RMLView r2rmlView = new R2RMLView(source);

        return new LogicalTable(r2rmlView);
    }

    public static LogicalTable createR2RMLView(String source, String version) {
        BaseTableOrView baseTableOrView = new BaseTableOrView(source);
        baseTableOrView.setProperty("sqlVersion", version);

        return new LogicalTable(baseTableOrView);
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
