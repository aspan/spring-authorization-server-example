package com.example.auth.controller;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.security.web.webauthn.api.CredentialRecord;
import org.springframework.security.web.webauthn.management.PublicKeyCredentialUserEntityRepository;
import org.springframework.security.web.webauthn.management.UserCredentialRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebAuthnRegistrationPageController {
    private final PublicKeyCredentialUserEntityRepository userEntities;
    private final UserCredentialRepository userCredentials;

    public WebAuthnRegistrationPageController(PublicKeyCredentialUserEntityRepository userEntities, UserCredentialRepository userCredentials) {
        this.userEntities = userEntities;
        this.userCredentials = userCredentials;
    }

    @GetMapping("/webauthn/register")
    public String webAuthnRegisterPage(HttpServletRequest httpServletRequest, Model model) {
        var userEntity = this.userEntities.findByUsername(httpServletRequest.getRemoteUser());
        List<CredentialRecord> credentials = (userEntity != null) ? this.userCredentials.findByUserId(userEntity.getId()) : List.of();

        if (!credentials.isEmpty()) {
            model.addAttribute("passkeys", credentials.stream().map(
                    c -> new Passkey(
                            c.getCredentialId().toBase64UrlString(),
                            c.getLabel(),
                            formatInstant(c.getCreated()),
                            formatInstant(c.getLastUsed()),
                            c.getSignatureCount())
            ).toList());
        }
        return "webauthn-register";
    }

    private static String formatInstant(Instant created) {
        return ZonedDateTime.ofInstant(created, ZoneId.of("UTC"))
                            .truncatedTo(ChronoUnit.SECONDS)
                            .format(DateTimeFormatter.ISO_INSTANT);
    }

    public record Passkey(String id, String label, String created, String lastUsed, long signatureCount) {
    }
}