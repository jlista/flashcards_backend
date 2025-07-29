package com.flashcards.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.flashcards.model.AppUser;
import com.flashcards.repository.AppUserRepository;

@Service
public class UserService {

    final static Logger logger = LoggerFactory.getLogger(UserService.class);

    private final AppUserRepository appUserRepository;

    public UserService(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    public Optional<AppUser> getUserId(String username) {
        Optional<AppUser> foo = appUserRepository.findByUsername(username);
        logger.info(foo.toString());
        return appUserRepository.findByUsername(username);
    }
}