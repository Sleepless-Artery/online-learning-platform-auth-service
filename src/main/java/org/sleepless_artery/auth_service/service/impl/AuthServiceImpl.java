package org.sleepless_artery.auth_service.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sleepless_artery.auth_service.dto.CredentialDto;
import org.sleepless_artery.auth_service.dto.JwtResponse;
import org.sleepless_artery.auth_service.exception.BadCredentialException;
import org.sleepless_artery.auth_service.jwt.JwtTokenUtils;
import org.sleepless_artery.auth_service.service.AuthService;
import org.sleepless_artery.auth_service.service.CredentialService;
import org.sleepless_artery.auth_service.service.AuthCacheService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final CredentialService credentialService;
    private final AuthCacheService authCacheService;

    private final JwtTokenUtils jwtTokenUtils;
    private final AuthenticationManager authenticationManager;


    @Override
    @Cacheable(value = "tokens", key = "#credentialDto.emailAddress", unless = "#result == null")
    public JwtResponse createAuthenticationToken(CredentialDto credentialDto) {
        log.info("Creating authentication token");

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            credentialDto.getEmailAddress(),
                            credentialDto.getPassword()
                    )
            );
        } catch (BadCredentialsException exception) {
            log.warn("Error creating authentication token: {}", exception.getMessage());
            throw new BadCredentialException("Incorrect login or password");
        }

        UserDetails userDetails = credentialService.loadUserByUsername(credentialDto.getEmailAddress());
        String token = jwtTokenUtils.generateToken(userDetails);

        log.info("Successfully generated token for email address: {}", credentialDto.getEmailAddress());

        return new JwtResponse(token);
    }


    @Override
    public void logout(String emailAddress) {
        if (credentialService.existsByEmailAddress(emailAddress)) {
            authCacheService.evictUserCache(emailAddress);
        }
    }
}