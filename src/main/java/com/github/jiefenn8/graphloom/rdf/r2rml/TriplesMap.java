/*
 *    Copyright (c) 2019 - GraphLoom contributors (github.com/jiefenn8/graphloom)
 *    This software is made available under the terms of Apache License, Version 2.0.
 */

package com.github.jiefenn8.graphloom.rdf.r2rml;

import com.github.jiefenn8.graphloom.api.*;
import org.apache.jena.rdf.model.Resource;

import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Implementation of R2RML TriplesMap with {@link EntityMap} interface.
 */
public class TriplesMap implements EntityMap {

    private LogicalTable logicalTable;
    private SubjectMap subjectMap;
    private Map<RelationMap, NodeMap> predicateObjectMaps = new HashMap<>();

    protected TriplesMap(LogicalTable lt, SubjectMap sm) {
        logicalTable = checkNotNull(lt, "Logical table must not be null.");
        subjectMap = checkNotNull(sm, "Subject map must not be null.").withParentMap(this);
    }

    @Override
    public SourceMap applySource(InputSource source) {
        return logicalTable.loadInputSource(source);
    }

    @Override
    public Set<RelationMap> listRelationMaps() {
        return Collections.unmodifiableSet(predicateObjectMaps.keySet());
    }

    @Override
    public NodeMap getNodeMapWithRelation(RelationMap relationMap) {
        return predicateObjectMaps.get(relationMap);
    }

    @Override
    public boolean hasRelationNodeMaps() {
        return !predicateObjectMaps.isEmpty();
    }

    @Override
    public Resource generateEntityTerm(Record entityProps) {
        return subjectMap.generateEntityTerm(entityProps);
    }

    @Override
    public List<Resource> listEntityClasses() {
        return subjectMap.listEntityClasses();
    }

    /**
     * Adds a {@code PredicateMap} and {@code ObjectMap} pair to {@code TriplesMap}.
     *
     * @param predicateMap to add as key to the map.
     * @param objectMap    as value for {@code relationMap} key.
     */
    public void addRelationNodePair(PredicateMap predicateMap, ObjectMap objectMap) {
        predicateObjectMaps.put(predicateMap.withParentMap(this), objectMap.withParentMap(this));
    }
}
