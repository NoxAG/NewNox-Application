package com.noxag.newnox.ui.configurationmodule;

import java.io.File;
import java.util.List;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class ConfigurationPane extends BorderPane {

    private Button btnrun, btnopen;
    private FileChooser fileChooser;
    private ConfigurationTabPane configTabPane;
    private HBox btnbox;

    public ConfigurationPane() {
        this.setStyle("-fx-border-width: 0 0 2 0; " + "-fx-border-style: solid solid solid solid;");

        configTabPane = new ConfigurationTabPane();
        configTabPane.prefHeightProperty().bind(this.heightProperty().multiply(0.9));

        btnrun = createButtons("Run");
        btnopen = createButtons("Open File...");
        btnrun.setDisable(true);

        fileChooser = createFileChooser();

        btnbox = createButtonBox();
        btnbox.getChildren().addAll(btnopen, btnrun);

        this.setCenter(configTabPane);
        this.setBottom(btnbox);
    }

    public List<String> getSelectedAnalyzers() {
        return this.configTabPane.getSelectedAnalyzers();
    }

    private Button createButtons(String text) {
        Button btn = new Button(text);
        HBox.setMargin(btn, new Insets(4.0, 4.0, 4.0, 4.0));
        return btn;
    }

    private HBox createButtonBox() {
        HBox btnBox = new HBox();
        btnBox.setAlignment(Pos.BOTTOM_RIGHT);
        return btnBox;
    }

    private FileChooser createFileChooser() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open File...");
        fileChooser.getExtensionFilters().clear();
        fileChooser.getExtensionFilters().add(new ExtensionFilter("PDF Files", "*.pdf"));
        fileChooser.setInitialDirectory(getUserDocumentDirectory());
        return fileChooser;
    }

    private File getUserDocumentDirectory() {
        File userDirectory = new File(System.getProperty("user.home") + "/Documents");
        if (!userDirectory.canRead()) {
            userDirectory = new File("c:/");
        }
        return userDirectory;
    }

    public Button getRunButton() {
        return btnrun;
    }

    public Button getOpenButton() {
        return btnopen;
    }

    public FileChooser getFileChooser() {
        return fileChooser;
    }

    public ConfigurationPane getPane() {
        return this;
    }

    public void setTextanalyzerUInames(List<String> AlgorithmUINames) {
        this.configTabPane.setTextanalyzerUInames(AlgorithmUINames);
    }

    public void setStatisticanalyzerUInames(List<String> AlgorithmUINames) {
        this.configTabPane.setStatisticanalyzerUInames(AlgorithmUINames);
    }

}
