package org.sleepless_artery.auth_service.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.sleepless_artery.auth_service.dto.CredentialDto;
import org.sleepless_artery.auth_service.service.EmailChangeService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("${basic-request-path}")
@Validated
public class EmailChangeController {

    private final EmailChangeService emailChangeService;


    @PostMapping("/change-email-address")
    public ResponseEntity<Void> changeEmailAddress(
            @Valid @RequestBody CredentialDto credentialDto,
            @RequestParam @NotBlank String emailAddress
    ) {
        emailChangeService.changeEmailAddress(credentialDto, emailAddress);
        return ResponseEntity.ok().build();
    }


    @PostMapping("/confirm-email-address")
    public ResponseEntity<Void> confirmEmailAddress(
            @Valid @RequestBody CredentialDto credentialDto,
            @RequestParam @NotBlank String emailAddress,
            @RequestParam @NotBlank String confirmationCode
    ) {
        emailChangeService.confirmEmailAddress(credentialDto.getEmailAddress(), emailAddress, confirmationCode);
        return ResponseEntity.ok().build();
    }
}
