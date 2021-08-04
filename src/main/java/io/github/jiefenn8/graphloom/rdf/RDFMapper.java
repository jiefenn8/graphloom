/*
 * Copyright (c) 2019 - GraphLoom contributors (github.com/jiefenn8/graphloom)
 * This software is made available under the terms of Apache License, Version 2.0.
 */

package io.github.jiefenn8.graphloom.rdf;

import io.github.jiefenn8.graphloom.api.ConfigMaps;
import io.github.jiefenn8.graphloom.api.EntityMap;
import io.github.jiefenn8.graphloom.api.GraphMapper;
import io.github.jiefenn8.graphloom.api.InputSource;
import io.github.jiefenn8.graphloom.exceptions.MapperException;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

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
        outputGraph.add(mapEntity(inputSource, configMaps.getEntityMaps()));
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
    private Model mapEntity(InputSource source, Set<EntityMap> triplesMaps) {
        Model outputGraph = ModelFactory.createDefaultModel();
        triplesMaps.forEach((t) -> {
            String id = t.getIdName();
            MDC.put("TRIPLESMAP_ID", id);
            LOGGER.info("Generating triples with '{}'.", id);
            t.getSourceMap().forEachEntity(source, (e) -> {
                Resource subject = t.generateEntityTerm(e);
                outputGraph.add(t.generateClassTerms(subject));
                LOGGER.info("Class property triples generated.");
                outputGraph.add(t.generateNodeTerms(subject, e));
                LOGGER.info("Node terms generated for entity.");
                outputGraph.add(t.generateRefNodeTerms(subject, source));
                LOGGER.info("Reference node terms generated for entity.");
            });
            LOGGER.info("Completed generating {} triples for '{}'.", outputGraph.size(), id);
            MDC.clear();
        });
        return outputGraph;
    }
}
