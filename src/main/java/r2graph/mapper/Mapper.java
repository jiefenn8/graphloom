package r2graph.mapper;

import org.apache.jena.rdf.model.Model;
import r2graph.configmap.ConfigMap;
import r2graph.util.InputDatabase;

public interface Mapper {
    Model mapToGraph(InputDatabase input, ConfigMap configMap);
}
