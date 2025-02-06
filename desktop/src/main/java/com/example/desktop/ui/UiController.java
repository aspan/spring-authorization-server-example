package com.example.desktop.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;

@Component
public class UiController implements StageAware {
    private static final Logger LOGGER = LoggerFactory.getLogger(UiController.class);
    private final AuthenticationService authenticationService;
    private final ResourcesService resourcesService;
    private Stage stage;

    @FXML
    public Button login;
    @FXML
    public Label label;
    @FXML
    public Label resourcesLabel;

    private WebView webView;

    public UiController(AuthenticationService authenticationService, ResourcesService resourcesService) {
        this.authenticationService = authenticationService;
        this.resourcesService = resourcesService;
    }

    @FXML
    public void initialize() {
        final var dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(getStage());
        webView = new WebView();
        webView.getEngine().getLoadWorker().stateProperty().addListener((ov, oldState, newState) -> LOGGER.info("State changed to {}, url = {}", newState, webView.getEngine().getLocation()));
        webView.getEngine().getLoadWorker().exceptionProperty().addListener((ov, t, t1) -> LOGGER.error("Received exception", t1));
        this.login.setOnAction(event -> {
            authenticationService.authenticate(
                    url -> {
                        webView.getEngine().load(url);
                        VBox dialogVbox = new VBox(20, webView);
                        Scene dialogScene = new Scene(dialogVbox, 640, 480);
                        dialog.setScene(dialogScene);
                        dialog.show();
                    },
                    authentication -> {
                        try {
                            Thread.sleep(1000L);
                        } catch (InterruptedException ignored) {
                        }
                        Platform.runLater(dialog::close);
                        Platform.runLater(() -> {
                            login.setDisable(true);
                            label.setText(authentication.getName());
                        });
                        Platform.runLater(() -> resourcesLabel.setText(String.join(", ", resourcesService.getResources())));
                    },
                    () -> {
                        Platform.runLater(() -> {
                            dialog.close();
                            label.setText("Login failed");
                        });
                    });
        });
    }

    @Override
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Stage getStage() {
        return stage;
    }
}
