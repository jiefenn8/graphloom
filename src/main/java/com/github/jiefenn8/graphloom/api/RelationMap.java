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

import org.apache.jena.rdf.model.Property;

import java.util.Map;

/**
 * Object Map
 * <p>
 * This interface defines the base methods that manages the mapping
 * of relation to their graph relation term.
 */
public interface RelationMap {
    /**
     * Returns the RelationMap's relation as a term {@code Property}.
     *
     * @return the term {@code Property} created from relation.
     */
    Property getRelationTerm();

    Property getRelationTerm(Map<String, String> row);
}
