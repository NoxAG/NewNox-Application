package com.noxag.newnox.application;

import com.noxag.newnox.ui.NewNoxWindow;

import javafx.application.Application;
import javafx.stage.Stage;

public class NewNox extends Application {
    private Stage primaryStage;
    private NewNoxWindow mainWindow;

    public NewNox() {
        mainWindow = new NewNoxWindow();
    }

    public static void main(String[] args) {
        MainController mainController = new MainController();
        launch(args);
        /*
         * mainWindow.registerOpenPDFEvent(mainController::openPDFDocument);
         * mainWindow.registerAnalyzeEvent(mainController::analyzePDFDocument);
         * mainWindow.setTextanalyzerAlgorithms(mainController.
         * getTextanalyzerUINames()); mainWindow.setBounds(600, 150, 800, 600);
         * mainWindow.setVisible(true); mainWindow.setAlwaysOnTop(true);
         */
    }

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        mainWindow.start(primaryStage);
    }

}
