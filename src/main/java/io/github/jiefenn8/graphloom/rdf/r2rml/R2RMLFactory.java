/*
 * Copyright (c) 2019 - GraphLoom contributors (github.com/jiefenn8/graphloom)
 * This software is made available under the terms of Apache License, Version 2.0.
 */

package io.github.jiefenn8.graphloom.rdf.r2rml;

/**
 * This class defines the base methods that manages the setup and creation of
 * R2RML related classes.
 */
public class R2RMLFactory {

    /**
     * Constructs a LogicalTable with a base table/view with the specified
     * payload.
     *
     * @param source the payload that this logical table represents
     * @return instance of logical table created with a query config
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
     * @return instance of logical table created with a query config
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
}
