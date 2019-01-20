import org.apache.jena.iri.IRI;
import org.apache.jena.rdf.model.Model;

import java.util.ArrayList;

public class SubjectMap {
    private Model graphMap;
    private String template;
    private ArrayList<IRI> classes;

    public void setGraphMap(Model model) {
        graphMap = model;
    }

    public Model getGraph() {
        return graphMap;
    }

    public String getTemplate() {
        return template;
    }

    public ArrayList<IRI> getClasses() {
        return classes;
    }

    public String getClassType() {
        return null;
    }
}
