package org.sleepless_artery.auth_service.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sleepless_artery.auth_service.exception.VerificationException;
import org.sleepless_artery.auth_service.service.VerificationService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Duration;


@Slf4j
@Service
@RequiredArgsConstructor
public class VerificationServiceImpl implements VerificationService {

    private final RedisTemplate<String, String> redisTemplate;


    @Override
    public String saveVerificationCode(String emailAddress) {
        String verificationCode = generateConfirmationCode();
        redisTemplate.delete("verificationCode:" + emailAddress);

        log.debug("Saving verification code for email: {}", emailAddress);
        try {
            redisTemplate.opsForValue().set(
                    "verification_code:" + emailAddress,
                    verificationCode,
                    Duration.ofMinutes(60)
            );
        } catch (Exception e) {
            log.warn("Failed to save verification code for email: {}", emailAddress);
            throw new VerificationException("Failed to save verification code");
        }

        return verificationCode;
    }


    @Override
    public boolean verifyAndDeleteCode(String emailAddress, String code) {
        log.debug("Verifying and deleting code for email address: {}", emailAddress);

        String storedCode = redisTemplate.opsForValue().get("verification_code:" + emailAddress);
        if (storedCode != null && storedCode.equals(code)) {
            redisTemplate.delete("verification_code:" + emailAddress);
            return true;
        }
        return false;
    }


    private String generateConfirmationCode() {
        log.info("Generating confirmation code");

        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[3];
        random.nextBytes(bytes);

        return String.format("%06d", ((bytes[0] & 0xFF) << 16 | (bytes[1] & 0xFF) << 8 | (bytes[2] & 0xFF)) % 1000000);
    }
}
