package org.freeplane.plugin.util;


/**
 * UTF-8 エンコーディングされたプロパティファイルを取り扱う方法
 * http://blog.k11i.biz/2014/09/java-utf-8.html
 */

import java.io.*;
import java.util.Properties;

public class PropertyManager_ver2 {
    public static void main(String[] args) {
        /**
         * 書き出しテスト
         */
        Properties new_properties = new Properties();
        new_properties.setProperty("x", "225");
        new_properties.setProperty("y", "465");
        new_properties.setProperty("z", "777");
        PropertyManager_ver2.saveUtf8Properties(new_properties, "src/FileIO/new_PropertyFile_UTF8.txt");

        /**
         * 読み込みテスト
         */
        PropertyManager_ver2 pm = null;
        try {
            pm = new PropertyManager_ver2("src/FileIO/new_PropertyFile_UTF8.txt");
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        System.out.println("x = " + pm.getProperties().getProperty("x"));
    }


    private Properties properties = new Properties();

    public PropertyManager_ver2(String propertyFilePath) throws IOException {
        InputStream is = new FileInputStream(new File(propertyFilePath));
        InputStreamReader isr = new InputStreamReader(is, "UTF-8");
        BufferedReader bfreader = new BufferedReader(isr);
        // Properties#load() で渡す Reader オブジェクトを UTF-8 エンコーディング指定して生成した
        // InputStreamReader オブジェクトにする
        properties.load(bfreader);
        bfreader.close();
        isr.close();
        is.close();

    }

    public Properties getProperties() {
        return properties;
    }

    public static void saveUtf8Properties(Properties properties, String new_file_path) {
        File new_property_file = new File(new_file_path);
        new_property_file.getParentFile().mkdirs();
        try {
            new_property_file.createNewFile();
            OutputStream os = new FileOutputStream(new_property_file);
            OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
            BufferedWriter bfwriter = new BufferedWriter(osw);
            properties.store(bfwriter, "");
            System.out.println("新しいプロパティを " + new_file_path + " へ書き出しました。");
            bfwriter.close();
            osw.close();
            os.close();
        } catch (FileNotFoundException fne) {
            fne.printStackTrace();
        } catch (UnsupportedEncodingException usee) {
            usee.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

    }


}

