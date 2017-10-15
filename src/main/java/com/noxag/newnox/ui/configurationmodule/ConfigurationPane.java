package com.noxag.newnox.ui.configurationmodule;

import java.util.List;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class ConfigurationPane extends BorderPane {

    private Button btnrun, btnopen;
    private FileChooser fileChooser;
    private ConfigurationTabPane configTabPane;
    private HBox btnbox;

    public ConfigurationPane() {

        configTabPane = new ConfigurationTabPane();
        this.setCenter(configTabPane);

        this.setBorder(new Border(
                new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

        createButtons();
        createFileChooser();

    }

    private void createButtons() {
        btnrun = new Button("Run");
        btnopen = new Button("Open File ...");
        btnbox = new HBox();
        this.setBottom(btnbox);
        btnbox.getChildren().addAll(btnopen, btnrun);
        btnbox.setAlignment(Pos.BOTTOM_RIGHT);
        HBox.setMargin(btnrun, new Insets(4.0, 4.0, 4.0, 4.0));
        HBox.setMargin(btnopen, new Insets(4.0, 0.0, 4.0, 4.0));
    }

    private void createFileChooser() {
        fileChooser = new FileChooser();
        fileChooser.setTitle("Open File...");
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("PDF Files", "*.pdf"),
                new ExtensionFilter("All Files", "*.*"));
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

    public List<String> getTextanalyzer() {
        // TODO: This method should return a list of strings containing every
        // algorithm that has been "checked"
        return null;
    }

}
