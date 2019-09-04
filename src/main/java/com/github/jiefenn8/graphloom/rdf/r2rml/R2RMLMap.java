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

import com.github.jiefenn8.graphloom.api.ConfigMaps;
import com.github.jiefenn8.graphloom.api.EntityMap;

import java.util.*;

/**
 * Implementation of R2RML with {@code ConfigMaps} interface.
 * This class stores and manages all the {@code TriplesMap} associated
 * with a mapping document.
 */
public class R2RMLMap implements ConfigMaps {

    private Map<String, String> namespaceMap = new HashMap<String, String>();
    private List<EntityMap> triplesMaps = new ArrayList<>();

    public R2RMLMap(Map<String, String> ns) {
        if (ns != null) {
            namespaceMap.putAll(ns);
        }
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
