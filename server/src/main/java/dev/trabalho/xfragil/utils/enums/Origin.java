
package dev.trabalho.xfragil.utils.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import dev.trabalho.xfragil.exception.customExceptions.InvalidEnumException;

public enum Origin {
    
    RESPONSAVEL, PROFISSIONAL;

    @JsonCreator
    public static Origin from(String origin){
        try {
            return Origin.valueOf(origin.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidEnumException("Tipo de origem de avaliação inválida: " + origin);
        }                       
    }  

}
