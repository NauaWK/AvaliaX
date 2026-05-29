
package dev.trabalho.xfragil.exception;

import org.springframework.validation.FieldError;

public class MyFieldError {
    
    private String field;
    private String defaultErrorMessage;

    public MyFieldError(FieldError er) {
        this.field = er.getField();
        this.defaultErrorMessage = er.getDefaultMessage();
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getDefaultErrorMessage() {
        return defaultErrorMessage;
    }

    public void setDefaultErrorMessage(String defaultErrorMessage) {
        this.defaultErrorMessage = defaultErrorMessage;
    }
    
}
