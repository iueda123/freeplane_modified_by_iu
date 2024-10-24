/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.freeplane.plugin.util;

import java.io.File;
import java.io.FilenameFilter;

/**
 *  指定する拡張子（.zip）であるファイルだけを取得します。
 *  source:http://www.syboos.jp/java/doc/listfiles-by-filefilter.html
 * 
 *  <使用例>
 *  File file = new File("c:\\");
 *  //拡張子は.zipのファイルを取得します。  
 *  File[] zipFiles = file.listFiles(getFileExtensionFilter(".zip"));
 */
public class FileNameFilterConstructor_OnlyDir {
    
    
  public static FilenameFilter getOnlyDirFilter() {
        return new FilenameFilter() {

            @Override
            public boolean accept(File folder, String name) {
                File f = new File(folder.getAbsolutePath()+"\\"+name);
                return !(f.isFile());
            }
        };
    }
    

}
