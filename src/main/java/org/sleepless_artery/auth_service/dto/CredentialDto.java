package org.sleepless_artery.auth_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class CredentialDto {

    @NotBlank(message = "Email address cannot be blank")
    @Size(min = 1, max = 50, message = "Username must be between 1 and 50 characters")
    @Email(message = "Enter a correct email address")
    private String emailAddress;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[A-Z])(?=.*[a-z]).*$",
            message = "Password must contain lowercase and uppercase letters and at least one digit")
    private String password;
}
