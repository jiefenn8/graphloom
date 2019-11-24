/*
 *    Copyright (c) 2019 - GraphLoom contributors (github.com/jiefenn8/graphloom)
 *    This software is made available under the terms of Apache License, Version 2.0.
 */

package com.github.jiefenn8.graphloom.rdf.r2rml;

import com.github.jiefenn8.graphloom.api.ConfigMaps;
import com.github.jiefenn8.graphloom.api.EntityMap;

import java.util.*;

import static org.apache.jena.ext.com.google.common.base.Preconditions.checkNotNull;

/**
 * Implementation of R2RML with {@code ConfigMaps} interface.
 * This class stores and manages all the {@code TriplesMap} associated
 * with a mapping document.
 */
public class R2RMLMap implements ConfigMaps {

    private Map<String, String> namespaceMap = new HashMap<String, String>();
    private List<EntityMap> triplesMaps = new ArrayList<>();

    protected R2RMLMap(Map<String, String> ns) {
        namespaceMap.putAll(checkNotNull(ns));
    }

    @Override
    public Map<String, String> getNamespaceMap() {
        return namespaceMap;
    }

    @Override
    public List<EntityMap> listEntityMaps() {
        return Collections.unmodifiableList(triplesMaps);
    }

    public void addTriplesMap(EntityMap triplesMap) {
        triplesMaps.add(triplesMap);
    }
}
