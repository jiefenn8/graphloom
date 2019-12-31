/*
 *    Copyright (c) 2019 - GraphLoom contributors (github.com/jiefenn8/graphloom)
 *    This software is made available under the terms of Apache License, Version 2.0.
 */

package com.github.jiefenn8.graphloom.rdf.r2rml;

import static com.github.jiefenn8.graphloom.api.SourceConfig.PayloadType;

/**
 * This enumeration defines the base database types possible for identifying a
 * {@link PayloadType} implementation.
 */
public enum DatabaseType implements PayloadType {

    /**
     * QUERY : A custom SQL query type; e.g "SELECT X, Y FROM Z;".
     * TABLE_NAME : A SQL table or view name; e.g. "TABLE_X", "VIEW_Y".
     */
    QUERY, TABLE_NAME
}
