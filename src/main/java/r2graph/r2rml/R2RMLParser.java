package r2graph.r2rml;

import org.apache.jena.rdf.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import r2graph.configmap.ConfigMap;
import r2graph.configmap.ConfigMapFactory;
import r2graph.configmap.EntityMap;
import r2graph.exceptions.ParserException;
import r2graph.exceptions.ValidatorException;
import r2graph.io.MappingDocument;

/**
 * R2RML Parser
 * <p>
 * This class defines the base methods that manages the mapping
 * configuration of predicate and any objects related to parent entity
 * by the specified predicate.
 */
public class R2RMLParser {

    private final static Logger LOGGER = LoggerFactory.getLogger(R2RMLParser.class.getName());
    private final String r2rmlPrefix = "rr";
    private Model mappingGraph;
    private String r2rmlPrefixURI;
    private R2RMLValidator r2rmlValidator = new R2RMLValidator();

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
     * @param document         the document containing the mapping configuration
     * @param validateDocument whether to validate the document in advance before parsing
     * @return the R2RMLMap containing the mapping components
     */
    public ConfigMap parse(MappingDocument document, boolean validateDocument) throws ParserException, ValidatorException {
        MappingDocument current = document;
        if (validateDocument) {
            current = r2rmlValidator.validate(current);
        }
        return initParser(current);
    }

    private ConfigMap initParser(MappingDocument document) {
        try {
            this.mappingGraph = document.getMappingGraph();
            r2rmlPrefixURI = this.mappingGraph.getNsPrefixURI(r2rmlPrefix);
            return findTriplesMaps();
        } catch (Exception e) {
            throw new ParserException(e);
        }
    }

    private ConfigMap findTriplesMaps() {
        ConfigMap configMap = ConfigMapFactory.createR2RMLMap();
        ResIterator itr = mappingGraph.listSubjectsWithProperty(mappingGraph.getProperty(r2rmlPrefixURI, "logicalTable"));
        while (itr.hasNext()) {
            Resource res = itr.nextResource();
            EntityMap triplesMap = new TriplesMap();

            findLogicalTable(res, triplesMap);
            findSubjectMap(res, triplesMap);
            findPredicateObjectMaps(res, triplesMap);

            configMap.addEntityMap(res.getLocalName(), triplesMap);
        }
        return configMap;
    }

    private void findPredicateObjectMaps(Resource resource, EntityMap triplesMap) {
        Property predicateObjectMapProp = mappingGraph.getProperty(r2rmlPrefixURI, "predicateObjectMap");
        Property objectMapProp = mappingGraph.getProperty(r2rmlPrefixURI, "objectMap");
        Property predicateProp = mappingGraph.getProperty(r2rmlPrefixURI, "predicate");
        Property columnProp = mappingGraph.getProperty(r2rmlPrefixURI, "column");

        if (resource.hasProperty(predicateObjectMapProp)) {
            //Check if any predicate object map exists
            Resource res4 = resource.getPropertyResourceValue(predicateObjectMapProp);
            PredicateObjectMap predicateObjectMap = new PredicateObjectMap();

            //Check if predicate object map have predicate
            if (res4.hasProperty(predicateProp)) {
                Statement stmt = res4.getProperty(predicateProp);
                predicateObjectMap.setPredicate(stmt.getResource().getURI());
            }

            //Check if predicate object map have object map
            Resource objectResource = res4.getPropertyResourceValue(objectMapProp);

            if (objectResource.hasProperty(columnProp)) {
                Statement stmt = objectResource.getProperty(columnProp);
                predicateObjectMap.setObjectSource(stmt.getLiteral().toString());
            }
            triplesMap.addPredicateMap(predicateObjectMap);
        }
    }

    private boolean findSubjectMap(Resource resource, EntityMap triplesMap) {
        Property subjectMapProp = mappingGraph.getProperty(r2rmlPrefixURI, "subjectMap");
        Property templateProp = mappingGraph.getProperty(r2rmlPrefixURI, "template");
        Property classProp = mappingGraph.getProperty(r2rmlPrefixURI, "class");

        //Check if any subject map exist
        Resource subjectResource = resource.getPropertyResourceValue(subjectMapProp);

        //Check if subject map have template
        if (subjectResource.hasProperty(templateProp)) {
            Statement stmt = subjectResource.getProperty(templateProp);
            triplesMap.setTemplate(stmt.getLiteral().toString());
        }

        //Check if subject map have class type
        if (subjectResource.hasProperty(classProp)) {
            Statement stmt = subjectResource.getProperty(classProp);
            triplesMap.setClassType(stmt.getResource().getURI());
        }

        return true;
    }

    private void findLogicalTable(Resource resource, EntityMap triplesMap) {
        Property tableNameProp = mappingGraph.getProperty(r2rmlPrefixURI, "tableName");

        //Check if any logical map exist
        Resource logicalTableResource = resource.getPropertyResourceValue(
                mappingGraph.getProperty(r2rmlPrefixURI, "logicalTable"));

        //Check if logical table have table name
        if (logicalTableResource.hasProperty(tableNameProp)) {
            Statement stmt = logicalTableResource.getProperty(tableNameProp);
            triplesMap.setEntitySource(stmt.getLiteral().toString());
        }
    }
}
