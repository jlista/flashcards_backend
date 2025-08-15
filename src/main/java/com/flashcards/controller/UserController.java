package com.flashcards.controller;

import com.flashcards.model.AppUser;
import com.flashcards.model.DTO.AppUserLoginDTO;
import com.flashcards.model.DTO.AppUserLoginResponseDTO;
import com.flashcards.model.DTO.AppUserRegisterDTO;
import com.flashcards.service.AuthenticationService;
import com.flashcards.service.JwtService;
import com.flashcards.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final String COOKIE_NAME = "AUTH_TOKEN";

    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationService authenticationService;


    public UserController(UserService userService, JwtService jwtService,
            AuthenticationService authenticationService) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @GetMapping()
    public ResponseEntity<AppUser> getUser(@RequestParam String username) {

        Optional<AppUser> user = userService.getUserByName(username);

        if (user.isPresent()) {
            return new ResponseEntity<>(user.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @PostMapping("/signup")
    public ResponseEntity<AppUser> register(@RequestBody AppUserRegisterDTO registerUserDto) {
        AppUser registeredUser = authenticationService.signup(registerUserDto);

        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<AppUserLoginResponseDTO> authenticate(@RequestBody AppUserLoginDTO appUserLoginDTO, HttpServletResponse response) {
        AppUser authenticatedUser = authenticationService.authenticate(appUserLoginDTO);

        String jwtToken = jwtService.generateToken(authenticatedUser);
        Long expiresIn = jwtService.getExpirationTime();
        Long userId = authenticatedUser.getUserId();

        AppUserLoginResponseDTO loginResponse =
                new AppUserLoginResponseDTO(jwtToken, expiresIn, userId);

        jwtService.setJwtCookie(jwtToken, response);
        return ResponseEntity.ok(loginResponse);
    }
}

