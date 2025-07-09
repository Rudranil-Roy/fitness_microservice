package com.fitness.gateway.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank(message = "email required")
    @Email(message = "Email not valid")
    private String email;

    private String keyCloakId;
    @NotBlank(message = "password required")
    @Size(min = 6, message = "Password word must have at least 6 characters ")
    private String password;
    private String firstName;
    private String lastName;
}

