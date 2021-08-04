/*
 * Copyright (c) 2019 - GraphLoom contributors (github.com/jiefenn8/graphloom)
 * This software is made available under the terms of Apache License, Version 2.0.
 */

package io.github.jiefenn8.graphloom.rdf.r2rml;

import com.google.gson.GsonBuilder;
import io.github.jiefenn8.graphloom.api.*;
import io.github.jiefenn8.graphloom.api.inputsource.Entity;
import io.github.jiefenn8.graphloom.util.GsonHelper;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.RDF;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Implementation of R2RML TriplesMap with {@link EntityMap} interface.
 * This class is an immutable class and requires the use of its {@link Builder}
 * class to populate and create an instance.
 */
public class TriplesMap implements EntityMap {

    private final String idName;
    private final LogicalTable logicalTable;
    private final SubjectMap subjectMap;
    private final Map<RelationMap, ObjectMap> predicateObjectMaps;
    private final Map<RelationMap, RefObjectMap> predicateRefObjectMaps;

    /**
     * Constructs a TriplesMap with the specified Builder containing the
     * properties to populate and initialise this immutable instance.
     *
     * @param builder the triples map builder to build from
     */
    private TriplesMap(Builder builder) {
        Objects.requireNonNull(builder);
        idName = builder.idName;
        logicalTable = builder.logicalTable;
        subjectMap = builder.subjectMap;
        predicateObjectMaps = Map.copyOf(builder.predicateObjectMaps);
        predicateRefObjectMaps = Map.copyOf(builder.predicateRefObjectMaps);
    }

    @Override
    public SourceMap getSourceMap() {
        return logicalTable;
    }

    @Override
    public Resource generateEntityTerm(Entity entity) {
        return subjectMap.generateEntityTerm(entity);
    }

    @Override
    public Model generateClassTerms(Resource term) {
        Model model = ModelFactory.createDefaultModel();
        subjectMap.listEntityClasses().forEach((c) -> {
            model.add(term, RDF.type, c);
        });
        return model;
    }

    @Override
    public Model generateNodeTerms(Resource term, Entity entity) {
        Model model = ModelFactory.createDefaultModel();
        predicateObjectMaps.forEach((r, n) -> {
            RDFNode node = n.generateNodeTerm(entity);
            if (node != null) {
                model.add(term, r.generateRelationTerm(entity), n.generateNodeTerm(entity));
            }
        });
        return model;
    }

    @Override
    public Model generateRefNodeTerms(Resource term, InputSource source) {
        Model model = ModelFactory.createDefaultModel();
        predicateRefObjectMaps.forEach((r, n) -> {
            LogicalTable jointLogicalTable = logicalTable.asJointLogicalTable(n);
            jointLogicalTable.forEachEntity(source, (e) -> {
                RDFNode node = n.generateNodeTerm(e);
                if (node != null) {
                    model.add(term, r.generateRelationTerm(e), node);
                }
            });
        });
        return model;
    }

    @Override
    public String getIdName() {
        return idName;
    }

    @Override
    public boolean hasNodeMapPairs() {
        return !predicateObjectMaps.isEmpty();
    }

    @Override
    public String toString() {
        return GsonHelper.loadTypeAdapters(new GsonBuilder())
                .create()
                .toJson(this);
    }

    @Override
    public String getUniqueId() {
        return idName;
    }

    /**
     * Builder class for TriplesMap.
     */
    public static class Builder {

        private static final Logger LOGGER = LoggerFactory.getLogger(Builder.class);
        private final String idName;
        private final LogicalTable logicalTable;
        private final SubjectMap subjectMap;
        private final Map<RelationMap, ObjectMap> predicateObjectMaps = new HashMap<>();
        private final Map<RelationMap, RefObjectMap> predicateRefObjectMaps = new HashMap<>();

        /**
         * Constructs a TriplesMap Builder with the specified triples map id,
         * logical table and subject map instance.
         *
         * @param idName       the name of this triples map definition
         * @param logicalTable the logical table to set on this triples map
         * @param subjectMap   the subject map to set on this triples map
         */
        public Builder(String idName, LogicalTable logicalTable, SubjectMap subjectMap) {
            this.idName = Objects.requireNonNull(idName, "ID name must not be null.");
            this.logicalTable = Objects.requireNonNull(logicalTable, "Logical table must not be null.");
            this.subjectMap = Objects.requireNonNull(subjectMap, "Subject map must not be null.");
        }

        /**
         * Adds a predicate map and object map pair to this triples map.
         *
         * @param pom the pair to add to triples map
         * @return this builder for fluent method chaining
         */
        public Builder addPredicateObjectMap(Pair<PredicateMap, NodeMap> pom) {
            NodeMap nodeMap = pom.getValue();
            if (nodeMap instanceof RefObjectMap) {
                RefObjectMap refObjectMap = (RefObjectMap) nodeMap;
                predicateRefObjectMaps.put(pom.getKey(), refObjectMap);
                return this;
            }
            predicateObjectMaps.put(pom.getKey(), (ObjectMap) pom.getValue());
            return this;
        }

        /**
         * Returns an immutable instance of triples maps containing the properties
         * given to its builder.
         *
         * @return instance of triples map created with the info in this builder
         */
        public TriplesMap build() {
            LOGGER.debug("Building TriplesMap from parameters. ID:{}", idName);
            TriplesMap triplesMap = new TriplesMap(this);
            LOGGER.debug("{}", triplesMap);
            return triplesMap;
        }
    }
}
