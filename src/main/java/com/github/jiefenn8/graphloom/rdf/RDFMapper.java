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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.List;
import java.util.Set;

/**
 * Implementation of {@link GraphMapper} interface using Jena; and R2RML
 * specifications to store the required data and functions to generate RDF terms.
 */
public class RDFMapper implements GraphMapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(RDFMapper.class);

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

        LOGGER.info("Starting RDF mapping process.");
        Model outputGraph = ModelFactory.createDefaultModel();
        outputGraph.setNsPrefixes(configMaps.getNamespaceMap());
        outputGraph.add(mapSource(inputSource, configMaps.getEntityMaps()));
        LOGGER.info("Finished mapping source to RDF model. Total of {} triples generated.", outputGraph.size());
        return outputGraph;
    }

    /**
     * Returns a model containing RDF triples generated from mapping the given
     * source using the provided set of mapping configs for each entity.
     *
     * @param source      the source to map over to RDF triples
     * @param triplesMaps the set of mapping configs
     * @return the model containing all the generated terms
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
        String triplesMapId = triplesMap.getIdName();
        MDC.put("TRIPLESMAP_ID", triplesMapId);
        LOGGER.info("Generating triples with '{}'.", triplesMapId);
        Model entityGraph = ModelFactory.createDefaultModel();
        SourceMap sourceMap = triplesMap.getSourceMap();
        sourceMap.forEachEntity(source, (e) -> {
            Resource subject = triplesMap.generateEntityTerm(e);
            List<Resource> classes = triplesMap.listEntityClasses();
            LOGGER.debug("Found {} class to map to entity.", classes.size());
            int classCount = 0;
            for (Resource resource : classes) {
                entityGraph.add(subject, RDF.type, resource);
                classCount++;
            }
            LOGGER.info("{} class property triples generated.", classCount);

            Set<RelationMap> entityProperties = triplesMap.listRelationMaps();
            LOGGER.debug("Found a total of {} property to map to entity.", entityProperties.size());
            int propertyCount = 0;
            for (RelationMap relationMap : entityProperties) {
                NodeMap nodeMap = triplesMap.getNodeMapWithRelation(relationMap);
                if (!(nodeMap instanceof RefObjectMap)) {
                    RDFNode node = nodeMap.generateNodeTerm(e);
                    if (node != null) {
                        entityGraph.add(subject, relationMap.generateRelationTerm(e), node);
                        propertyCount++;
                    }
                }
            }
            LOGGER.info("{} base property triples generated.", propertyCount);
        });

        int entityRefCount = 0;
        for (RelationMap relationMap : triplesMap.listRelationMaps()) {
            NodeMap nodeMap = triplesMap.getNodeMapWithRelation(relationMap);
            if (nodeMap instanceof RefObjectMap) {
                RefObjectMap rdfObjMap = (RefObjectMap) nodeMap;
                EntityMap refTriplesMap = rdfObjMap.getParentTriplesMap();
                LogicalTable refLogicalTable = (LogicalTable) refTriplesMap.getSourceMap();
                LogicalTable rootLogicalTable = (LogicalTable) triplesMap.getSourceMap();
                LogicalTable jointLogicalTable = new LogicalTable.Builder(rootLogicalTable)
                        .withJointQuery(refLogicalTable, rdfObjMap.listJoinConditions())
                        .build();

                jointLogicalTable.forEachEntity(source, (e) -> {
                    Resource subject = triplesMap.generateEntityTerm(e);
                    RDFNode node = rdfObjMap.generateNodeTerm(e);
                    if (node != null) {
                        entityGraph.add(subject, relationMap.generateRelationTerm(e), node);
                    }
                });
            }
            entityRefCount++;
        }
        LOGGER.info("{} entity reference property triples generated.", entityRefCount);

        LOGGER.info("Completed generating {} triples for '{}'.", entityGraph.size(), triplesMapId);
        MDC.clear();
        return entityGraph;
    }
}
