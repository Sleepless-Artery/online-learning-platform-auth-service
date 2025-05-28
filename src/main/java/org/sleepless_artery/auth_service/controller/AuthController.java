package org.sleepless_artery.auth_service.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.sleepless_artery.auth_service.dto.CredentialDto;
import org.sleepless_artery.auth_service.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("${basic-request-path}")
public class AuthController {

    private final AuthService authService;


    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@Valid @RequestBody CredentialDto credentialDto) {
        return ResponseEntity.ok(authService.createAuthenticationToken(credentialDto));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestParam @NotBlank String emailAddress) {
        authService.logout(emailAddress);
        return ResponseEntity.ok().build();
    }
}