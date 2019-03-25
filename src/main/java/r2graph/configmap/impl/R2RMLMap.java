package r2graph.configmap.impl;

import r2graph.configmap.ConfigMap;
import r2graph.configmap.EntityMap;

import java.util.HashMap;
import java.util.Map;

public class R2RMLMap implements ConfigMap {

    private Map<String, EntityMap> entityMap = new HashMap<>();

    protected R2RMLMap(){}

    @Override
    public EntityMap addEntityMap(String id, EntityMap triplesMap){
        return entityMap.put(id, triplesMap);
    }

    @Override
    public Map<String, EntityMap> listEntityMaps() {
        return entityMap;
    }

}
