package com.github.jiefenn8.graphloom.rdf.r2rml;

import com.github.jiefenn8.graphloom.api.Record;
import org.apache.jena.rdf.model.RDFNode;

public interface TermMap {

    /**
     * Returns a generated RDF Term using the provided entity
     * properties collections. The type of RDF term returned
     * depends on the class {@code TermMapType}.
     *
     * @param entityProps the entity properties collection to use.
     * @return the RDF term generated.
     */
    RDFNode generateRDFTerm(Record entityProps);

    /**
     * The TermType to return generated term as.
     */
    enum TermType {
        IRI, BLANK, LITERAL
    }
}
