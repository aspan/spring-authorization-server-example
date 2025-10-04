# Spring Authorization Server Example

Example project to demonstrate how to integrate different
clients with the Spring Authorization Server.

## Authorization Server

The authorization-server module contains the authorization server
set up with mainly plain spring boot configuration.

## Web & Thymeleaf

The web module contains a web app with thymeleaf
and javascript that demonstrates SPA login.

## Resource Server

The resource module has a simple resource server.

## Vaadin & Hilla

The vaadin and hilla modules demonstrates how to integrate
OAuth2 with the Flow java based frontend and with the Hilla
react based frontend.

## Desktop JavaFX

The desktop module demonstrates how to integrate with a desktop app.
The desktop app spins up a separate short-lived spring boot app
that handles the authentication flow and then shuts down once the 
user has been authorized. The JavaFX application has a WebView dialog
to handle the web login.


