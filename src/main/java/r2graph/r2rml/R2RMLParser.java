package r2graph.r2rml;

import r2graph.util.MappingDocument;

/**
 * R2RML Parser
 * <p>
 * This interface defines the base methods that manages the mapping
 * configuration of predicate and any objects related to parent entity
 * by the specified predicate.
 */
public class R2RMLParser {

    /**
     * Parses and returns the R2RML mapping configuration stored in
     * MappingDocument into R2RML containing the collection of mapping components
     * for each type of configuration.
     *
     * @param mappingDocument the document containing the mapping configuration
     * @return the R2RMLMap containing the mapping components
     */
    public R2RMLMap parse(MappingDocument mappingDocument) {
        return new R2RMLMap();
    }
}
