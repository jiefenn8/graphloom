package r2graph.exceptions;

import r2graph.exceptions.base.FeijoaException;

public class InvalidRuleClassException extends FeijoaException {

    public InvalidRuleClassException(String property){super(property); }
}
