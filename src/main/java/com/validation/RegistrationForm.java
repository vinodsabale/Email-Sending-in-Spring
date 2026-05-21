package com.validation;
 

import jakarta.validation.constraints.*;
import lombok.Data;

/**
 * Assignment 4 — Form Model
 *
 * Bean Validation annotations enforce rules at the controller layer.
 * @NotBlank  → field must not be empty
 * @Email     → must be a valid email format
 * @Pattern   → regex match (used for phone number)
 * @Size      → min/max length constraints
 */
@Data
public class RegistrationForm {

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 60, message = "Name must be between 2 and 60 characters")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Please enter a valid email address")
    private String email;

    @NotBlank(message = "Phone number is required")
    @Pattern(
        regexp = "^[6-9]\\d{9}$",
        message = "Enter a valid 10-digit Indian mobile number (starts with 6-9)"
    )
    private String phno;
}