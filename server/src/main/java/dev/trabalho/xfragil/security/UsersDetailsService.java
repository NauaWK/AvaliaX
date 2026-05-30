package dev.trabalho.xfragil.security;

import dev.trabalho.xfragil.entities.Users;
import dev.trabalho.xfragil.exception.customExceptions.InactiveUserException;
import dev.trabalho.xfragil.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UsersDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public UsersDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Users user = userRepository.findByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário " + login + " não encontrado!"));

        if (!user.isActive()) {
            throw new InactiveUserException("Usuário " + login + " está inativo e não pode logar!");
        }
        
        return new UserDetailsImpl(user);
    }

}
