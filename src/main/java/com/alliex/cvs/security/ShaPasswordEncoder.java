package com.alliex.cvs.security;

import com.alliex.cvs.util.CryptoUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class ShaPasswordEncoder implements PasswordEncoder {

    @Override
    public String encode(CharSequence rawPassword) {
        return CryptoUtils.sha256(rawPassword.toString());
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return CryptoUtils.sha256(rawPassword.toString()).equals(encodedPassword);
    }

}
