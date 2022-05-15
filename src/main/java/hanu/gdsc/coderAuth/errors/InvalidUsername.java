package hanu.gdsc.coderAuth.errors;

import hanu.gdsc.share.error.BusinessLogicError;

public class InvalidUsername extends BusinessLogicError { 
    public InvalidUsername(String message) {
        super(message, "INVALID_USERNAME");
    }    
}
