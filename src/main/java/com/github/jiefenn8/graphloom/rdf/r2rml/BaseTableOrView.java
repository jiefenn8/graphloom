/*
 * Copyright (c) 2019 - GraphLoom contributors (github.com/jiefenn8/graphloom
 * This software is made available under the terms of Apache License, Version 2.0.
 */

package com.github.jiefenn8.graphloom.rdf.r2rml;

import com.github.jiefenn8.graphloom.api.BaseEntityReference;
import com.github.jiefenn8.graphloom.api.EntityReference;
import org.apache.commons.lang3.StringUtils;

/**
 * This class extends the base methods of the {@link BaseEntityReference} to manage
 * configuration to only handle SQL retrieval through the use of base table or
 * view  name.
 */
public class BaseTableOrView extends BaseEntityReference implements EntityReference {

    /**
     * Constructs a BaseTableOrView with the specified SQL table or view name
     * as the source payload.
     *
     * @param payload the table or view name
     */
    protected BaseTableOrView(String payload) {
        super(payload, DatabaseType.TABLE_NAME, StringUtils.EMPTY);
    }
}
