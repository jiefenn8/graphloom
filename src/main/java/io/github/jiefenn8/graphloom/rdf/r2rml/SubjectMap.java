/*
 * Copyright (c) 2019 - GraphLoom contributors (github.com/jiefenn8/graphloom)
 * This software is made available under the terms of Apache License, Version 2.0.
 */

package io.github.jiefenn8.graphloom.rdf.r2rml;

import com.google.gson.GsonBuilder;
import io.github.jiefenn8.graphloom.api.EntityMap;
import io.github.jiefenn8.graphloom.api.PropertyMap;
import io.github.jiefenn8.graphloom.api.inputsource.Entity;
import io.github.jiefenn8.graphloom.exceptions.MapperException;
import io.github.jiefenn8.graphloom.util.GsonHelper;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;

import java.util.*;

/**
 * Implementation of R2RML SubjectMap with {@link PropertyMap} interface.
 * This term map will return either a rr:IRI or rr:BlankNode for its main term.
 */
public class SubjectMap extends AbstractTermMap implements PropertyMap {

    private final UUID uuid = UUID.randomUUID();
    private final Set<Resource> classes;

    /**
     * Constructs a SubjectMap with the specified TermMap Builder containing the
     * data to initialise an immutable instance.
     *
     * @param builder the TermMap Builder to builder instance from
     */
    private SubjectMap(Builder builder) {
        super(builder);
        this.classes = Set.copyOf(builder.classes);
    }

    @Override
    public Resource generateEntityTerm(Entity entity) {
        RDFNode rdfTerm = generateRDFTerm(entity);
        return rdfTerm == null ? null : rdfTerm.asResource();
    }

    @Override
    public List<Resource> listEntityClasses() {
        return new ArrayList<>(classes);
    }

    @Override
    public String toString() {
        return GsonHelper.loadTypeAdapters(new GsonBuilder())
                .create()
                .toJson(this);
    }

    @Override
    public String getUniqueId() {
        return uuid.toString();
    }

    /**
     * Builder class for SubjectMap.
     */
    public static class Builder extends AbstractBuilder {

        private final Set<Resource> classes = new HashSet<>();

        /**
         * Constructs a Builder with the specified base value and valued type.
         *
         * @param baseValue  the value for this TermMap to use
         * @param valuedType the type
         */
        public Builder(RDFNode baseValue, ValuedType valuedType) {
            super(baseValue, valuedType);
        }

        @Override
        public Builder termType(TermType type) {
            if (type.equals(TermType.LITERAL)) {
                throw new MapperException("SubjectMap can only generate IRI or BlankNode.");
            }
            this.termType = type;
            return this;
        }

        /**
         * Associates a collection of classes with this to the RDF term that
         * this TermMap will generate.
         *
         * @param classes Set containing unique classes
         * @return this Builder for method chaining
         */
        public Builder addEntityClasses(Set<Resource> classes) {
            this.classes.addAll(classes);
            return this;
        }

        @Override
        public SubjectMap build() {
            return new SubjectMap(this);
        }
    }

}
