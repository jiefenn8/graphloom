package r2graph.r2rml;

import r2graph.configmap.EntityMap;
import r2graph.configmap.PredicateObjectMap;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of {@link EntityMap} interface.
 */
public class TriplesMap implements EntityMap {

    private String entitySource;
    private String template;
    private String classType;
    private List<PredicateObjectMap> predicateObjectMaps = new ArrayList();

    //Default constructor
    protected TriplesMap(){}

    @Override
    public void setEntitySource(String source) {
        this.entitySource = source;
    }

    @Override
    public String getEntitySource() {
        return entitySource;
    }

    @Override
    public void setTemplate(String template) {
        this.template = template;
    }

    @Override
    public String getTemplate() {
        return template;
    }

    @Override
    public void setClassType(String classType) {
        this.classType = classType;
    }

    @Override
    public String getClassType() {
        return classType;
    }

    @Override
    public void addPredicateObjectMap(PredicateObjectMap predicateObjectMap) {
        predicateObjectMaps.add(predicateObjectMap);
    }

    @Override
    public List<PredicateObjectMap> listPredicateObjectMaps() {
        return predicateObjectMaps;
    }
}
