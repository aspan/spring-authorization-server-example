package com.example.desktop.ui;

import org.springframework.context.ApplicationEvent;

import javafx.stage.Stage;

public class StageReadyEvent extends ApplicationEvent {
    public Stage getStage() {
        return (Stage) this.getSource();
    }

    public StageReadyEvent(Stage stage) {
        super(stage);
    }
}
