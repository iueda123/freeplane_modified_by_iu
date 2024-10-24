package org.freeplane.plugin.util;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * A simple example showing how to use {@link FileDrop}
 *
 * @author Robert Harder, rob@iharder.net
 */
public class JTextField_with_FileDrop extends JTextField {

    /**
     * Runs a sample program that shows dropped files
     */
    public static void main(String[] args) {
        JFrame frame = new JFrame("JTextField_with_FileDrop");

        /**
         * FileDrop
         */
        JTextField_with_FileDrop textField = new JTextField_with_FileDrop();
        //textField.setPreferredSize(new Dimension(250,25));

        frame.getContentPane().add(new JScrollPane(textField), BorderLayout.CENTER);
        //frame.setBounds(100, 100, 300, 25);
        frame.pack();
        frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }   // end main


    /**
     * Constructs a new TextArea.  A default model is set, the initial string
     * is null, and rows/columns are set to 0.
     */
    public JTextField_with_FileDrop() {
        setFileDrop();
        //setFileDrop_with_Border();
    }


    private void setFileDrop() {
        new FileDrop(
                null,//System.out,
                JTextField_with_FileDrop.this, /*dragBorder,*/
                new FileDrop.Listener() {
                    public void filesDropped(java.io.File[] files) {
                        try {
                            JTextField_with_FileDrop.this.setText(files[0].getCanonicalPath());
                        } // end try
                        catch (java.io.IOException e) {
                            e.printStackTrace();
                        }
                    }   // end filesDropped
                }); // end FileDrop.Listener
    }

    private void setFileDrop_with_Border() {
        TitledBorder dragBorder = new TitledBorder("Drop 'em");
        new FileDrop(
                System.out,
                JTextField_with_FileDrop.this, dragBorder,
                new FileDrop.Listener() {
                    public void filesDropped(java.io.File[] files) {
                        try {
                            JTextField_with_FileDrop.this.setText(files[0].getCanonicalPath() + "\n");
                        } // end try
                        catch (java.io.IOException e) {
                            e.printStackTrace();
                        }
                    }   // end filesDropped
                }); // end FileDrop.Listener
    }
}
