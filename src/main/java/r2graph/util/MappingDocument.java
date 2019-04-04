package r2graph.util;

import org.apache.jena.rdf.model.Model;

/**
 * Mapping Document
 * <p>
 * This interface defines the base methods that manages the document
 * that contains the mapping configurations.
 */
public interface MappingDocument {

    /**
     * Returns mapping graph of the loaded mapping document data.
     *
     * @return the {@code Model} containing the mapping
     */
    Model getMappingGraph();
}
