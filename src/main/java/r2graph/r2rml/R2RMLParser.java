package r2graph.r2rml;

import org.apache.jena.rdf.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import r2graph.configmap.ConfigMap;
import r2graph.configmap.ConfigMapFactory;
import r2graph.configmap.EntityMap;
import r2graph.configmap.PredicateObjectMap;
import r2graph.configmap.impl.PredicateObjectMapCom;
import r2graph.io.MappingDocument;

/**
 * R2RML Parser
 * <p>
 * This interface defines the base methods that manages the mapping
 * configuration of predicate and any objects related to parent entity
 * by the specified predicate.
 */
public class R2RMLParser {

    private final static Logger LOGGER = LoggerFactory.getLogger(R2RMLParser.class.getName());
    private final String r2rmlPrefix = "rr";

    /**
     * Parses and returns the R2RML mapping configuration stored in
     * MappingDocument into R2RML containing the collection of mapping components
     * for each type of configuration.
     * <p>
     * The parser searches for triple maps by collecting subjects that have the
     * logicalTable property since every triple maps must have a logicalTable property
     * to be a valid map. From there, the parser will be able to search through the graph
     * and gather the required properties and values to generate a R2RMLMap for
     * the mapping document.
     *
     * @param mappingDoc the document containing the mapping configuration
     * @return the R2RMLMap containing the mapping components
     */
    public ConfigMap parse(MappingDocument mappingDoc) {
        Model mappingGraph = mappingDoc.getMappingGraph();

        String r2rmlPrefixURI = mappingGraph.getNsPrefixURI(r2rmlPrefix);
        Property logicalTableProp = mappingGraph.getProperty(r2rmlPrefixURI, "logicalTable");
        Property subjectMapProp = mappingGraph.getProperty(r2rmlPrefixURI, "subjectMap");
        Property predicateObjectMapProp = mappingGraph.getProperty(r2rmlPrefixURI, "predicateObjectMap");
        Property tableNameProp = mappingGraph.getProperty(r2rmlPrefixURI, "tableName");
        Property templateProp = mappingGraph.getProperty(r2rmlPrefixURI, "template");
        Property classProp = mappingGraph.getProperty(r2rmlPrefixURI, "class");
        Property predicateProp = mappingGraph.getProperty(r2rmlPrefixURI, "predicate");
        Property objectMapProp = mappingGraph.getProperty(r2rmlPrefixURI, "objectMap");
        Property columnProp = mappingGraph.getProperty(r2rmlPrefixURI, "column");

        ConfigMap configMap = ConfigMapFactory.createR2RMLMap();
        ResIterator itr = mappingGraph.listSubjectsWithProperty(logicalTableProp);
        while (itr.hasNext()) {
            Resource res = itr.nextResource();

            EntityMap triplesMap = new TriplesMap();
            LOGGER.debug(res.getURI());

            if (!res.hasProperty(subjectMapProp)) {
                break;
            }

            //Check if any logical map exist
            Resource res2 = res.getPropertyResourceValue(logicalTableProp);
            LOGGER.debug(" : " + res2.getId());

            //Check if logical table have table name
            if (res2.hasProperty(tableNameProp)) {
                Statement stmt = res2.getProperty(tableNameProp);
                String tableName = stmt.getLiteral().toString();
                LOGGER.debug("  : " + stmt.getPredicate() + " : " + tableName);
                triplesMap.setEntitySource(tableName);
            }

            //Check if any subject map exist
            Resource res3 = res.getPropertyResourceValue(subjectMapProp);
            LOGGER.debug(" : " + res3.getId());

            //Check if subject map have template
            if (res3.hasProperty(templateProp)) {
                Statement stmt = res3.getProperty(templateProp);
                String template = stmt.getLiteral().toString();
                LOGGER.debug("  : " + stmt.getPredicate() + " : " + template);
                triplesMap.setTemplate(template);
            }

            //Check if subject map have class type
            if (res3.hasProperty(classProp)) {
                Statement stmt = res3.getProperty(classProp);
                String classType = stmt.getResource().getURI();
                LOGGER.debug("  : " + stmt.getPredicate() + " : " + classType);
                triplesMap.setClassType(classType);
            }

            if (res.hasProperty(predicateObjectMapProp)) {
                //Check if any predicate object map exists
                Resource res4 = res.getPropertyResourceValue(predicateObjectMapProp);
                LOGGER.debug(" : " + res4.getId());

                PredicateObjectMap predicateObjectMap = new PredicateObjectMapCom();

                //Check if predicate object map have predicate
                if (res4.hasProperty(predicateProp)) {
                    Statement stmt = res4.getProperty(predicateProp);
                    String predicate = stmt.getResource().getURI();
                    LOGGER.debug("  : " + stmt.getPredicate() + " : " + predicate);
                    predicateObjectMap.setPredicate(predicate);
                }

                //Check if predicate object map have object map
                Resource objectResource = res4.getPropertyResourceValue(objectMapProp);
                LOGGER.debug("  : " + objectResource.getId());

                if (objectResource.hasProperty(columnProp)) {
                    Statement stmt = objectResource.getProperty(columnProp);
                    String column = stmt.getLiteral().toString();
                    LOGGER.debug("   : " + stmt.getPredicate() + " : " + column);
                    predicateObjectMap.setObjectSource(column);
                }
                triplesMap.addPredicateObjectMap(predicateObjectMap);
            }
            configMap.addEntityMap(res.getLocalName(), triplesMap);
        }

        return configMap;
    }

}
