package hanu.gdsc.domain.core_order.exceptions;

import hanu.gdsc.domain.share.exceptions.BusinessLogicException;

public class InsufficientQuantityException extends BusinessLogicException {
    public InsufficientQuantityException(String message) {
        super(message, "INSUFFICIENT_QUANTITY");
    }
}
