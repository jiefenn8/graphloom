package r2graph.r2rml;

import org.apache.jena.rdf.model.Model;
import r2graph.exceptions.ValidatorException;
import r2graph.io.MappingDocument;

/**
 * R2RML Validator
 * <p>
 * This class defines the base methods that manages the validation
 * of a loaded R2RML {@Model} in {@code MappingDocument}.
 */
public class R2RMLValidator {

    /**
     * Validates the R2RML mapping configuration in the {@code MappingDocument}
     * for any formatting or data entries that does not conforms to the R2RML standard.
     * <p>
     * The {@code MappingDocument} will be returned once it passes all validations checks
     * or a {@code ValidatorException} will be thrown if it fails.
     *
     * @param document  the document containing the mapping configuration to validate
     * @return          the document given if fully passes the validation
     */
    public MappingDocument validate(MappingDocument document) {
        try {
            Model r2rml = document.getMappingGraph();
            //perform validation checks here

            return document;
        } catch (Exception e) {
            throw new ValidatorException(e);
        }
    }
}
