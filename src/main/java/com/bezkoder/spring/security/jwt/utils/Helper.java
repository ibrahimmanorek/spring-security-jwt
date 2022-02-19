package com.bezkoder.spring.security.jwt.utils;

import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

@Component
public class Helper {
    public static String createSignature(String payload, String key) {
        byte[] signatureByte = null;
        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
            sha256_HMAC.init(secret_key);

            signatureByte = sha256_HMAC.doFinal(payload.getBytes("UTF-8"));
        } catch (Exception e) {

        }

        return DatatypeConverter.printBase64Binary(signatureByte);
    }

    public static String createPayload(String path, String verb, String token, String timestamp, String body) {
        String payload = "path=" + path + "&verb=" + verb + "&token=Bearer " + token + "&timestamp=" + timestamp
                + "&body=" + body;
        return payload;
    }
}
