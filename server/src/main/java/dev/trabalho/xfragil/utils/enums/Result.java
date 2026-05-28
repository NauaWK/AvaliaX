
package dev.trabalho.xfragil.utils.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import dev.trabalho.xfragil.exception.customExceptions.InvalidRoleException;

public enum Result {
    INCONCLUSIVO, TESTE_INDICADO;

    //tentativa de conversão de string do JSON para enum, permitindo maior flexibilidade na sintaxe
    @JsonCreator
    public static Result from(String result){
        try {
            return Result.valueOf(result.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidRoleException("Tipo de recomendação inválida: " + result);
        }                       
    }  
    
    
    
}
