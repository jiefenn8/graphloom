package r2graph.r2rml.impl;

import r2graph.r2rml.R2RML;
import r2graph.r2rml.TriplesMap;

import java.util.HashMap;
import java.util.Map;

public class R2RMLMap implements R2RML {
    private Map<String, TriplesMap> triplesMap = new HashMap<>();

    public TriplesMap addTriplesMap(String id, TriplesMap tripleMap){
        return triplesMap.put(id, tripleMap);
    }

    @Override
    public Map<String, TriplesMap> listTriplesMap() {
        return triplesMap;
    }

    @Override
    public TriplesMap getTriplesMapById(String id) {
        return triplesMap.get(id);
    }

    @Override
    public Boolean hasTriplesMapId(String id) {
        return triplesMap.containsKey(id);
    }
}
