import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.RDF;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Process R2RML map and Input Database to RDF Triples.
 */
public class Processor {
    private final static Logger LOGGER = LoggerFactory.getLogger(Processor.class.getName());

    private final Pattern pattern = Pattern.compile("\\{(.*?)\\}");

    private Model rdfGraph;
    private MappingDocument r2rmlDoc;// = new MappingDocument();
    private boolean cancelled;

    /**
     * Main mapping function converting RDF SQL database data to RDF triples using R2RML Mapping document.
     *
     * @param input       of the sql database to send query to retrieve data.
     * @param r2rmlStream the mapping document to use to map the data from the database into RDF triples.
     */
    public void mapToGraph(InputDatabase input, InputStream r2rmlStream) {

        rdfGraph = ModelFactory.createDefaultModel();
        R2RMLMapping r2rmlMapping = r2rmlDoc.load(r2rmlStream);
        Map<String, TriplesMap> triplesMaps = r2rmlMapping.getTriplesMaps();

        triplesMaps.forEach((key, triplesMap) -> {
            SubjectMap sm = triplesMap.getSubjectMap();
            String template = sm.getTemplate();
            String rClass = sm.getClassType();

            //info: <ColumnName, ColumnData> for that single row of rows.
            for (Map<String, String> row : input.getRows(triplesMap.getLogicalTable().getTableName())) {
                if (cancelled) break;

                String subjectURI = "";
                Matcher matcher = pattern.matcher(template);
                if (matcher.find()) {
                    subjectURI = template.replace(matcher.group(0), row.get(matcher.group(1)));
                }

                if (subjectURI.isEmpty()) break;

                //Create initial resource and add class type property
                Resource subject = rdfGraph.createResource(subjectURI).addProperty(RDF.type, rClass);

                //For each column data of row.
                triplesMap.getPredicateObjectMaps().forEach((pom) -> {
                    Property predicate = rdfGraph.createProperty(pom.getPredicateMap().getPredicate());
                    RDFNode object = rdfGraph.createLiteral(row.get(pom.getObjectMap().getColumn()));

                    subject.addProperty(predicate, object);
                });
            }
        });
        LOGGER.info("Mapping complete");
    }

    private boolean r2rmlMatchesDatabase(MappingDocument r2rmlDoc) {
        return false;
    }

    /**
     * @return the Model containing the output data-set graph.
     */
    public Model getGraph() {
        return rdfGraph;
    }

    /**
     * @return the MappingDocument contained the loaded r2rml mapping graph.
     */
    public MappingDocument getMappingDocument() {
        return r2rmlDoc;
    }
}
