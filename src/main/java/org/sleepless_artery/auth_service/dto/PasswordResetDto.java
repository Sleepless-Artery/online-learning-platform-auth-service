package org.sleepless_artery.auth_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;


@Builder
@Getter
public class PasswordResetDto {

    @NotBlank(message = "Email address cannot be blank")
    @Size(min = 1, max = 255, message = "Email address must be between 1 and 255 characters")
    @Email(message = "Enter a correct email address")
    private String emailAddress;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters")
    @Pattern(regexp = "^(?=.*[A-Za-zА-Яа-я])(?=.*[0-9])(?=.*[A-ZА-Я])(?=.*[a-zа-я]).*$",
            message = "Password must contain at least one lowercase letter, one uppercase letter and one digit")
    private String password;

    @NotBlank(message = "Confirm your password")
    private String confirmationPassword;
}
