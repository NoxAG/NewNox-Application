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

public class ConfigurationPane extends BorderPane {

    public Button btnrun, open;

    public ConfigurationPane() {

        ConfigurationTabPane configTabPane = new ConfigurationTabPane();
        this.setCenter(configTabPane);

        this.setBorder(new Border(
                new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

        // Add "run" button
        btnrun = new Button("Run");
        open = new Button("Open File ...");
        HBox btnbox = new HBox();
        this.setBottom(btnbox);
        btnbox.getChildren().addAll(open, btnrun);
        btnbox.setAlignment(Pos.BOTTOM_RIGHT);
        HBox.setMargin(btnrun, new Insets(4.0, 4.0, 4.0, 4.0));
        HBox.setMargin(open, new Insets(4.0, 0.0, 4.0, 4.0));
        // btnrun.minWidthProperty().bind(open.widthProperty());

        /*
         * FileChooser fileChooser = new FileChooser();
         * fileChooser.setTitle("Open File...");
         */

    }

    public Button getRunButton() {
        // Gibt Run Button zurück
        return btnrun;
    }

    public List<String> getTextanalyzer() {
        // TODO: This method should return a list of strings containing every
        // algorithm that has been "checked"
        return null;
    }

}
