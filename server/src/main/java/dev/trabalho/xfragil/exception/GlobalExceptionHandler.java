
package dev.trabalho.xfragil.exception;

import dev.trabalho.xfragil.exception.customExceptions.DuplicatedObjectException;
import dev.trabalho.xfragil.exception.customExceptions.InactiveUserException;
import dev.trabalho.xfragil.exception.customExceptions.InvalidCredentialsException;
import dev.trabalho.xfragil.exception.customExceptions.InvalidEnumException;
import dev.trabalho.xfragil.exception.customExceptions.ObjectNotFoundException;
import jakarta.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> validationExceptionHandler(MethodArgumentNotValidException ex){
        
        List<MyFieldError> fieldErrors = new ArrayList<>();
        Map<String, Object> requestErrors = new HashMap<>();
        
        //selecionando todos os campos com erros da exceção e inserindo na lista "fieldErrors"
        ex.getBindingResult().getFieldErrors().forEach(error -> {
        fieldErrors.add(new MyFieldError(error));            
        });
        
        requestErrors.put("status", HttpStatus.BAD_REQUEST.value());
        requestErrors.put("timestamp", LocalDateTime.now().withNano(0));
        requestErrors.put("error", HttpStatus.BAD_REQUEST.getReasonPhrase());
        requestErrors.put("message", "1 ou mais campos inválidos");
        requestErrors.put("field_errors", fieldErrors);
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(requestErrors);
    }
    
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> constraintViolationExceptionHandler (ConstraintViolationException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage()));
    }

    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<ErrorResponse> objectNotFoundExceptionHandler (ObjectNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage()));
    }

    @ExceptionHandler(DuplicatedObjectException.class)
    public ResponseEntity<ErrorResponse> duplicatedObjectExceptionHandler (DuplicatedObjectException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(HttpStatus.CONFLICT.value(), ex.getMessage()));
    }
    
    @ExceptionHandler(InvalidEnumException.class)
    public ResponseEntity<ErrorResponse> InvalidEnumExceptionHandler (InvalidEnumException ex){      
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage()));
    } 
    
    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorResponse> invalidCredentialsExceptionHandler (InvalidCredentialsException ex){      
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), ex.getMessage()));
    } 
    
    @ExceptionHandler(InactiveUserException.class)
    public ResponseEntity<ErrorResponse> inactiveUserExceptionHandler (InactiveUserException ex){      
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), ex.getMessage()));
    } 
    
}
