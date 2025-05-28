package org.sleepless_artery.auth_service.repository;

import org.sleepless_artery.auth_service.model.Credential;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface CredentialRepository extends JpaRepository<Credential, Long> {

    Optional<Credential> findByEmailAddress(String emailAddress);

    boolean existsByEmailAddress(String emailAddress);

    void deleteByEmailAddress(String emailAddress);
}
