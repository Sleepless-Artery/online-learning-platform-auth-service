package org.sleepless_artery.auth_service.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.sleepless_artery.auth_service.dto.RegistrationCredentialDto;
import org.sleepless_artery.auth_service.service.RegistrationService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("${basic-request-path}")
@Validated
public class RegistrationController {

    private final RegistrationService registrationService;

    @PostMapping("/register")
    public ResponseEntity<Void> registerUser(@Valid @RequestBody RegistrationCredentialDto credentialDto) {
        registrationService.startRegistration(credentialDto);
        return ResponseEntity.ok().build();
    }


    @PostMapping("/confirm-registration")
    public ResponseEntity<?> confirmRegistration(
            @Valid @RequestBody RegistrationCredentialDto credentialDto,
            @RequestParam @NotBlank String confirmationCode
    ) {
        return ResponseEntity.ok(registrationService.confirmRegistration(credentialDto, confirmationCode));
    }
}
