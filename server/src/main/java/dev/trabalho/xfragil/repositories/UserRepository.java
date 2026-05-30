package dev.trabalho.xfragil.repositories;

import dev.trabalho.xfragil.entities.Users;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Integer> {

    Optional<Users> findByLogin(String login);
    
    List<Users> findByActiveTrue();
    
    boolean existsByLogin(String login);

}
