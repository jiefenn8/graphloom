package com.github.jiefenn8.graphloom.exceptions;

import com.github.jiefenn8.graphloom.exceptions.base.GraphLoomException;

public class InvalidMappingDocumentException extends GraphLoomException {
    /**
     * The MappingDocument is not valid.
     */
    public InvalidMappingDocumentException(String document) {
        super(document);
    }
}
