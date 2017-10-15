package com.noxag.newnox.ui.configurationmodule;

import java.io.File;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
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
import javafx.stage.Window;

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

        btnrun = createButtons("Run");
        btnopen = createButtons("Open File...");
        btnbox = createButtonBox();

        btnbox.getChildren().addAll(btnopen, btnrun);

        createActionEventForOpenFile();

        this.setBottom(btnbox);

        createFileChooser();
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

    private void createFileChooser() {
        fileChooser = new FileChooser();
        fileChooser.setTitle("Open File...");
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("PDF Files", "*.pdf"),
                new ExtensionFilter("All Files", "*.*"));
    }

    private void createActionEventForOpenFile() {
        btnrun.setDisable(true);

        btnopen.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(final ActionEvent e) {
                Node source = (Node) e.getSource();
                Window stage = source.getScene().getWindow();
                File file = fileChooser.showOpenDialog(stage);
                if (file != null) {
                    btnrun.setDisable(false);
                }
            }
        });
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
