package r2graph.exceptions;

import r2graph.exceptions.base.FeijoaException;

public class InvalidMappingDocumentException extends FeijoaException {
    /**
     * The MappingDocument is not valid.
     */
    public InvalidMappingDocumentException(String document) {
        super(document);
    }
}
