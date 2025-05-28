package org.sleepless_artery.auth_service.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sleepless_artery.auth_service.exception.RoleNotFoundException;
import org.sleepless_artery.auth_service.model.Credential;
import org.sleepless_artery.auth_service.model.Role;
import org.sleepless_artery.auth_service.service.CredentialRoleService;
import org.sleepless_artery.auth_service.service.CredentialService;
import org.sleepless_artery.auth_service.service.RoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@RequiredArgsConstructor
public class CredentialRoleServiceImpl implements CredentialRoleService {

    private final CredentialService credentialService;
    private final RoleService roleService;


    @Override
    @Transactional
    public void addRoleToUser(String emailAddress, String roleName) {
        log.info("Adding role '{}' to user with email address '{}'", roleName, emailAddress);

        Credential credential = credentialService.findCredentialByEmailAddress(emailAddress);

        Role role = roleService.findRoleByName(roleName).orElseThrow(() -> {
            log.warn("Role '{}' not found", roleName);
            return new RoleNotFoundException();
        });
        credential.getRoles().add(role);

        credentialService.save(credential);
    }


    @Override
    @Transactional
    public void deleteRoleForUser(String emailAddress, String roleName) {
        log.info("Deleting role '{}' for user with email address '{}'", roleName, emailAddress);

        Credential credential = credentialService.findCredentialByEmailAddress(emailAddress);

        Role role = roleService.findRoleByName(roleName).orElseThrow(() -> {
            log.warn("Role '{}' not found", roleName);
            return new RoleNotFoundException();
        });
        credential.getRoles().remove(role);

        credentialService.save(credential);
    }
}
