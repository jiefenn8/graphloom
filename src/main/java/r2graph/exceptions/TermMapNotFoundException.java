package r2graph.exceptions;

import r2graph.exceptions.base.FeijoaValidatorException;

public class TermMapNotFoundException extends FeijoaValidatorException {

    /**
     * The term map does not exist.
     */
    public TermMapNotFoundException(String termMap){
        super(termMap);
    }
}
