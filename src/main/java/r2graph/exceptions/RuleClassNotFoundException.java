package r2graph.exceptions;

public class RuleClassNotFoundException extends RuntimeException {

    /**
     * The rule class does not exist.
     */
    public RuleClassNotFoundException(String rule){
        super(rule);
    }
}
