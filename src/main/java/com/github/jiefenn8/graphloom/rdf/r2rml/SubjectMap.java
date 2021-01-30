/*
 * Copyright (c) 2019 - GraphLoom contributors (github.com/jiefenn8/graphloom
 * This software is made available under the terms of Apache License, Version 2.0.
 */

package com.github.jiefenn8.graphloom.rdf.r2rml;

import com.github.jiefenn8.graphloom.api.EntityChild;
import com.github.jiefenn8.graphloom.api.EntityMap;
import com.github.jiefenn8.graphloom.api.PropertyMap;
import com.github.jiefenn8.graphloom.api.inputsource.Entity;
import com.github.jiefenn8.graphloom.exceptions.MapperException;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;

import java.util.*;

/**
 * Implementation of R2RML SubjectMap with {@link PropertyMap} interface.
 * This term map will return either a rr:IRI or rr:BlankNode for its main term.
 */
public class SubjectMap implements PropertyMap, EntityChild {

    private final TermMap termMap;
    private final List<Resource> classes = new ArrayList<>();
    private EntityMap parent;

    /**
     * Constructs a SubjectMap with the specified term map that is either
     * a constant, template or a column typ.
     *
     * @param termMap the term map that this instance will behave as
     */
    protected SubjectMap(TermMap termMap) {
        this.termMap = Objects.requireNonNull(termMap, "Must provide a TermMap.");
    }

    /**
     * Adds class type to the entity associated with this subject map.
     *
     * @param resource the class to associate with entity
     */
    public void addEntityClass(Resource resource) {
        classes.add(resource);
    }

    @Override
    public Resource generateEntityTerm(Entity entity) {
        RDFNode term = termMap.generateRDFTerm(entity);
        if (term.isLiteral()) {
            throw new MapperException("SubjectMap can only return IRI or BlankNode.");
        }
        return term.asResource();
    }

    public Resource generateEntityTerm(Set<JoinCondition> joins, Entity entity) {
        RDFNode term = termMap.generateRDFTerm(joins, entity);
        if (term.isLiteral()) {
            throw new MapperException("SubjectMap can only return IRI or BlankNode.");
        }
        return term.asResource();
    }

    /**
     * Adds association to an triples map that this subject map belongs to.
     *
     * @param entityMap the triples map to associate with
     * @return this builder for fluent method chaining
     */
    protected SubjectMap withParentMap(EntityMap entityMap) {
        parent = entityMap;
        return this;
    }

    @Override
    public List<Resource> listEntityClasses() {
        return Collections.unmodifiableList(classes);
    }

    @Override
    public EntityMap getEntityMap() {
        return parent;
    }
}
