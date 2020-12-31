/*
 * Copyright (c) 2019 - GraphLoom contributors (github.com/jiefenn8/graphloom
 * This software is made available under the terms of Apache License, Version 2.0.
 */

package com.github.jiefenn8.graphloom.rdf;

import com.github.jiefenn8.graphloom.api.*;
import com.github.jiefenn8.graphloom.exceptions.MapperException;
import com.github.jiefenn8.graphloom.rdf.r2rml.LogicalTable;
import com.github.jiefenn8.graphloom.rdf.r2rml.RefObjectMap;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.RDF;

import java.util.Set;

/**
 * Implementation of {@link GraphMapper} interface using Jena; and R2RML
 * specifications to store the required data and functions to generate RDF terms.
 */
public class RDFMapper implements GraphMapper {

    /**
     * Main mapping function converting a data-source to a RDF graph model form
     * the provided DAO and mapping configurations.
     *
     * @param inputSource DAO providing access to the entity to map
     * @param configMaps  to control the mapping function process
     * @return the graph of the result from mapping
     */
    @Override
    public Model mapToGraph(InputSource inputSource, ConfigMaps configMaps) {
        if (inputSource == null) throw new MapperException("Cannot retrieve source data from null input source.");
        if (configMaps == null) throw new MapperException("Cannot map source from null config maps.");

        Model outputGraph = ModelFactory.createDefaultModel();
        outputGraph.setNsPrefixes(configMaps.getNamespaceMap());

        return outputGraph.add(mapSource(inputSource, configMaps.getEntityMaps()));
    }

    /**
     * Returns a model containing RDF triples generated from mapping the given
     * source using the provided set of mapping configs for each entity.
     *
     * @param source      the source to map over to RDF triples
     * @param triplesMaps the set of mapping configs
     * @return
     */
    private Model mapSource(InputSource source, Set<EntityMap> triplesMaps) {
        Model outputGraph = ModelFactory.createDefaultModel();
        triplesMaps.forEach((e) -> outputGraph.add(mapEntity(e, source)));

        return outputGraph;
    }

    /**
     * Returns a model containing RDF triples related to an entity generated
     * from mapping with the given source and entity mapping.
     *
     * @param triplesMap the entity mapping to generate terms
     * @param source     the source to map into RDF triples
     * @return model containing RDF triples related to an entity
     */
    private Model mapEntity(EntityMap triplesMap, InputSource source) {
        Model entityGraph = ModelFactory.createDefaultModel();
        triplesMap.applySource(source).forEachEntityRecord((r) -> {
            Resource subject = triplesMap.generateEntityTerm(r);
            triplesMap.listEntityClasses()
                    .forEach((c) -> entityGraph.add(subject, RDF.type, c));

            triplesMap.listRelationMaps().forEach((k) -> {
                NodeMap nodeMap = triplesMap.getNodeMapWithRelation(k);
                if (!(nodeMap instanceof RefObjectMap)) {
                    RDFNode node = nodeMap.generateNodeTerm(r);
                    if (node != null) {
                        entityGraph.add(subject, k.generateRelationTerm(r), node);
                    }
                }
            });
        });

        triplesMap.listRelationMaps().forEach((k) -> {
            NodeMap nodeMap = triplesMap.getNodeMapWithRelation(k);
            if (nodeMap instanceof RefObjectMap) {
                RefObjectMap refObjectMap = (RefObjectMap) nodeMap;
                EntityMap refTriplesMap = refObjectMap.getParentTriplesMap();
                LogicalTable refLogicalTable = (LogicalTable) refTriplesMap.applySource(null);
                LogicalTable rootLogicalTable = (LogicalTable) triplesMap.applySource(null);
                LogicalTable jointLogicalTable = new LogicalTable.Builder(rootLogicalTable)
                        .withJointQuery(refLogicalTable, refObjectMap.listJoinConditions())
                        .build();

                jointLogicalTable.loadInputSource(source).forEachEntityRecord((r) -> {
                    Resource subject = triplesMap.generateEntityTerm(r);
                    RDFNode node = refObjectMap.generateNodeTerm(r);
                    if (node != null) {
                        entityGraph.add(subject, k.generateRelationTerm(r), node);
                    }
                });
            }
        });

        return entityGraph;
    }
}
