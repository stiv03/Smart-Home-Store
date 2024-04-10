package com.ninjas.gig.security;

import java.security.SecureRandom;
import java.util.Base64;

public class SecurityConstants {
    public static final long JWT_EXPIRATION = 60000 * 60 * 24; // 24 hours authorization
    public static final String JWT_SECRET;

    static {
        int keyLength = 256;

        byte[] keyBytes = new byte[keyLength / 8];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(keyBytes);

        JWT_SECRET = Base64.getEncoder().encodeToString(keyBytes);
    }
}
