package org.sleepless_artery.auth_service.service;

import org.sleepless_artery.auth_service.model.Role;

import java.util.Optional;


public interface RoleService {

    Optional<Role> findRoleByName(String roleName);
}
