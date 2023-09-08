package org.freeplane.plugin.util;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * A simple example showing how to use {@link FileDrop}
 *
 * @author Robert Harder, rob@iharder.net
 */
public class JPanel_with_FileDrop extends JPanel {

    /**
     * Runs a sample program that shows dropped files
     */
    public static void main(String[] args) {
        JFrame frame = new JFrame("JTextField_with_FileDrop");

        /**
         * FileDrop
         */
        JPanel_with_FileDrop panel = new JPanel_with_FileDrop();
        //textField.setPreferredSize(new Dimension(250,25));
        panel.setBackground(Color.yellow);

        frame.getContentPane().add(new JScrollPane(panel), BorderLayout.CENTER);
        //frame.setBounds(100, 100, 300, 25);
        frame.pack();
        frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }   // end main


    /**
     * Constructs a new TextArea.  A default model is set, the initial string
     * is null, and rows/columns are set to 0.
     */
    public JPanel_with_FileDrop() {
        //setFileDrop();
        setFileDrop_with_Border("Drop on me!");
    }


    private void setFileDrop() {
        new FileDrop(
                null,//System.out,
                JPanel_with_FileDrop.this, /*dragBorder,*/
                new FileDrop.Listener() {
                    public void filesDropped(java.io.File[] files) {
                        try {
                            JPanel_with_FileDrop.this.add(new JLabel(files[0].getCanonicalPath()));
                        } // end try
                        catch (java.io.IOException e) {
                            e.printStackTrace();
                        }
                    }   // end filesDropped
                }); // end FileDrop.Listener
    }

    private void setFileDrop_with_Border(String message) {
        TitledBorder dragBorder = new TitledBorder(message);
        new FileDrop(
                System.out,
                JPanel_with_FileDrop.this, dragBorder,
                new FileDrop.Listener() {
                    public void filesDropped(java.io.File[] files) {
                        try {
                            JPanel_with_FileDrop.this.add(new JLabel(files[0].getCanonicalPath()));
                        } // end try
                        catch (java.io.IOException e) {
                            e.printStackTrace();
                        }
                    }   // end filesDropped
                }); // end FileDrop.Listener
    }
}
