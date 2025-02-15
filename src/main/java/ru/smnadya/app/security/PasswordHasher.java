package ru.smnadya.app.security;

import com.google.common.hash.Hashing;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class PasswordHasher {

    public String hashPassword(String password) {
        return Hashing.sha256()
                .hashString(password, StandardCharsets.UTF_8)
                .toString();
    }

}
