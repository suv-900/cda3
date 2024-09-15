package com.cuda.backend.entities.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreationDTO {
    @Size(max = 10,min = 5,message = "username should be betweent 5-10")
    @NotBlank
    String username;

    @Email
    String email;

    @Size(max = 10,min = 5,message = "password should be betweent 5-10")
    @NotBlank
    String password;
}
