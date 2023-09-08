/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.freeplane.plugin.util;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.util.regex.Pattern;


/**
 * 指定する拡張子（.zip）であるファイルだけを取得します。
 * source:http://www.syboos.jp/java/doc/listfiles-by-filefilter.html
 *
 * <使用例>
 * File file = new File("c:\\"); //拡張子は.zipのファイルを取得します。 File[] zipFiles =
 * file.listFiles(getFileExtensionFilter(".zip"));
 *
 * FilenameFilterとFileFilterの違いについては「https://www.daniweb.com/software-development/java/threads/357131/filefilter-vs-filenamefilter」を参照。
 */
public class FileFilterConstructor_Extension_ver2 {
    public static FileFilter getFileFilter(String extension) {
        final String _extension = extension;
        return new FileFilter() {

            @Override
            public boolean accept(File pathname) {
                /**
                 * この「pathname」はどうして親フォルダを指すのか？
                 * FileクラスがlistFiles()メソッドなどを通じてFileFilterなり、FileNameFilterなりを利用するとき、
                 * accept(File dir, String name)やaccept(File pathname)を介して、
                 * すなわちacceptというメソッドに親フォルダ名やファイル名を渡しながら処理を進めてゆくからと考えられる。
                 */
                //System.out.println("◆pathname.getAbsolutePath():" + pathname.getAbsolutePath());
                String file_extension = getExtension(pathname.getAbsolutePath());
                int ret = file_extension.compareToIgnoreCase(_extension);
                return ret == 0;
            }
        };
    }
    
    

    /**
     * 大文字と小文字の区別ありバージョン
     * @param extension
     * @return 
     */
    public static FileFilter getFileFilter_Exactly(String extension) {
        final String _extension = extension;
        return new FileFilter() {

            @Override
            public boolean accept(File pathname) {
                /**
                 * この「pathname」はどうして親フォルダを指すのか？
                 * FileクラスがlistFiles()メソッドなどを通じてFileFilterなり、FileNameFilterなりを利用するとき、
                 * accept(File dir, String name)やaccept(File pathname)を介して、
                 * すなわちacceptというメソッドに親フォルダ名やファイル名を渡しながら処理を進めてゆくからと考えられる。
                 */

                //System.out.println("◆pathname.getAbsolutePath():" + pathname.getAbsolutePath());
                boolean ret = pathname.getAbsolutePath().endsWith(_extension);
                return ret;
            }
        };
    }

    public static FilenameFilter getFileNameFilter(String extension) {
        final String _extension = extension;
        return new FilenameFilter() {

            @Override
            public boolean accept(File dir, String name) {
                String file_extension = getExtension(name);
                int ret = file_extension.compareToIgnoreCase(_extension);
                //System.out.println("ret: " + ret);
                return ret == 0;
            }
        };
    }

    /**
     * 大文字と小文字の区別ありバージョン
     * @param extension
     * @return 
     */
    public static FilenameFilter getFileNameFilter_Exactly(String extension) {
        final String _extension = extension;

        return new FilenameFilter() {

            @Override
            public boolean accept(File dir, String name) {
                boolean ret = name.endsWith(_extension);
                return ret;
            }
        };
    }

    /**
     * descriptionには、例えば「ZIPファイル(*.zip)」などと入れる。
     * @param extension
     * @param description:例えば「ZIPファイル(*.zip)」などと入れる。
     * @return 
     */
    public static javax.swing.filechooser.FileFilter getSwingFileFilter(String extension, String description) {
        final String _extension = extension;
        final String _description = description;

        return new javax.swing.filechooser.FileFilter() {

            @Override
            public boolean accept(File f) {
                String file_extension = getExtension(f.getAbsolutePath());
                int ret = file_extension.compareToIgnoreCase(_extension);
                return ret == 0;
                
            }

            @Override
            public String getDescription() {
                return _description;
            }
        };
    }
    
    private static String getExtension(String FileNameWithExtension) {
        Pattern splitMark = Pattern.compile("\\.");
        String[] SplitResult = splitMark.split(FileNameWithExtension);

        /*拡張子を分離*/
        String Extension = SplitResult[SplitResult.length - 1];
        return Extension;
    }

    
    
    
    public static void main(String[] args) {
        File F1 = new File("C:\\Users\\Issey\\Downloads");

        System.out.println("-----------------getFileFilter() test------------------------");
        for (File f : F1.listFiles(getFileFilter("Zip"))) {
            System.out.println(f.getAbsolutePath());
        }

        System.out.println("-----------------getFileFilter_Exactly() test------------------------");
        for (File f : F1.listFiles(getFileFilter_Exactly("Zip"))) {
            System.out.println(f.getAbsolutePath());
        }

        System.out.println("-----------------getFileNameFilter() test------------------------");
        for (File f : F1.listFiles(getFileNameFilter("zip"))) {
            System.out.println(f.getAbsolutePath());
        }

        System.out.println("-----------------getFileNameFilter_Exactly() test------------------------");
        for (File f : F1.listFiles(getFileNameFilter_Exactly("zip"))) {
            System.out.println(f.getAbsolutePath());
        }

    }

}
