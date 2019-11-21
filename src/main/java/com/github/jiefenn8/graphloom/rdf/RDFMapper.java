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

package com.github.jiefenn8.graphloom.rdf;

import com.github.jiefenn8.graphloom.api.ConfigMaps;
import com.github.jiefenn8.graphloom.api.EntityMap;
import com.github.jiefenn8.graphloom.api.GraphMapper;
import com.github.jiefenn8.graphloom.api.InputSource;
import com.github.jiefenn8.graphloom.exceptions.MapperException;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.RDF;

import java.util.List;

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
    public Model mapToGraph(InputSource source, ConfigMaps configs) {
        if (source == null) throw new MapperException("Cannot retrieve source data from null input source.");
        if (configs == null) throw new MapperException("Cannot map source from null config maps.");

        Model outputGraph = ModelFactory.createDefaultModel();
        outputGraph.setNsPrefixes(configs.getNamespaceMap());

        return outputGraph.add(mapSource(source, configs.listEntityMaps()));
    }

    private Model mapSource(InputSource source, List<EntityMap> triplesMaps) {
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

            //todo: Refactor to separate PredicateObjectMap class.
            triplesMap.listRelationMaps().forEach(
                    (k) -> entityGraph.add(
                            subject,
                            k.generateRelationTerm(r),
                            triplesMap.getNodeMapWithRelation(k).generateNodeTerm(r)));
        });

        return entityGraph;
    }
}
