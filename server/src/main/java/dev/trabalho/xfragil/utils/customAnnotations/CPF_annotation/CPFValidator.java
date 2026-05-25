
package dev.trabalho.xfragil.utils.customAnnotations.CPF_annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CPFValidator implements ConstraintValidator<CPF, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return false;
        }

        String cpf = value.replaceAll("\\D", "");

        if (cpf.length() != 11) return false;

        if (cpf.matches("(\\d)\\1{10}")) return false;

        return verifyCpfDigits(cpf);
    }
    
    public boolean verifyCpfDigits(String cpf) {

        for (int i = 0; i < 2; i++) {
            int soma = 0;
            for (int j = 0; j < 9 + i; j++) {
                soma += (cpf.charAt(j) - '0') * (10 + i - j);
            }
            int digito = soma % 11 < 2 ? 0 : 11 - (soma % 11);
            if (digito != (cpf.charAt(9 + i) - '0')) return false;
        }

        return true;
    }
    
}
    

