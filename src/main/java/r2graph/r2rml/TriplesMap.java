package r2graph.r2rml;

import java.util.List;

public interface TriplesMap {
    String getTableName();
    String getTemplate();
    String getClassType();
    List<PredicateObjectMap> getPredicateObjectMaps();
}
