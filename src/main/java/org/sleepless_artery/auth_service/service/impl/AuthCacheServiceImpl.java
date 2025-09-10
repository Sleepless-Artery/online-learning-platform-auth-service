package org.sleepless_artery.auth_service.service.impl;

import lombok.RequiredArgsConstructor;
import org.sleepless_artery.auth_service.model.Credential;
import org.sleepless_artery.auth_service.service.AuthCacheService;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class AuthCacheServiceImpl implements AuthCacheService {

    private final CacheManager cacheManager;
    private final RedisTemplate<String, Object> redisTemplate;


    @Override
    public void evictUserCache(String emailAddress) {
        String[] cacheNames = {"credentials", "userDetails", "tokens"};

        for (String cacheName : cacheNames) {
            Cache cache = cacheManager.getCache(cacheName);
            if (cache != null) {
                redisTemplate.delete(cacheName + "::" + emailAddress);
            }
        }
    }


    @Override
    public void putUserCache(String emailAddress, Credential credential) {
        Cache credentialCache = cacheManager.getCache("credentials");
        Cache userDetailsCache = cacheManager.getCache("userDetails");

        if (credentialCache != null) {
            credentialCache.put(emailAddress, credential);
        }

        if (userDetailsCache != null) {
            UserDetails userDetails = createUserDetails(credential);
            userDetailsCache.put(emailAddress, userDetails);
        }
    }


    @Override
    public boolean existsCredential(String emailAddress) {
        Cache credentialCache = cacheManager.getCache("credentials");
        if (credentialCache != null) {
            return redisTemplate.hasKey("credentials::" + emailAddress);
        }
        return false;
    }


    private UserDetails createUserDetails(Credential credential) {
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
}
