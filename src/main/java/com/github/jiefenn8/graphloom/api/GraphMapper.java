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

import org.apache.jena.rdf.model.Model;

/**
 * This interface defines the base methods that manages the mapping
 * of a input source using provided mapping configurations.
 */
public interface GraphMapper {

    /**
     * Returns the resulting mapping of input source applied to the config
     * mappings given. Returns an empty model if the given
     * input source or configuration mappings was not sufficient enough
     * to generate any semantic terms.
     *
     * @param s input source containing the data to map over to graph
     * @param c config maps to manage the mapping of data
     * @return the model containing the mapped source as a graph model
     */
    Model mapToGraph(InputSource s, ConfigMaps c);
}
