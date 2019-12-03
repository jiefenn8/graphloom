/*
 *    Copyright (c) 2019 - GraphLoom contributors (github.com/jiefenn8/graphloom)
 *    This software is made available under the terms of Apache License, Version 2.0.
 */

package com.github.jiefenn8.graphloom.rdf.r2rml;

import com.github.jiefenn8.graphloom.api.ConfigMaps;
import com.github.jiefenn8.graphloom.api.EntityMap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import java.util.*;

import static org.apache.jena.ext.com.google.common.base.Preconditions.checkNotNull;

/**
 * Implementation of R2RML with ConfigMaps interface.
 * This class stores and manages all the triples map associated
 * with a mapping document.
 */
public class R2RMLMap implements ConfigMaps {

    private final Map<String, String> nsPrefixMap;
    private final List<EntityMap> triplesMaps;

    protected R2RMLMap(Builder builder) {
        checkNotNull(builder);
        nsPrefixMap = ImmutableMap.copyOf(builder.nsPrefixMap);
        triplesMaps = ImmutableList.copyOf(builder.triplesMaps);
    }

    @Override
    public Map<String, String> getNamespaceMap() {
        return nsPrefixMap;
    }

    @Override
    public List<EntityMap> listEntityMaps() {
        return triplesMaps;
    }

    @Override
    public Iterator<EntityMap> iterator() {
        return triplesMaps.iterator();
    }

    public static class Builder {

        private static final String RR_PREFIX = "rr";
        private final Map<String, String> nsPrefixMap = new HashMap<>();
        private List<TriplesMap> triplesMaps = new ArrayList<>();

        public Builder addNsPrefix(String prefix, String namespace) {
            nsPrefixMap.put(prefix, namespace);
            return this;
        }

        public Builder addTriplesMap(TriplesMap triplesMap) {
            triplesMaps.add(triplesMap);
            return this;
        }

        public R2RMLMap build() {
            nsPrefixMap.putIfAbsent(RR_PREFIX, R2RMLSyntax.getURI());
            return new R2RMLMap(this);
        }
    }
}
