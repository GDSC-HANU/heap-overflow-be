package hanu.gdsc.share.exceptions;

public class InvalidStateException extends BusinessLogicException {
    public InvalidStateException(String message) {
        super(message, "INVALID_STATE");
    }
}
