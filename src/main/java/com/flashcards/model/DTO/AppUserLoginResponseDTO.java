package com.flashcards.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class AppUserLoginResponseDTO {
    private String token;
    private Long expiresIn;
    private Long userId;
}
