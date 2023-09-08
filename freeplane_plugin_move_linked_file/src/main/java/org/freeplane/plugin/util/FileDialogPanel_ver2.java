/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.freeplane.plugin.util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * @author issey
 */
public class FileDialogPanel_ver2 extends JPanel {

    private JButton LoadButton = new JButton("...");
    private JTextField_with_FileDrop textField;
    public String defaultValue;

    /**
     * コンストラクタ
     */
    public FileDialogPanel_ver2(JFrame parentframe, String defalutValue) {
        //「LOAD」ダイアログ
        final JFrame ParentFrame = parentframe;

        //初期値
        this.defaultValue = defalutValue;

        //テキストボックス
        textField = new JTextField_with_FileDrop();
        textField.setText(defalutValue);
        //textField.setPreferredSize(new Dimension(this.getWidth(), this.getHeight()));
        textField.setPreferredSize(new Dimension(500, 25));

        //ボタン
        LoadButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                String Title = "";

                //ファイルチューザーの構築と表示
                FileDialog LoadDialog = new FileDialog(ParentFrame, Title, FileDialog.SAVE);
                LoadDialog.setFilenameFilter(FileNameFilterConstructor_OnlyDir.getOnlyDirFilter());//ファイルフィルタ設定
                if ((new File(FileDialogPanel_ver2.this.defaultValue)).exists()) {
                    LoadDialog.setDirectory(FileDialogPanel_ver2.this.defaultValue);//カレントディレクトリ
                } else {
                    LoadDialog.setDirectory(System.getProperty("user.home"));//ユーザーホーム
                }
                LoadDialog.setVisible(true);

                //選択後に投げ込むところ
                String SelectedFilePath = LoadDialog.getDirectory() + LoadDialog.getFile();
                //System.out.println("File selected by FileDialog = " + SelectedFilePath);
                if (SelectedFilePath.equals("nullnull")) {//何も選択されなかった時
                    //何もしない
                } else {
                    if (new File(SelectedFilePath).isDirectory()){
                        textField.setText("--Please select a file--");
                    } else {
                        textField.setText(SelectedFilePath);
                    }
                }
            }
        });

        //レイアウト
        Box hBox = Box.createHorizontalBox();
        hBox.add(textField);
        hBox.add(Box.createVerticalStrut(5));
        hBox.add(LoadButton);
        this.add(hBox);
    }

    public String getText() {
        return textField.getText();
    }

    public void setText(String text) {
        textField.setText(text);
    }


    public static void main(String[] args) {

        final JFrame frame = new JFrame();

        FileDialogPanel_ver2 FDP = new FileDialogPanel_ver2(frame, "");
        frame.getContentPane().add(FDP);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

    }
}
