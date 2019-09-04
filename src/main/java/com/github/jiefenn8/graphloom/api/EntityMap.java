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

package com.github.jiefenn8.graphloom.api;

import java.util.Set;

/**
 * Entity Map
 * <p>
 * This interface defines the base methods that manages the mapping
 * of data to their graph terms sharing the same entity.
 */
public interface EntityMap extends SourceMap, PropertyMap {
    /**
     * Returns all {@code RelationMap} that this {@code EntityMap} has.
     *
     * @return all {@code RelationMap} in associated.
     */
    Set<RelationMap> listRelationMaps();

    /**
     * Returns the {@code NodeMap} associated with the {@code RelationMap}.
     *
     * @param relationMap key to search for paired ObjectMap.
     * @return the {@code NodeMap} found with {@param relationMap} key.
     */
    NodeMap getNodeMapWithRelation(RelationMap relationMap);

    /**
     * Checks if {@code EntityMap} has any {@code RelationMap} and {@code NodeMap} pair.
     *
     * @return true if there are any pair, otherwise false.
     */
    boolean hasRelationNodeMaps();


}
