
package dev.trabalho.xfragil.entities.dto.assessment_dtos;

import dev.trabalho.xfragil.utils.enums.Origin;
import dev.trabalho.xfragil.utils.enums.Result;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record AssessmentOverviewDTO(
        
        String profissional,
        
        Origin origem,
        
        LocalDate data,
        
        BigDecimal score,
        
        Result resultado,
        
        String detalhes,
        
        List<String> sintomas
        
    ) {}
