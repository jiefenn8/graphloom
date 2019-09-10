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

import java.util.List;
import java.util.Map;

/**
 * Graph Maps
 * <p>
 * This interface defines the base methods to express the rules
 * from many configurations that will map data-source to a graph model.
 */
public interface ConfigMaps {

    /**
     * Returns namespace map of all prefix and {@code URI} that was used in the
     * mapping document.
     *
     * @return the map containing all prefixes and their {@code URI}.
     */

    Map<String, String> getNamespaceMap();

    /**
     * Returns all {@code EntityMap} that exists in the mapping configuration.
     *
     * @return all {@code EntityMap} associated.
     */
    List<EntityMap> listEntityMaps();
}