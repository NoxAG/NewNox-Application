package com.noxag.newnox.ui.configurationmodule;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Window;

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
        fileChooser = createFileChooser();

        createActionEventForOpenFile();
        createActionEventForRunButton();

        btnbox = createButtonBox();
        btnbox.getChildren().addAll(btnopen, btnrun);

        this.setCenter(configTabPane);
        this.setBottom(btnbox);
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
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("PDF Files", "*.pdf"),
                new ExtensionFilter("All Files", "*.*"));
        return fileChooser;
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

    private void createActionEventForRunButton() {
        btnrun.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(final ActionEvent e) {
                List<String> selectedAlgorithms = new ArrayList<String>();
                configTabPane.getTabs().stream().forEach(tab -> {
                    selectedAlgorithms.addAll(((ConfigurationTab) tab).getSelectedAnalyzers());
                });
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

    public ConfigurationPane getPane() {
        return this;
    }

}
