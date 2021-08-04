/*
 * Copyright (c) 2019 - GraphLoom contributors (github.com/jiefenn8/graphloom)
 * This software is made available under the terms of Apache License, Version 2.0.
 */

package io.github.jiefenn8.graphloom.rdf.r2rml;

import com.google.gson.Gson;
import io.github.jiefenn8.graphloom.api.inputsource.BaseEntityReference;
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
     * @param builder the r2rml view builder ot build from
     */
    protected R2RMLView(Builder builder) {
        super(builder.payload, DatabaseType.QUERY, StringUtils.EMPTY);
        this.setProperty("sqlVersion", builder.sqlVersion);
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    /**
     * Builder class for R2RMLView.
     */
    public static class Builder {

        private final String payload;
        private final String sqlVersion;

        /**
         * Constructs an instance of Builder with the given payload and
         * sql version.
         *
         * @param payload    the payload containing the info needed to get the view
         * @param sqlVersion the sql version that this payload supports
         */
        public Builder(String payload, String sqlVersion) {
            this.payload = payload;
            this.sqlVersion = sqlVersion;
        }

        /**
         * Returns an immutable instance of r2rml view containing the properties
         * given to its builder.
         *
         * @return instance of r2rml view created with the info in this builder
         */
        public R2RMLView build() {
            return new R2RMLView(this);
        }
    }
}
