package com.example.core;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class OpenBrowserOnApplicationReadyEventListener implements ApplicationListener<ApplicationReadyEvent> {
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        System.setProperty("java.awt.headless", "false");
        var serverAddress = event.getApplicationContext().getEnvironment().getProperty("server.address");
        var serverPort = event.getApplicationContext().getEnvironment().getProperty("server.port");
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            try {
                Desktop.getDesktop().browse(new URI("http://" + serverAddress + ":" + serverPort));
            } catch (IOException | URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
