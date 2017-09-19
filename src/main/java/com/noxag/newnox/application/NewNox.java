package com.noxag.newnox.application;

import com.noxag.newnox.ui.NewNoxWindow;

public class NewNox {
    public static void main(String[] args) {
        NewNoxWindow mainWindow = new NewNoxWindow();
        mainWindow.setBounds(600, 150, 800, 600);
        mainWindow.setVisible(true);
    }
}
