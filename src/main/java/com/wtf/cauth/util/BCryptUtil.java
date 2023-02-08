package com.wtf.cauth.util;

import org.springframework.security.crypto.bcrypt.BCrypt;

public class BCryptUtil {

    public static String hash(String plaintext) {
        return BCrypt.hashpw(plaintext, BCrypt.gensalt());
    }

    public static boolean verify(String plaintext, String hashed) {
        return BCrypt.checkpw(plaintext, hashed);
    }


}
