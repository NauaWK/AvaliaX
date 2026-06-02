package dev.trabalho.xfragil.utils.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import dev.trabalho.xfragil.exception.customExceptions.InvalidEnumException;

public enum Role {
    ADMIN, USER;

    //tentativa de conversão de string do JSON para enum, permitindo maior flexibilidade na sintaxe
    @JsonCreator
    public static Role from(String role){
        try {
            return Role.valueOf(role.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidEnumException("Tipo de perfil inválido: " + role);
        }                       
    }  

}
