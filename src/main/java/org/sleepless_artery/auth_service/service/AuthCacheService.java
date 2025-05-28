package org.sleepless_artery.auth_service.service;

import org.sleepless_artery.auth_service.model.Credential;


public interface AuthCacheService {

    void evictUserCache(String emailAddress);

    void putUserCache(String emailAddress, Credential credential);

    boolean existsCredential(String emailAddress);
}
