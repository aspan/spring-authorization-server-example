package com.example.desktop.ui;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

@Component
@Scope(SCOPE_PROTOTYPE)
public class ApplicationController extends AbstractController {
    public static final String FXML = "application.fxml";
    private final AuthenticationService authenticationService;
    private final ResourceService resourceService;

    @FXML
    public Label usernameLabel;
    @FXML
    private ListView<String> resourcesList;

    public ApplicationController(AuthenticationService authenticationService, ResourceService resourceService) {
        this.authenticationService = authenticationService;
        this.resourceService = resourceService;
    }

    @FXML
    public void initialize() {
        this.usernameLabel.setText("Username: " + authenticationService.getAuthentication().getName());
        this.resourcesList.setItems(FXCollections.observableList(this.resourceService.getResources()));
    }

    public void logout(ActionEvent actionEvent) {
        this.authenticationService.logout();
        changeView(LoginController.FXML);
    }
}
