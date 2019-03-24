package r2graph.configmap.impl;

import r2graph.configmap.ConfigMap;
import r2graph.configmap.EntityMap;

import java.util.HashMap;
import java.util.Map;

public class R2RMLMap implements ConfigMap {

    private Map<String, EntityMap> triplesMap = new HashMap<>();

    protected R2RMLMap(){}

    @Override
    public EntityMap addTriplesMap(String id, EntityMap tripleMap){
        return triplesMap.put(id, tripleMap);
    }

    @Override
    public Map<String, EntityMap> listTriplesMap() {
        return triplesMap;
    }

}
