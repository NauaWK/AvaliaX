
package dev.trabalho.xfragil.exception.customExceptions;

public class ExpiredTokenException extends RuntimeException {
    
    public ExpiredTokenException(String msg) {
        super(msg);
    }
}
