package com.flashcards.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class AppUserLoginDTO {
    private String username;
    private String password;
}
