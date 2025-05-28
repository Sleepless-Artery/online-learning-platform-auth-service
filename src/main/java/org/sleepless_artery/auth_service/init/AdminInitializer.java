package org.sleepless_artery.auth_service.init;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sleepless_artery.auth_service.exception.RoleNotFoundException;
import org.sleepless_artery.auth_service.model.Credential;
import org.sleepless_artery.auth_service.model.Role;
import org.sleepless_artery.auth_service.repository.CredentialRepository;
import org.sleepless_artery.auth_service.service.RoleService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;


@Slf4j
@Component
@RequiredArgsConstructor
public class AdminInitializer {

    private final CredentialRepository credentialRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    @Value("${admin.email-address}")
    private String adminEmailAddress;

    @Value("${admin.password}")
    private String adminPassword;


    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void initAdminUser() {
        log.info("Initializing Admin User");
        if (!credentialRepository.existsByEmailAddress(adminEmailAddress)) {
            Role adminRole = roleService.findRoleByName("ADMIN").orElseThrow(() -> {
                log.error("Admin Role not found");
                return new RoleNotFoundException();
            });

            Credential adminCredential = Credential.builder()
                    .emailAddress(adminEmailAddress)
                    .passwordHash(passwordEncoder.encode(adminPassword))
                    .roles(Set.of(adminRole))
                    .build();

            credentialRepository.save(adminCredential);
            log.info("Admin User created");
        }
    }
}