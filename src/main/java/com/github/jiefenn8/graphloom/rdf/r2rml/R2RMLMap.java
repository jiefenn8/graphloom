/*
 * Copyright (c) 2019 - GraphLoom contributors (github.com/jiefenn8/graphloom
 * This software is made available under the terms of Apache License, Version 2.0.
 */

package com.github.jiefenn8.graphloom.rdf.r2rml;

import com.github.jiefenn8.graphloom.api.ConfigMaps;
import com.github.jiefenn8.graphloom.api.EntityMap;

import java.util.*;

/**
 * Implementation of R2RML with ConfigMaps interface.
 * This class stores and manages all the triples map associated
 * with a mapping document.
 */
public class R2RMLMap implements ConfigMaps {

    private final Map<String, String> nsPrefixMap;
    private final Set<EntityMap> triplesMaps;

    /**
     * Constructs a R2RMLMap using the parameters provided by the
     * given class builder to create an immutable object.
     *
     * @param builder the class builder with the parameters
     */
    protected R2RMLMap(Builder builder) {
        Objects.requireNonNull(builder);
        nsPrefixMap = Map.copyOf(builder.nsPrefixMap);
        triplesMaps = Set.copyOf(builder.triplesMaps);
    }

    @Override
    public Map<String, String> getNamespaceMap() {
        return nsPrefixMap;
    }

    @Override
    public Set<EntityMap> getEntityMaps() {
        return triplesMaps;
    }

    @Override
    public Iterator<EntityMap> iterator() {
        return triplesMaps.iterator();
    }

    public static class Builder {

        private static final String RR_PREFIX = "rr";
        private final Map<String, String> nsPrefixMap = new HashMap<>();
        private final Set<TriplesMap> triplesMaps = new HashSet<>();

        /**
         * Adds the prefix along with its namespace in the namespace map.
         *
         * @param prefix    the prefix that represent the namespace
         * @param namespace the given value mapped to the prefix
         * @return builder of this R2RMLMap class
         */
        public Builder addNsPrefix(String prefix, String namespace) {
            nsPrefixMap.put(prefix, namespace);
            return this;
        }

        /**
         * Appends given triples map to this R2RMLMap.
         *
         * @param triplesMap the unique triples map to add
         * @return builder of this R2RMLMap class
         */
        public Builder addTriplesMap(TriplesMap triplesMap) {
            triplesMaps.add(triplesMap);
            return this;
        }

        /**
         * Returns an immutable instance of R2RMLMap with this builder containing
         * the parameters given to populate the object.
         *
         * @return the instance of R2RMLMap created with the info in this builder
         */
        public R2RMLMap build() {
            nsPrefixMap.putIfAbsent(RR_PREFIX, R2RMLSyntax.getURI());
            return new R2RMLMap(this);
        }
    }
}
