package com.otp.otpauthserver.util;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import lombok.experimental.UtilityClass;

@UtilityClass
public class GenerateCodeUtil {

    public static String generateCode() {
        String code;
        try {
            SecureRandom random = SecureRandom.getInstanceStrong();
            int c = random.nextInt(9000) + 1000;
            code = String.valueOf(c);
            return code;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Problem when generating the random code.");
        }
    }
}
