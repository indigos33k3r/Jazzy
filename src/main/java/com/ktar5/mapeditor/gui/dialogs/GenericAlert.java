package com.ktar5.mapeditor.gui.dialogs;

import javafx.scene.control.Alert;

public class GenericAlert {
    public final String message;

    public GenericAlert(String message) {
        this.message = message;

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error Occurred");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}