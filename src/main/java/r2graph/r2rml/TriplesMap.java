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
    protected TriplesMap() {
    }

    /**
     * @return the String of the entity source name
     * @see EntityMap#getEntitySource() (String)
     */
    @Override
    public String getEntitySource() {
        return entitySource;
    }

    /**
     * @param source the String of the source name to use as reference
     * @see EntityMap#setEntitySource(String)
     */
    @Override
    public void setEntitySource(String source) {
        this.entitySource = source;
    }

    /**
     * @return the String containing template pattern
     * @see EntityMap#getTemplate()
     */
    @Override
    public String getTemplate() {
        return template;
    }

    /**
     * @param template the String of the template to use as a pattern
     * @see EntityMap#setTemplate(String)
     */
    @Override
    public void setTemplate(String template) {
        this.template = template;
    }

    /**
     * @return the class type String associated with the entity, or
     * {@code null} if this entity is has no class type
     * @see EntityMap#getClassType()
     */
    @Override
    public String getClassType() {
        return classType;
    }

    /**
     * @param classType the String of the class type to associate this entity with
     * @see EntityMap#setClassType(String)
     */
    @Override
    public void setClassType(String classType) {
        this.classType = classType;
    }

    /**
     * @param predicateObjectMap the {@code PredicateObjectMap} to be appended
     * @see EntityMap#addPredicateObjectMap(PredicateObjectMap)
     */
    @Override
    public void addPredicateObjectMap(PredicateObjectMap predicateObjectMap) {
        predicateObjectMaps.add(predicateObjectMap);
    }

    /**
     * @return list of {@code PredicateObjectMap} configs
     * @see EntityMap#listPredicateObjectMaps()
     */
    @Override
    public List<PredicateObjectMap> listPredicateObjectMaps() {
        return predicateObjectMaps;
    }
}
