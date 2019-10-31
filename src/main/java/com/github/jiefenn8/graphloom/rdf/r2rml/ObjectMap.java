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

import com.github.jiefenn8.graphloom.api.EntityChild;
import com.github.jiefenn8.graphloom.api.EntityMap;
import com.github.jiefenn8.graphloom.api.NodeMap;
import com.github.jiefenn8.graphloom.api.Record;
import org.apache.jena.rdf.model.RDFNode;

import static org.apache.jena.ext.com.google.common.base.Preconditions.checkNotNull;

/**
 * Implementation of R2RML ObjectMap with {@link NodeMap} interface.
 * This term map will return either a rr:IRI, rr:BlankNode or rr:Literal for its main term.
 */
public class ObjectMap implements TermMap, NodeMap, EntityChild {

    private EntityMap parent;
    private TermMap termMap;

    protected ObjectMap(TermMap termMap) {
        this.termMap = checkNotNull(termMap);
    }

    protected ObjectMap withParentMap(EntityMap entityMap) {
        this.parent = entityMap;
        return this;
    }

    @Override
    public EntityMap getParentMap() {
        return parent;
    }

    @Override
    public RDFNode generateRDFTerm(Record entityProps) {
        return termMap.generateRDFTerm(entityProps);
    }

    @Override
    public RDFNode generateNodeTerm(Record entityProps) {
        return generateRDFTerm(entityProps);
    }
}
