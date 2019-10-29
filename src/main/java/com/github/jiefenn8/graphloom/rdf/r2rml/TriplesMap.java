/*
 *    Copyright (c) 2019 - Javen Liu (github.com/jiefenn8)
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.github.jiefenn8.graphloom.rdf.r2rml;

import com.github.jiefenn8.graphloom.api.EntityMap;
import com.github.jiefenn8.graphloom.api.NodeMap;
import com.github.jiefenn8.graphloom.api.RelationMap;
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

    protected TriplesMap(LogicalTable source, SubjectMap subject) {
        logicalTable = checkNotNull(source);
        subjectMap = checkNotNull(subject);
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
    public Resource generateEntityTerm(Map<String, String> entityProps) {
        return subjectMap.generateEntityTerm(entityProps);
    }

    @Override
    public List<Resource> listEntityClasses() {
        return subjectMap.listEntityClasses();
    }

    @Override
    public String getSource() {
        return logicalTable.getSource();
    }

    /**
     * Adds a {@code PredicateMap} and {@code ObjectMap} pair to {@code TriplesMap}.
     *
     * @param relationMap to add as key to the map.
     * @param nodeMap     as value for {@code relationMap} key.
     */
    public void addRelationNodePair(RelationMap relationMap, NodeMap nodeMap) {
        predicateObjectMaps.put(relationMap, nodeMap);
    }
}
