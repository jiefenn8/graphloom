/*
 * Copyright (c) 2019 - GraphLoom contributors (github.com/jiefenn8/graphloom)
 * This software is made available under the terms of Apache License, Version 2.0.
 */

package io.github.jiefenn8.graphloom.rdf.r2rml;

import com.google.gson.Gson;
import io.github.jiefenn8.graphloom.api.BaseEntityReference;
import io.github.jiefenn8.graphloom.api.EntityReference;
import org.apache.commons.lang3.StringUtils;

/**
 * This class extends the base methods of the {@link BaseEntityReference} to manage
 * configuration to only handle SQL retrieval through the use of custom SQL query.
 */
public class R2RMLView extends BaseEntityReference {

    /**
     * Constructs a R2RMLView with the specified custom SQL query as the source
     * payload.
     *
     * @param payload the payload containing info needed to get the view
     */
    protected R2RMLView(String payload) {
        super(payload, DatabaseType.QUERY, StringUtils.EMPTY);
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
