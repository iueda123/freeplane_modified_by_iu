package org.freeplane.plugin.helloaction;

import org.freeplane.core.ui.AFreeplaneAction;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class HelloAction extends AFreeplaneAction {
    public HelloAction() {
        super("HelloAction");
    }

    public void actionPerformed(final ActionEvent e) {

        JOptionPane.showMessageDialog(null, "Hello!");

    }
}