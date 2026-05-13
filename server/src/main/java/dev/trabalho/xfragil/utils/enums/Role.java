package dev.trabalho.xfragil.utils.enums;

public enum Role {
    ADMIN, USER;

    Role convertToRole(String s){
        try{
            return Role.valueOf(s.toUpperCase());
        } catch (Exception e){
            return null;
        }
    }

}
