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

import com.github.jiefenn8.graphloom.configmap.EntityMap;
import com.github.jiefenn8.graphloom.configmap.PredicateMap;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of {@link EntityMap} interface.
 */
public class TriplesMap implements EntityMap {

    private String entitySource;
    private String template;
    private String classType;
    private List<PredicateMap> predicateMaps = new ArrayList();

    //Default constructor
    protected TriplesMap() {
    }

    /**
     * @return the String of the entity source name
     * @see EntityMap#getEntitySource() (String)
     */
    @Override
    public String getEntitySource() {
        return entitySource;
    }

    /**
     * @param source the String of the source name to use as reference
     * @see EntityMap#setEntitySource(String)
     */
    @Override
    public void setEntitySource(String source) {
        this.entitySource = source;
    }

    /**
     * @return the String containing template pattern
     * @see EntityMap#getTemplate()
     */
    @Override
    public String getTemplate() {
        return template;
    }

    /**
     * @param template the String of the template to use as a pattern
     * @see EntityMap#setTemplate(String)
     */
    @Override
    public void setTemplate(String template) {
        this.template = template;
    }

    /**
     * @return the class type String associated with the entity, or
     * {@code null} if this entity is has no class type
     * @see EntityMap#getClassType()
     */
    @Override
    public String getClassType() {
        return classType;
    }

    /**
     * @param classType the String of the class type to associate this entity with
     * @see EntityMap#setClassType(String)
     */
    @Override
    public void setClassType(String classType) {
        this.classType = classType;
    }

    /**
     * @param predicateMap the {@code PredicateMap} to be appended
     * @see EntityMap#addPredicateMap(PredicateMap) (PredicateMap)
     */
    @Override
    public void addPredicateMap(PredicateMap predicateMap) {
        predicateMaps.add(predicateMap);
    }

    /**
     * @return list of {@code PredicateMap} configs
     * @see EntityMap#listPredicateMaps() ()
     */
    @Override
    public List<PredicateMap> listPredicateMaps() {
        return predicateMaps;
    }
}
