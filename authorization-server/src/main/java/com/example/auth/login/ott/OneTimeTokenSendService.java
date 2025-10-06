package com.example.auth.login.ott;

import org.springframework.security.authentication.ott.OneTimeToken;

public interface OneTimeTokenSendService {
    void send(OneTimeToken token);
}
