/*
 *    Copyright (c) 2019 - GraphLoom contributors (github.com/jiefenn8/graphloom)
 *    This software is made available under the terms of Apache License, Version 2.0.
 */

package com.github.jiefenn8.graphloom.rdf;

import com.github.jiefenn8.graphloom.api.*;
import com.github.jiefenn8.graphloom.exceptions.MapperException;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.RDF;

import java.util.Set;

/**
 * Implementation of {@link GraphMapper} interface using Jena; and R2RML
 * specifications to store the required data and functions to generate RDF terms.
 */
public class RDFMapper implements GraphMapper {

    /**
     * Main mapping function converting a data-source to a RDF graph
     * model form the provided DAO and mapping configurations.
     *
     * @param source  DAO providing access to the entity to map.
     * @param configs to control the mapping function process.
     * @return the graph of the result from mapping.
     */
    @Override
    public Model mapToGraph(InputSource source, ConfigMaps configs) {
        if (source == null) throw new MapperException("Cannot retrieve source data from null input source.");
        if (configs == null) throw new MapperException("Cannot map source from null config maps.");

        Model outputGraph = ModelFactory.createDefaultModel();
        outputGraph.setNsPrefixes(configs.getNamespaceMap());

        return outputGraph.add(mapSource(source, configs.getEntityMaps()));
    }

    private Model mapSource(InputSource source, Set<EntityMap> triplesMaps) {
        Model outputGraph = ModelFactory.createDefaultModel();
        triplesMaps.forEach((e) -> outputGraph.add(mapEntity(e, source)));

        return outputGraph;
    }

    private Model mapEntity(EntityMap triplesMap, InputSource source) {
        Model entityGraph = ModelFactory.createDefaultModel();
        triplesMap.applySource(source).forEachEntityRecord((r) -> {
            Resource subject = triplesMap.generateEntityTerm(r);
            triplesMap.listEntityClasses().forEach(
                    (c) -> entityGraph.add(subject, RDF.type, c));

            triplesMap.listRelationMaps().forEach(
                    (k) -> {
                        NodeMap nodeMap = triplesMap.getNodeMapWithRelation(k);
                        entityGraph.add(subject, k.generateRelationTerm(r), nodeMap.generateNodeTerm(r));
                    });
        });

        return entityGraph;
    }
}
