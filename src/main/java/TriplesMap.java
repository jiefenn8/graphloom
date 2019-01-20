import org.apache.jena.rdf.model.Model;

import java.util.ArrayList;
import java.util.List;

public class TriplesMap {
    private LogicalTable logicalTable;
    private SubjectMap subjectMap;
    private Model defaultGraph;
    private ArrayList<PredicateObjectMap> predicateObjectMaps;

    public LogicalTable getLogicalTable() {
        return logicalTable;
    }

    public SubjectMap getSubjectMap() {
        return subjectMap;
    }

    public ArrayList<Model> getAllRelatedGraphs() {
        return null;
    }

    public Model getDefaultGraph() {
        return defaultGraph;
    }

    public List<PredicateObjectMap> getPredicateObjectMaps() {
        return predicateObjectMaps;
    }
}
