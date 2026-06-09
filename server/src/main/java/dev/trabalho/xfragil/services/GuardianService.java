package dev.trabalho.xfragil.services;

import dev.trabalho.xfragil.entities.Guardian;
import dev.trabalho.xfragil.entities.dto.guardian_dtos.GuardianRequestDTO;
import dev.trabalho.xfragil.repositories.GuardianRepository;
import dev.trabalho.xfragil.utils.mappers.GuardianMapper;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class GuardianService {
    
    private final GuardianRepository guardianRepo;
    private final GuardianMapper guardianMapper;

    public GuardianService(GuardianRepository guardianRepo, GuardianMapper guardianMapper) {
        this.guardianRepo = guardianRepo;
        this.guardianMapper = guardianMapper;
    }
    
    public Guardian createOrFind(GuardianRequestDTO dto)
    {
        String normalizedCpf = dto.CPF_responsavel().replaceAll("\\D", "");
        Optional<Guardian> optionalGuardian = guardianRepo.findByCPF(normalizedCpf);
        Guardian g;
        if(optionalGuardian.isPresent()){
            g = optionalGuardian.get();
        }
        else{
           g = guardianMapper.toGuardian(dto);
           g.setCPF(normalizedCpf);
           guardianRepo.save(g);
        }
        return g;
    }
    
    public boolean guardianAlreadyExists(String CPF){
        return guardianRepo.existsByCPF(CPF);
    }
    
}
