package org.sleepless_artery.auth_service.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sleepless_artery.auth_service.exception.RoleNotFoundException;
import org.sleepless_artery.auth_service.model.Role;
import org.sleepless_artery.auth_service.repository.RoleRepository;
import org.sleepless_artery.auth_service.service.RoleService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Slf4j
@Service
@AllArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;


    @Override
    @Cacheable(value = "roles", key = "#roleName")
    public Optional<Role> findRoleByName(String roleName) {
        log.info("Getting role by name: {}", roleName);

        return Optional.ofNullable(roleRepository.findByRoleName(roleName).orElseThrow(() -> {
            log.warn("Role not found: {}", roleName);
            return new RoleNotFoundException();
        }));
    }
}