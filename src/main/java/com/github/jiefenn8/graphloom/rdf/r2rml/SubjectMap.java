/*
 *    Copyright (c) 2019 - GraphLoom contributors (github.com/jiefenn8/graphloom)
 *    This software is made available under the terms of Apache License, Version 2.0.
 */

package com.github.jiefenn8.graphloom.rdf.r2rml;

import com.github.jiefenn8.graphloom.api.EntityChild;
import com.github.jiefenn8.graphloom.api.EntityMap;
import com.github.jiefenn8.graphloom.api.PropertyMap;
import com.github.jiefenn8.graphloom.api.Record;
import com.github.jiefenn8.graphloom.exceptions.MapperException;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Implementation of R2RML SubjectMap with {@link PropertyMap} interface.
 * This term map will return either a rr:IRI or rr:BlankNode for its main term.
 */
public class SubjectMap implements PropertyMap, EntityChild {

    private EntityMap parent;
    private TermMap termMap;
    private List<Resource> classes = new ArrayList<>();

    protected SubjectMap(TermMap m) {
        termMap = checkNotNull(m, "Must provide a TermMap.");
    }

    /**
     * Adds class type to the entity associated with this subject map.
     *
     * @param r class to associate with entity
     */
    public void addEntityClass(Resource r) {
        classes.add(r);
    }

    @Override
    public Resource generateEntityTerm(Record r) {
        RDFNode term = termMap.generateRDFTerm(r);
        if (term.isLiteral()) throw new MapperException("SubjectMap can only return IRI or BlankNode.");
        return term.asResource();
    }

    protected SubjectMap withParentMap(EntityMap em) {
        parent = em;
        return this;
    }

    @Override
    public List<Resource> listEntityClasses() {
        return Collections.unmodifiableList(classes);
    }

    @Override
    public EntityMap getParentMap() {
        return parent;
    }
}
