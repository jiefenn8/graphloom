package r2graph.r2rml;

import java.util.Map;

public interface R2RML {
    Map<String, TriplesMap> listTriplesMap();
    TriplesMap getTriplesMapById(String id);
    Boolean hasTriplesMapId(String id);
}
