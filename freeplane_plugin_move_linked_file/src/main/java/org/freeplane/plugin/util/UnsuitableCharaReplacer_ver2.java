/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.freeplane.plugin.util;

/**
 * Windowsシステムでファイル名に含むことのできない文字列の変換クラス
 * @author Issey
 */
public class UnsuitableCharaReplacer_ver2 {
    
    public  static String replace(String str){
        
        String result = str;
        result = result.replaceAll("\\\\", "￥");
        result = result.replaceAll("/", "／");
        result = result.replaceAll(":", "：");
        result = result.replaceAll("[*]", "＊");
        result = result.replaceAll("[?]", "？");
        result = result.replaceAll("<", "＜");
        result = result.replaceAll("[|]", "｜");
        result = result.replaceAll(">", "＞");
        result = result.replaceAll("\\n", "");
        
        return result;
        
    }
    
    public static void main(String[] args){
        System.out.println(replace("\\/:?*<>|"));
    }
    
}
