package com.noxag.newnox.application;

import com.noxag.newnox.ui.NewNoxWindow;

import javafx.application.Application;
import javafx.stage.Stage;

public class NewNox extends Application {
    private NewNoxWindow mainWindow;
    private MainController mainController;

    public NewNox() {
        mainWindow = new NewNoxWindow();
    }

    public static void main(String[] args) {
        launch(args);

    }

    @Override
    public void start(Stage stage) throws Exception {
        mainController = new MainController();
        mainWindow.start(stage);

        mainWindow.setTextanalyzerAlgorithms(mainController.getTextanalyzerUINames());
        mainWindow.setStatisticanalyzerAlgorithms(mainController.getStatisticanalyzerUINames());

        mainWindow.registerOpenPDFEvent(mainController::openPDFDocument);
        mainWindow.registerAnalyzeEvent(mainController::analyzePDFDocument);

        mainController.registerPDFImagesUpdateEvent(mainWindow::updatePDFImages);
        mainController.registerTextMarkupImagesUpdateEvent(mainWindow::updateTextMarkupImages);
        mainController.registerStatisticViewUpdateEvent(mainWindow::updateStatisticView);
        mainController.registerAlertPopupEvent(mainWindow::popupAlert);
    }

}
