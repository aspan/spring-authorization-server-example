package com.example.desktop.ui;

import java.io.Serial;

import org.springframework.context.ApplicationEvent;

import javafx.stage.Stage;

public class StageReadyEvent extends ApplicationEvent {
    @Serial
    private static final long serialVersionUID = 1L;

    public Stage getStage() {
        return ((StageFxml) this.getSource()).stage();
    }

    public String getFxml() {
        return ((StageFxml) this.getSource()).fxml();
    }

    public StageReadyEvent(Stage stage, String location) {
        super(new StageFxml(stage, location));
    }

    private record StageFxml(Stage stage, String fxml) {
    }
}
