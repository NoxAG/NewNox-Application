package com.noxag.newnox.ui.configurationmodule;

import java.util.List;

import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

public class ConfigurationPane extends BorderPane {
    public ConfigurationPane() {
        this.setCenter(new ConfigurationTabPane());

        this.setBorder(new Border(
                new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        // Add "run" button
    }

    public List<String> getTextanalyzer() {
        // TODO: This method should return a list of strings containing every
        // algorithm that has been "checked"
        return null;
    }

}
