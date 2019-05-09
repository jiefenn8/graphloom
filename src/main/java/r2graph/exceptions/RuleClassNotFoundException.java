package r2graph.exceptions;

import r2graph.exceptions.base.FeijoaValidatorException;

public class RuleClassNotFoundException extends FeijoaValidatorException {

    /**
     * The rule class does not exist.
     */
    public RuleClassNotFoundException(String rule){
        super(rule);
    }
}
