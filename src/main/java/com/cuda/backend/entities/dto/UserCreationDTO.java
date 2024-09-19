package com.cuda.backend.entities.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UserCreationDTO {
    @Size(max = 10,message = "username should be betweent 5-10")
    @NotBlank
    String username;

    @Email
    @NotNull(message="email cannot be blank")
    String email;

    @NotBlank
    String password;

    @NotBlank
    @Size(max = 50,message = "bio should be under 40")
    String bio;
}
