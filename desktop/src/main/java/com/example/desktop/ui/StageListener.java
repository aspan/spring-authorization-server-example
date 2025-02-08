package com.example.desktop.ui;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

@Component
public class StageListener implements ApplicationListener<StageReadyEvent> {
    private final ApplicationContext applicationContext;
    private final String applicationName;

    public StageListener(
            ApplicationContext applicationContext,
            @Value("${spring.application.name}") String applicationName
    ) {
        this.applicationContext = applicationContext;
        this.applicationName = applicationName;
    }

    @Override
    public void onApplicationEvent(StageReadyEvent event) {
        try {
            var stage = event.getStage();
            var url = getClass().getResource("/" + event.getFxml());
            var fxmlLoader = new FXMLLoader(
                    url,
                    null,
                    null,
                    c -> {
                        var controller = this.applicationContext.getBean(c);
                        if (controller instanceof StageAware stageAware) {
                            stageAware.setStage(stage);
                        }
                        return controller;
                    });
            var root = (Parent) fxmlLoader.load();
            var scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle(this.applicationName);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
