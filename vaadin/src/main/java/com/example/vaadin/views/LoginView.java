package com.example.vaadin.views;


import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.theme.lumo.LumoUtility;

@Route("login")
@PageTitle("Login")
@AnonymousAllowed
public class LoginView extends VerticalLayout {

    private static final String OAUTH_URL = "/oauth2/authorization/vaadin-client-oidc";

    public LoginView() {
        setAlignItems(Alignment.CENTER);
        add(new H1("Login"));
        add(new Paragraph("Login example for Spring Security OAuth 2.0 Authentication Server:"));
        Anchor loginLink = new Anchor(OAUTH_URL, "Login with OAuth 2.0");
        loginLink.addClassName(LumoUtility.FontSize.XLARGE);
        loginLink.setRouterIgnore(true);
        add(loginLink);
    }
}