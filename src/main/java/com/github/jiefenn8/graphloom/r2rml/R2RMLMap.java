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

package com.github.jiefenn8.graphloom.r2rml;

import com.github.jiefenn8.graphloom.configmap.ConfigMap;
import com.github.jiefenn8.graphloom.configmap.EntityMap;

import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of {@link ConfigMap} interface.
 */
public class R2RMLMap implements ConfigMap {

    private Map<String, EntityMap> entityMaps = new HashMap<>();

    public R2RMLMap() {
    }

    /**
     * @param id        the id name of the entity map
     * @param entityMap the {@code EntityMap} to appended to map
     * @return the given {@code EntityMap} when successfully appended
     * @see ConfigMap#addEntityMap(String, EntityMap)
     */
    @Override
    public EntityMap addEntityMap(String id, EntityMap entityMap) {
        return entityMaps.put(id, entityMap);
    }

    /**
     * @return the map containing all {@code EntityMap}
     * @link{ ConfigMap#listEntityMaps()}
     * @see ConfigMap#listEntityMaps()
     */
    @Override
    public Map<String, EntityMap> listEntityMaps() {
        return entityMaps;
    }

}
