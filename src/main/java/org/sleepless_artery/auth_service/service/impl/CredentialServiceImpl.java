package org.sleepless_artery.auth_service.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sleepless_artery.auth_service.dto.RegistrationCredentialDto;
import org.sleepless_artery.auth_service.exception.CredentialAlreadyExistsException;
import org.sleepless_artery.auth_service.exception.CredentialNotFoundException;
import org.sleepless_artery.auth_service.exception.GrpcProcessingException;
import org.sleepless_artery.auth_service.exception.RoleNotFoundException;
import org.sleepless_artery.auth_service.grpc.client.UserCreationServiceGrpcClient;
import org.sleepless_artery.auth_service.model.Credential;
import org.sleepless_artery.auth_service.model.Role;
import org.sleepless_artery.auth_service.repository.CredentialRepository;
import org.sleepless_artery.auth_service.service.CredentialService;
import org.sleepless_artery.auth_service.service.RoleService;
import org.sleepless_artery.auth_service.service.AuthCacheService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class CredentialServiceImpl implements CredentialService {

    private final CredentialRepository credentialRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    private final UserCreationServiceGrpcClient grpcClient;
    private final AuthCacheService authCacheService;


    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "userDetails", key = "#emailAddress")
    public UserDetails loadUserByUsername(String emailAddress) throws UsernameNotFoundException {
        log.info("Loading user with email address: {}", emailAddress);

        Credential credential = credentialRepository.findByEmailAddress(emailAddress)
                .orElseThrow(() -> {
                    log.warn("User not found with email address: {}", emailAddress);
                    return new CredentialNotFoundException();
                });

        Set<SimpleGrantedAuthority> authorities = credential.getRoles() == null
                ? Set.of()
                : credential.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getRoleName()))
                .collect(Collectors.toSet());

        return new User(
                credential.getEmailAddress(),
                credential.getPasswordHash(),
                authorities
        );
    }


    @Override
    @Transactional
    public Credential createCredential(RegistrationCredentialDto registrationCredentialDto) {
        log.info("Creating credential: {}", registrationCredentialDto);

        if (credentialRepository.existsByEmailAddress(registrationCredentialDto.getEmailAddress())) {
            log.warn("Credential already exists with email address: {}", registrationCredentialDto.getEmailAddress());
            throw new CredentialAlreadyExistsException("Credential already exists");
        }

        Role role = roleService.findRoleByName("USER").orElseThrow(() -> {
            log.warn("Role 'USER' not found");
            return new RoleNotFoundException();
        });

        Credential credential = Credential.builder()
                .emailAddress(registrationCredentialDto.getEmailAddress())
                .passwordHash(passwordEncoder.encode(registrationCredentialDto.getPassword()))
                .roles(Set.of(role))
                .build();

        if (!grpcClient.createUser(credential.getEmailAddress())) {
            log.warn("Error creating user: {}", credential.getEmailAddress());
            throw new GrpcProcessingException("Error creating user on the grpc server side");
        }

        Credential savedCredential = credentialRepository.save(credential);
        log.info("Successfully created credential for email: {}", savedCredential.getEmailAddress());

        authCacheService.putUserCache(savedCredential.getEmailAddress(), savedCredential);

        return savedCredential;
    }


    @Override
    @Transactional
    @Cacheable(value = "credentials", key = "#emailAddress")
    public Credential findCredentialByEmailAddress(String emailAddress) {
        return credentialRepository.findByEmailAddress(emailAddress)
                .orElseThrow(() -> {
                    log.warn("Credential not found with email address: {}", emailAddress);
                    return new CredentialNotFoundException();
                });
    }


    @Override
    @Transactional
    public boolean existsByEmailAddress(String emailAddress) {
        log.info("Checking if credential with email address '{}' exists", emailAddress);
        if (authCacheService.existsCredential(emailAddress)) {
            return true;
        }
        return credentialRepository.existsByEmailAddress(emailAddress);
    }


    @Override
    @Transactional
    public Credential save(Credential credential) {
        log.info("Saving credential: {}", credential);
        return credentialRepository.save(credential);
    }


    @Override
    @Transactional
    public void changeEmailAddress(String oldEmailAddress, String newEmailAddress) {
        log.info("Changing email address for credential with email address '{}'", oldEmailAddress);

        if (!credentialRepository.existsByEmailAddress(oldEmailAddress)) {
            log.warn("Credential not found with : email address '{}'", oldEmailAddress);
            throw new CredentialNotFoundException();
        }

        Credential credential = findCredentialByEmailAddress(oldEmailAddress);
        credential.setEmailAddress(newEmailAddress);

        Credential updatedCredential = save(credential);

        authCacheService.evictUserCache(oldEmailAddress);
        authCacheService.putUserCache(newEmailAddress, updatedCredential);
    }


    @Override
    @Transactional
    public void deleteByEmailAddress(String email) {
        log.info("Deleting credential: {}", email);
        authCacheService.evictUserCache(email);
        credentialRepository.deleteByEmailAddress(email);
    }
}
