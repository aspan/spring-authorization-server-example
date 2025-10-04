package com.example.desktop.ui;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;

@Component
@Scope(SCOPE_PROTOTYPE)
@SuppressWarnings("this-escape")
public class LoginController extends AbstractController {
    public static final String FXML = "login.fxml";

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);
    private final AuthenticationService authenticationService;
    private final Stage dialog;
    private final WebView webView;

    public LoginController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
        this.dialog = new Stage();
        this.dialog.initModality(Modality.APPLICATION_MODAL);
        this.dialog.initOwner(getStage());
        this.dialog.setOnCloseRequest(e -> Platform.runLater(this.authenticationService::cancel));
        this.webView = new WebView();
        this.dialog.setScene(new Scene(new VBox(20, webView), 640, 480));
        this.webView.getEngine().getLoadWorker().stateProperty().addListener((ov, oldState, newState) -> LOGGER.debug("State changed to {}, url = {}", newState, webView.getEngine().getLocation()));
        this.webView.getEngine().getLoadWorker().exceptionProperty().addListener((ov, t, t1) -> LOGGER.error("Received exception", t1));
    }

    public void login(ActionEvent actionEvent) {
        this.authenticationService.authenticate(
                url -> {
                    this.webView.getEngine().load(url);
                    this.dialog.show();
                },
                authentication -> {
                    try {
                        Thread.sleep(1000L);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    Platform.runLater(() -> {
                        this.dialog.close();
                        changeView(ApplicationController.FXML);
                    });
                },
                message -> {
                    Platform.runLater(() -> {
                        this.dialog.close();
                        var alert = new Alert(AlertType.WARNING, message, ButtonType.OK);
                        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                        alert.show();
                    });
                });
    }
}
