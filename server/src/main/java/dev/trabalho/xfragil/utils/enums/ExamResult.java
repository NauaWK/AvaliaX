
package dev.trabalho.xfragil.utils.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import dev.trabalho.xfragil.exception.customExceptions.InvalidEnumException;


public enum ExamResult {
    MUTACAO_COMPLETA,
    PRE_MUTACAO,
    ZONA_GRAY,
    MOSAICISMO,
    NEGATIVO_XF,
    NAO_SEI;
    
    @JsonCreator
    public static ExamResult from(String examResult){
        try {
            return ExamResult.valueOf(examResult.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidEnumException("Tipo de resultado de exame inválido: " + examResult);
        }                       
    }  
}
