package r2graph.r2rml;

import org.apache.jena.rdf.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import r2graph.exceptions.InvalidMappingDocumentException;
import r2graph.exceptions.base.FeijoaException;
import r2graph.io.MappingDocument;

/**
 * R2RML Validator
 * <p>
 * This class defines the base methods that manages the validation
 * of a loaded R2RML {@Model} in {@code MappingDocument}.
 * <p>
 * The validator works to validate the entire R2RML mapping document
 * as much as possible in order to reduce error that would occur during
 * parsing of the document.
 *
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
     * @param document  the document containing the mapping configuration to validate
     * @return          the document given if fully passes the validation
     */
    public Model validate(MappingDocument document) {
        findR2RMLGraph(document);
        findR2rmlPrefix();
        validateTriplesMaps();
        return r2rml;
    }

    private void findR2RMLGraph(MappingDocument document){
        try{
            this.r2rml = document.getMappingGraph();
        }catch(NullPointerException e){
            throw new FeijoaException("Mapping document does not exist.");
        }
    }

    private void findR2rmlPrefix(){
        r2rmlPrefixURI = r2rml.getNsPrefixURI(r2rmlPrefix);
    }

    /**
     * If there is no LogicalTable to base the search for TriplesMap, we can assume that
     * is no valid TriplesMap at all.
     */
    private void validateTriplesMaps() {
        ResIterator iter = r2rml.listSubjectsWithProperty(r2rml.getProperty(r2rmlPrefixURI, "logicalTable"));
        if(!iter.hasNext()){
            throw new InvalidMappingDocumentException("No TriplesMap found.");
        }

        while(iter.hasNext()){
            Resource res = iter.nextResource();
            validateLogicalTable(res);
            validateSubjectMap(res);
        }
    }

    private void validateSubjectMap(Resource res){
        Property subjectMapProp = r2rml.getProperty(r2rmlPrefixURI, "subjectMap");
        if(!res.hasProperty(subjectMapProp)){
            throw new InvalidMappingDocumentException("No SubjectMap found.");
        }
        //Validate subject map properties here
    }

    private Resource validateLogicalTable(Resource res){
        Resource ltRes = res.getPropertyResourceValue(
                r2rml.getProperty(r2rmlPrefixURI, "logicalTable"));
        if(!findBaseTableOrView(ltRes) & !findR2RMLView(ltRes)){
            throw new InvalidMappingDocumentException("Both BaseTableOrView and R2RMLView properties defined.");
        }
        return res;
    }

    private boolean findBaseTableOrView(Resource res){
        Property tableNameProp = r2rml.getProperty(r2rmlPrefixURI, "tableName");
        if(!res.hasProperty(tableNameProp)){
            return false;
        }
        return true;
    }

    private boolean findR2RMLView(Resource res) {
        Property sqlQueryProp = r2rml.getProperty(r2rmlPrefixURI, "sqlQuery");
        Property sqlVerProp = r2rml.getProperty(r2rmlPrefixURI, "sqlVersion");
        if(!res.hasProperty(sqlQueryProp) || !res.hasProperty(sqlVerProp)){
            return false;
        }
        return true;
    }
}
