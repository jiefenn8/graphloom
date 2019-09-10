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

import org.apache.jena.rdf.model.Resource;

import java.util.List;
import java.util.Map;

/**
 * Property Map
 * <p>
 * This interface defines the base methods that manages the mapping
 * of entity properties to their graph entity terms.
 */
public interface PropertyMap {
    /**
     * Returns a generated entity term object using the value found with
     * the column_name specified in template using record given.
     *
     * @param entityRow the entity record to find the entity id value.
     * @return term object generated from row id.
     */
    Resource generateEntityTerm(Map<String, String> entityRow);

    /**
     * Returns an enumerator of all class associated with the entity.
     *
     * @return the list with all class associated.
     */
    List<Resource> listEntityClasses();
}