package com.cuda.backend.entities.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class TweetCreationDTO {
    @NotBlank
    @Size(max=50,message="tweet cannot exceed 50")
    private String tweet; 
}
