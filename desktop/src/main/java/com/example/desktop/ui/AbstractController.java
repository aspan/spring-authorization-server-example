package com.example.desktop.ui;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javafx.stage.Stage;

public abstract class AbstractController implements ApplicationContextAware, StageAware {
    private ApplicationContext context;
    private Stage stage;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    @Override
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public Stage getStage() {
        return this.stage;
    }

    public void changeView(String fxml) {
        this.context.publishEvent(new StageReadyEvent(getStage(), fxml));
    }
}
