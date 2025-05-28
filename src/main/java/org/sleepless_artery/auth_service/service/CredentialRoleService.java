package org.sleepless_artery.auth_service.service;


public interface CredentialRoleService {

    void addRoleToUser(String emailAddress, String roleName);

    void deleteRoleForUser(String emailAddress, String roleName);
}
