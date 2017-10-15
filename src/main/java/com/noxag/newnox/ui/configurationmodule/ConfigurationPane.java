package com.noxag.newnox.ui.configurationmodule;

import java.util.List;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class ConfigurationPane extends BorderPane {
    public ConfigurationPane() {
        this.setCenter(new ConfigurationTabPane());

        this.setBorder(new Border(
                new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        // Add "run" button
        Button btnrun = new Button("Run");
        VBox btnbox = new VBox();
        this.setRight(btnbox);
        btnbox.getChildren().add(btnrun);
        btnbox.setAlignment(Pos.CENTER_RIGHT);

        // Textfield Path
        TextField path = new TextField();
        HBox hb = new HBox();
        this.setBottom(hb);
        Button open = new Button("Open");
        hb.getChildren().addAll(path, open);
        HBox.setHgrow(path, Priority.ALWAYS);

    }

    public List<String> getTextanalyzer() {
        // TODO: This method should return a list of strings containing every
        // algorithm that has been "checked"
        return null;
    }

}
