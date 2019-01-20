import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

import java.io.InputStream;

public class MappingDocument {

    private final String R2RML_NS_PREFIX = "rr";
    private R2RMLMapping r2rmlMap;

    public R2RMLMapping load(InputStream stream) {
        Model r2rmlModel = ModelFactory.createDefaultModel();
        r2rmlModel.read(stream, "http://example.com/ns#", "TTL");

        //todo: parse code of r2rml model to r2rml entities map.

        return r2rmlMap;
    }

    public R2RMLMapping getR2RMLMap() {
        return r2rmlMap;
    }
}
