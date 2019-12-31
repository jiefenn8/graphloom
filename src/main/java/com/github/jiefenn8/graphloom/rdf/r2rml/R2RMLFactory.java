/*
 *    Copyright (c) 2019 - GraphLoom contributors (github.com/jiefenn8/graphloom)
 *    This software is made available under the terms of Apache License, Version 2.0.
 */

package com.github.jiefenn8.graphloom.rdf.r2rml;

import com.github.jiefenn8.graphloom.rdf.r2rml.TermMap.TermType;
import org.apache.jena.rdf.model.RDFNode;

/**
 * This class defines the base methods that manages the setup and creation of
 * R2RML related classes.
 */
public class R2RMLFactory {

    //LogicalTable

    /**
     * Constructs a LogicalTable with a base table/view with the specified
     * payload.
     *
     * @param source the payload that this logical table represents
     * @return instance of logical table created with a source config
     */
    public static LogicalTable createLogicalBaseTableOrView(String source) {
        return new LogicalTable.Builder(new BaseTableOrView(source)).build();
    }

    /**
     * Constructs a LogicalTable with a R2RML view with the specified payload
     * and SQL version property.
     *
     * @param source  the payload that this logical table represents
     * @param version the SQL version that this payload supports
     * @return instance of logical table created with a source config
     */
    public static LogicalTable createLogicalR2RMLView(String source, String version) {
        R2RMLView r2rmlView = R2RMLFactory.createR2RMLView(source, version);
        return new LogicalTable.Builder(r2rmlView).build();
    }

    /**
     * Constructs a R2RMLView with the specified payload and SQL version that
     * this payload supports.
     *
     * @param source  the payload that this logical table represents
     * @param version the SQL version that this payload supports
     * @return instance of R2RML view with the given parameters
     */
    protected static R2RMLView createR2RMLView(String source, String version) {
        R2RMLView r2rmlView = new R2RMLView(source);
        r2rmlView.setProperty("sqlVersion", version);
        return r2rmlView;
    }

    //PredicateMap

    /**
     * Constructs a PredicateMap with the specified term map that is either
     * a constant, template or a column type.
     *
     * @param termMap the term map to use for this map config
     * @return instance of predicate map with a given term map
     */
    public static PredicateMap createPredicateMap(TermMap termMap) {
        return new PredicateMap(termMap);
    }

    //SubjectMap

    /**
     * Constructs a SubjectMap with the specified term map that is either
     * a constant, template or a column type.
     *
     * @param termMap the term map to use for this map config
     * @return instance of subject map with a given term map
     */
    public static SubjectMap createSubjectMap(TermMap termMap) {
        return new SubjectMap(termMap);
    }

    //ObjectMap

    /**
     * Constructs an ObjectMap with the specified term map that is either
     * a constant, template or a column type.
     *
     * @param termMap the term map to use for this map config
     * @return instance of object map with a given term map
     */
    public static ObjectMap createObjectMap(TermMap termMap) {
        return new ObjectMap(termMap);
    }

    //TermMap

    /**
     * Constructs a ConstTermMap with the specified rdf constant term to
     * use for rdf term generation.
     *
     * @param constant the term to use as constant term
     * @return instance of constant term map with given constant
     */
    public static ConstTermMap createConstantTermMap(RDFNode constant) {
        return new ConstTermMap(constant);
    }

    /**
     * Constructs a TemplateTermMap with the specified template string and
     * term type for rdf term generation.
     *
     * @param template the template string to generate term from
     * @param termType the term type to return the generated term as
     * @return instance of template term map with the given parameters
     */
    public static TemplateTermMap createTemplateTermMap(String template, TermType termType) {
        return new TemplateTermMap(template, termType);
    }

    /**
     * Constructs a ColumnTermMap with specified column name and term type
     * for rdf term generation.
     *
     * @param column   the column name to find the term value
     * @param termType the term type to return the generated term as
     * @return instance of column term map with the given parameters
     */
    public static ColumnTermMap createColumnTermMap(String column, TermType termType) {
        return new ColumnTermMap(column, termType);
    }
}
