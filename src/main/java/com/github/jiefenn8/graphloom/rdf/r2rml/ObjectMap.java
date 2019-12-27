/*
 *    Copyright (c) 2019 - GraphLoom contributors (github.com/jiefenn8/graphloom)
 *    This software is made available under the terms of Apache License, Version 2.0.
 */

package com.github.jiefenn8.graphloom.rdf.r2rml;

import com.github.jiefenn8.graphloom.api.EntityChild;
import com.github.jiefenn8.graphloom.api.EntityMap;
import com.github.jiefenn8.graphloom.api.NodeMap;
import com.github.jiefenn8.graphloom.api.Record;
import org.apache.jena.rdf.model.RDFNode;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Implementation of R2RML ObjectMap with {@link NodeMap} interface.
 * This term map will return either a rr:IRI, rr:BlankNode or rr:Literal for its main term.
 */
public class ObjectMap implements NodeMap, EntityChild {

    private EntityMap parent;
    private TermMap termMap;

    protected ObjectMap(TermMap m) {
        termMap = checkNotNull(m, "Term map must not be null.");
    }

    protected ObjectMap withParentMap(EntityMap m) {
        parent = m;
        return this;
    }

    @Override
    public EntityMap getEntityMap() {
        return parent;
    }

    @Override
    public RDFNode generateNodeTerm(Record r) {
        return termMap.generateRDFTerm(r);
    }
}
