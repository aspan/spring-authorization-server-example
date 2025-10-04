package com.example.desktop.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.server.servlet.context.ServletWebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class WebServerStartupListener implements ApplicationListener<ServletWebServerInitializedEvent> {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebServerStartupListener.class);
    private final AuthorizationService application;

    public WebServerStartupListener(AuthorizationService application) {
        this.application = application;
    }

    @Override
    public void onApplicationEvent(ServletWebServerInitializedEvent event) {
        var url = "http://127.0.0.1:" + event.getWebServer().getPort() + "/oauth2/authorization/desktop-client";
        LOGGER.debug("URL: {}", url);
        this.application.url().complete(url);
    }
}
