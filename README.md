# Spring Authorization Server Example

Example project to demonstrate how to integrate different
clients with the Spring Authorization Server.

## Authorization Server

The `authorization-server` module contains the authorization server.

## Resource Server

The `resource-server` module has a simple resource server.

## Web & Thymeleaf

The `thymeleaf-web-application` module contains a web app with thymeleaf
and javascript that demonstrates SPA login. Run with `./run-web.sh` which
will start the authorization and resource servers before the app is started
and stop them after.

## Vaadin & Hilla

The `vaadin-web-application` and `hilla-web-application` modules demonstrates
how to integrate OAuth2 with the Flow java based frontend and with the Hilla
react based frontend. Run with `./run-vaadin.sh` or `./run-hilla.sh` which
will start the authorization and resource servers before the app is started
and stop them after.

## Desktop JavaFX

The `javafx-desktop-application` module demonstrates how to integrate with
a desktop app. The desktop app spins up a separate short-lived spring boot app
that handles the authentication flow and then shuts down once the user has been
authorized. The JavaFX application has a WebView dialog to handle the web login.
Run with `./run-desktop.sh` which will start the authorization and resource servers
before the app is started and stop them after.


