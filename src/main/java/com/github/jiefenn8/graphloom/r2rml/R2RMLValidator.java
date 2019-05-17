package com.github.jiefenn8.graphloom.r2rml;

import com.github.jiefenn8.graphloom.exceptions.InvalidMappingDocumentException;
import com.github.jiefenn8.graphloom.exceptions.base.GraphLoomException;
import com.github.jiefenn8.graphloom.io.MappingDocument;
import org.apache.jena.rdf.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * R2RML Validator
 * <p>
 * This class defines the base methods that manages the validation
 * of a loaded R2RML {@Model} in {@code MappingDocument}.
 * <p>
 * The validator works to validate the entire R2RML mapping document
 * as much as possible in order to reduce error that would occur during
 * parsing of the document.
 */
public class R2RMLValidator {

    private final static Logger LOGGER = LoggerFactory.getLogger(R2RMLValidator.class.getName());
    private String r2rmlPrefix = "rr";
    private Model r2rml;
    private String r2rmlPrefixURI;

    /**
     * Validates the R2RML mapping configuration in the {@code MappingDocument}
     * for any formatting or data entries that does not conforms to the R2RML standard.
     * <p>
     * The {@code MappingDocument} will be returned once it passes all validations checks
     * or a {@code FeijoaValidatorException} will be thrown if it fails.
     *
     * @param document the document containing the mapping configuration to validate
     * @return the document given if fully passes the validation
     */
    public Model validate(MappingDocument document) throws InvalidMappingDocumentException {
        LOGGER.info("Setting up validator");
        findR2RMLGraph(document);
        findR2rmlPrefix();
        validateTriplesMaps();
        LOGGER.info("Validation successful");
        return r2rml;
    }

    private void findR2RMLGraph(MappingDocument document) {
        try {
            this.r2rml = document.getMappingGraph();
        } catch (NullPointerException e) {
            throw new GraphLoomException("Mapping document does not exist.", e);
        }
    }

    private void findR2rmlPrefix() {
        r2rmlPrefixURI = r2rml.getNsPrefixURI(r2rmlPrefix);
    }

    /**
     * If there is no LogicalTable to base the search for TriplesMap, we can assume that
     * is no valid TriplesMap at all.
     */
    private void validateTriplesMaps() {
        LOGGER.info("Validating document");
        ResIterator iter = r2rml.listSubjectsWithProperty(r2rml.getProperty(r2rmlPrefixURI, "logicalTable"));
        if (!iter.hasNext()) {
            throw new InvalidMappingDocumentException("No TriplesMap found.");
        }

        while (iter.hasNext()) {
            Resource res = iter.nextResource();
            validateLogicalTable(res);
            findSubjectMap(res);
            StmtIterator pomIter = findPredicateObjectMap(res);
            while (pomIter.hasNext()) {
                validatePredicateObjectMap(pomIter.nextStatement().getObject().asResource());
            }
        }
    }

    private Resource findSubjectMap(Resource res) {
        Resource subRes = res.getPropertyResourceValue(r2rml.getProperty(r2rmlPrefixURI, "subjectMap"));
        if (subRes == null) {
            throw new InvalidMappingDocumentException("SubjectMap not found.");
        }
        //Check for properties
        if (!findTemplate(subRes)) {
            throw new InvalidMappingDocumentException("Template not defined.");
        }
        //todo:Add checks for column and constant term map
        return subRes;
    }

    private boolean findTemplate(Resource res) {
        return res.hasProperty(r2rml.getProperty(r2rmlPrefixURI, "template"));
    }

    private void validateLogicalTable(Resource res) {
        Resource ltRes = res.getPropertyResourceValue(
                r2rml.getProperty(r2rmlPrefixURI, "logicalTable"));
        boolean isDefined;
        if ((isDefined = findBaseTableOrView(ltRes)) == findR2RMLView(ltRes)) {
            String message = "Both BaseTableOrView and R2RMLView defined.";
            if (!isDefined) {
                message = "BaseTableOrView or R2RMLView not defined.";
            }
            throw new InvalidMappingDocumentException(message);
        }
    }

    private boolean findBaseTableOrView(Resource res) {
        return res.hasProperty(r2rml.getProperty(r2rmlPrefixURI, "tableName"));
    }

    private boolean findR2RMLView(Resource res) {
        return res.hasProperty(r2rml.getProperty(r2rmlPrefixURI, "sqlQuery"));
    }

    private StmtIterator findPredicateObjectMap(Resource res) {
        return res.listProperties(r2rml.getProperty(r2rmlPrefixURI, "predicateObjectMap"));
    }

    private void validatePredicateObjectMap(Resource res) {
        if (!findPredicateMap(res)) {
            throw new InvalidMappingDocumentException("PredicateMap not found.");
        }
        if (!findObjectMap(res)) {
            throw new InvalidMappingDocumentException("ObjectMap not found.");
        }
    }

    private boolean findPredicateMap(Resource res) {
        Property predicateMap = r2rml.getProperty(r2rmlPrefixURI, "predicateMap");
        Property predicate = r2rml.getProperty(r2rmlPrefixURI, "predicate");
        return res.hasProperty(predicateMap) || res.hasProperty(predicate);
    }

    private boolean findObjectMap(Resource res) {
        Property objectMap = r2rml.getProperty(r2rmlPrefixURI, "objectMap");
        Property object = r2rml.getProperty(r2rmlPrefixURI, "object");
        return res.hasProperty(objectMap) || res.hasProperty(object);
    }
}
