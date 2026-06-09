
package dev.trabalho.xfragil.utils.enums;


import com.fasterxml.jackson.annotation.JsonCreator;
import dev.trabalho.xfragil.exception.customExceptions.InvalidEnumException;

public enum Answer {
    
    SIM, NAO, NAO_SEI;

    @JsonCreator
    public static Answer from(String answer){
        try {
            return Answer.valueOf(answer.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidEnumException("Tipo de resposta inválida: " + answer);
        }                       
    }  

}

