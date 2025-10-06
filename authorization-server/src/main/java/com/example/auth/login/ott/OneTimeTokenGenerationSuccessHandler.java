package com.example.auth.login.ott;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.ott.OneTimeToken;
import org.springframework.security.web.authentication.ott.RedirectOneTimeTokenGenerationSuccessHandler;
import org.springframework.stereotype.Component;

/**
 * One time token generation success handler sends the generated one time token
 * to the user and redirects to the "/login/ott" page to enter the token
 */
@Component
public class OneTimeTokenGenerationSuccessHandler implements org.springframework.security.web.authentication.ott.OneTimeTokenGenerationSuccessHandler {
    private final OneTimeTokenSendService oneTimeTokenSendService;
    private final RedirectOneTimeTokenGenerationSuccessHandler redirectHandler = new RedirectOneTimeTokenGenerationSuccessHandler("/login/ott");

    public OneTimeTokenGenerationSuccessHandler(OneTimeTokenSendService oneTimeTokenSendService) {
        this.oneTimeTokenSendService = oneTimeTokenSendService;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, OneTimeToken oneTimeToken) throws IOException, ServletException {
        oneTimeTokenSendService.send(oneTimeToken);
        redirectHandler.handle(request, response, oneTimeToken);
    }
}
