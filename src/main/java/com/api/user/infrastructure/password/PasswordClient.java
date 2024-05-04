package com.api.user.infrastructure.password;

import com.api.user.setting.PasswordConfig;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PasswordClient {

    private PasswordConfig passwordConfig;

    public String encode(String password) {
        return passwordConfig.passBuilder()
                .encode(password);
    }
}
