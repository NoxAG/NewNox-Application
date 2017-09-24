package com.noxag.newnox.ui.configurationmodule;

import java.awt.BorderLayout;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

public class FileChooserFrame extends JFrame {

    private static final long serialVersionUID = -6243001352460084515L;

    public FileChooserFrame() {
        JFileChooser jFileChooser = new javax.swing.JFileChooser();
        this.setLayout(new BorderLayout());
        this.add(jFileChooser, BorderLayout.CENTER);
    }

}
