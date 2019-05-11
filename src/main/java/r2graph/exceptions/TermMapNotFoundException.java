package r2graph.exceptions;

public class TermMapNotFoundException extends RuntimeException {

    /**
     * The term map does not exist.
     */
    public TermMapNotFoundException(String termMap){
        super(termMap);
    }
}
