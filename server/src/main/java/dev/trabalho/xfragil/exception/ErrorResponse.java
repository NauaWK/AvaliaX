
package dev.trabalho.xfragil.exception;

import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;

public class ErrorResponse {
    
    private int status;
    private String error;
    private String message;
    private LocalDateTime timestamp;

    public ErrorResponse(int status, String message) {
        this.status = status;
        this.error = HttpStatus.valueOf(status).getReasonPhrase();
        this.message = message;
        this.timestamp = LocalDateTime.now().withNano(0);
    }   
    
    public int getStatus(){
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
    
    public String getError(){
        return error;
    }
    
    public void setError(String error) {
        this.error = error;
    }
    
    public LocalDateTime getTimeStamp(){
        return timestamp;
    }
    
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
}
