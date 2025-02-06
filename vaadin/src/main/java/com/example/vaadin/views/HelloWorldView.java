package com.example.vaadin.views;

import jakarta.annotation.security.PermitAll;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.security.AuthenticationContext;

@PageTitle("Hello World")
@Route("")
@PermitAll
public class HelloWorldView extends VerticalLayout {
    private final TextField name;

    public HelloWorldView(AuthenticationContext authenticationContext) {
        add(new H1("Hello %s!".formatted(authenticationContext.getPrincipalName().map(Object::toString).orElse(""))));

        var logoutButton = new Button("Logout", _ -> authenticationContext.logout());
        add(logoutButton);

        setAlignItems(Alignment.CENTER);

        var helloWorldLayout = new HorizontalLayout();
        name = new TextField("Your name");
        var sayHelloButton = new Button("Say hello");
        sayHelloButton.addClickListener(e -> {
            Notification.show("Hello " + name.getValue());
        });
        sayHelloButton.addClickShortcut(Key.ENTER);

        helloWorldLayout.setMargin(true);
        helloWorldLayout.setVerticalComponentAlignment(Alignment.END, name, sayHelloButton);

        helloWorldLayout.add(name, sayHelloButton);
        add(helloWorldLayout);
    }
}
