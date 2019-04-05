package r2graph.util;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

import java.io.InputStream;


/**
 * Mapping Document
 * <p>
 * This class defines the base methods that manages the document
 * that contains the mapping configurations.
 */
public class MappingDocument {

    private InputStream fileStream;
    private Model graph;

    //Stop usage of this constructor
    private MappingDocument() {
    }

    //Default constructor
    public MappingDocument(InputStream in) {
        this.fileStream = in;
    }

    private Model load() {
        return graph = ModelFactory.createDefaultModel().read(fileStream, null, "TTL");
    }

    /**
     * Returns mapping graph of the loaded mapping document data.
     *
     * @return the {@code Model} containing the mapping
     */
    public Model getMappingGraph() {
        if (graph == null) {
            return load();
        }

        return graph;
    }

}
