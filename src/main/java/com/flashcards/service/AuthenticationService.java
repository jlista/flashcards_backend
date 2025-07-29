package com.flashcards.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.flashcards.model.AppUser;
import com.flashcards.model.DTO.AppUserLoginDTO;
import com.flashcards.model.DTO.AppUserRegisterDTO;
import com.flashcards.repository.AppUserRepository;

@Service
public class AuthenticationService {
    
    final static Logger logger = LoggerFactory.getLogger(AuthenticationService.class);
    private final AppUserRepository userRepository;
    
    private final PasswordEncoder passwordEncoder;
    
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(
        AppUserRepository userRepository,
        AuthenticationManager authenticationManager,
        PasswordEncoder passwordEncoder
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public AppUser signup(AppUserRegisterDTO input) {
        AppUser user = new AppUser();

        user.setUsername(input.getUsername());
        user.setEmail(input.getEmail());
        user.setPassword(passwordEncoder.encode(input.getPassword()));
        user.set_admin(false);

        return userRepository.save(user);
    }

    public AppUser authenticate(AppUserLoginDTO input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getUsername(),
                        input.getPassword()
                )
        );

        return userRepository.findByUsername(input.getUsername())
                .orElseThrow();
    }

    public AppUser getCurrentUser(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Optional<AppUser> currentUser = userRepository.findByUsername(username);
        return currentUser.get();
    }

    public boolean isOwnerOrAdmin(Long userId){
        // Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        // String username = auth.getName();
        // AppUser currentUser = userRepository.findByUsername(username).get();
        // logger.info(currentUser.toString());
        // return currentUser.is_admin() || currentUser.getUserId().equals(userId);
        return true;
    }
}