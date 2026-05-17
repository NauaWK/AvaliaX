package dev.trabalho.xfragil.utils.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import dev.trabalho.xfragil.exception.customExceptions.InvalidRoleException;

public enum Role {
    ADMIN, USER;

    //tentativa de conversão de string do JSON para enum, permitindo maior flexibilidade na sintaxe
    @JsonCreator
    public static Role from(String movimentacao){
        try {
            return Role.valueOf(movimentacao.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidRoleException("Tipo de perfil inválido: " + movimentacao);
        }                       
    }  

}
