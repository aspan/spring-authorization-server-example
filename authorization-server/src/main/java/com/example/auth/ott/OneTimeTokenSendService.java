package com.example.auth.ott;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.ott.OneTimeToken;
import org.springframework.stereotype.Service;

@Service
public class OneTimeTokenSendService {
    private static final Logger LOGGER = LoggerFactory.getLogger(OneTimeTokenSendService.class);

    public void send(OneTimeToken token) {
        LOGGER.info("Login at: http://localhost:9000/login/ott?token={}", token.getTokenValue());
    }
}
