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

import org.apache.jena.rdf.model.RDFNode;

import java.util.Map;

/**
 * Node Map
 * <p>
 * This interface defines the base method that manages the mapping of
 * any nodes to their graph node term.
 */
public interface NodeMap {
    /**
     * Returns a generated object node term using the value found form a
     * specified column_name in NodeMap using the provided entity records.
     *
     * @param row to find the column data.
     * @return the term object generated from column value.
     */
    RDFNode generateNodeTerm(Map<String, String> row);
}
