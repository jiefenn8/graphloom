package r2graph.r2rml;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ResIterator;
import r2graph.exceptions.base.FeijoaValidatorException;
import r2graph.exceptions.RuleClassNotFoundException;
import r2graph.io.MappingDocument;

/**
 * R2RML Validator
 * <p>
 * This class defines the base methods that manages the validation
 * of a loaded R2RML {@Model} in {@code MappingDocument}.
 */
public class R2RMLValidator {

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
    public MappingDocument validate(MappingDocument document) {
        try {
            Model r2rml = document.getMappingGraph();
            //perform validation checks here
            initValidator(r2rml);

            return document;
        } catch (Exception e) {
            throw new FeijoaValidatorException(e);
        }
    }

    private void initValidator(Model r2rml) {
        try{
            this.r2rml = r2rml;
            r2rmlPrefixURI = this.r2rml.getNsPrefixURI(r2rmlPrefix);
            validateTriplesMaps();
        }catch(Exception e){
            throw new FeijoaValidatorException(e);
        }
    }

    /**
     * If there is no LogicalTable to base the search for TriplesMap, we can assume that
     * is no valid TriplesMap at all.
     */
    private void validateTriplesMaps() {
        ResIterator iter = r2rml.listSubjectsWithProperty(r2rml.getProperty(r2rmlPrefixURI, "logicalTable"));
        if(!iter.hasNext()){
            throw new RuleClassNotFoundException("No TriplesMap found.");
        }
    }
}
