package com.noxag.newnox.application;

import javafx.application.Application;

public class NewNox {
    public static void main(String[] args) {
        MainController mainController = new MainController();

        Application.launch(com.noxag.newnox.ui.NewNoxWindow.class);
        /*
         * mainWindow.registerOpenPDFEvent(mainController::openPDFDocument);
         * mainWindow.registerAnalyzeEvent(mainController::analyzePDFDocument);
         * mainWindow.setTextanalyzerAlgorithms(mainController.
         * getTextanalyzerUINames()); mainWindow.setBounds(600, 150, 800, 600);
         * mainWindow.setVisible(true); mainWindow.setAlwaysOnTop(true);
         */
    }
}
