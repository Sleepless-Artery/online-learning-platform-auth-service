package org.sleepless_artery.auth_service.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.sleepless_artery.auth_service.dto.PasswordResetDto;
import org.sleepless_artery.auth_service.service.PasswordResetService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("${basic-request-path}")
public class PasswordResetController {

    private final PasswordResetService passwordResetService;


    @PostMapping("/forgot-password")
    public ResponseEntity<Void> forgotPassword(@RequestParam @NotBlank String emailAddress) {
        passwordResetService.initiatePasswordReset(emailAddress);
        return ResponseEntity.ok().build();
    }


    @PostMapping("/validate-reset-code")
    public ResponseEntity<Void> validateResetCode(
            @RequestParam @NotBlank String emailAddress,
            @RequestParam @NotBlank String resetCode
    ) {
        boolean isValid = passwordResetService.validatedResetCode(emailAddress, resetCode);
        return isValid ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }


    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(@Valid @RequestBody PasswordResetDto passwordResetDto) {
        passwordResetService.completePasswordReset(passwordResetDto);
        return ResponseEntity.ok().build();
    }
}
