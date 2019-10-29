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
import com.github.jiefenn8.graphloom.api.EntityChild;
import com.github.jiefenn8.graphloom.api.PropertyMap;
import com.github.jiefenn8.graphloom.exceptions.MapperException;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.apache.jena.ext.com.google.common.base.Preconditions.checkNotNull;

/**
 * Implementation of R2RML SubjectMap with {@link PropertyMap} interface.
 * This term map will return either a rr:IRI or rr:BlankNode for its main term.
 */
public class SubjectMap implements TermMap, PropertyMap, EntityChild {

    private EntityMap parent;
    private TermMap termMap;
    private List<Resource> classes = new ArrayList<>();

    protected SubjectMap(TermMap termMap){
        this.termMap = checkNotNull(termMap);
    }

    /**
     * Adds a class type to subject map.
     *
     * @param clazz to add to list.
     */
    public void addClass(Resource clazz) {
        classes.add(clazz);
    }

    @Override
    public Resource generateEntityTerm(Map<String, String> entityProps) {
        RDFNode term = generateRDFTerm(entityProps);
        if (term.isLiteral()) throw new MapperException("SubjectMap can only return IRI or BlankNode");
        return term.asResource();
    }

    protected SubjectMap withParentMap(EntityMap entityMap){
        this.parent = entityMap;
        return this;
    }

    @Override
    public List<Resource> listEntityClasses() {
        return Collections.unmodifiableList(classes);
    }

    @Override
    public RDFNode generateRDFTerm(Map<String, String> entityProps) {
        return termMap.generateRDFTerm(entityProps);
    }

    @Override
    public EntityMap getParentMap() {
        return parent;
    }
}
