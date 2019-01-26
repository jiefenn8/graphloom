package r2graph.mapper;

import org.apache.jena.rdf.model.Model;
import r2graph.r2rml.R2RML;
import r2graph.util.InputDatabase;

public interface Mapper {
    Model mapToGraph(InputDatabase input, R2RML r2rml);
}
