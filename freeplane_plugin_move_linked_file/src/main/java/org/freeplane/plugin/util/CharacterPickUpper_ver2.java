/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.freeplane.plugin.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Issey
 */
public class CharacterPickUpper_ver2 {

    public static String pickUp(String originalString) throws Exception {
        /**
         * 文字列を縮める
         */
        String regex = "(.{0,30}).*";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(originalString);
        String Character30;
        if (m.find()) {
            Character30 = m.group(1);
        } else {
            throw new Exception();
        }
        return Character30;
    }
    
    public static String pickUp(String originalString, int length) throws Exception {
        /**
         * 文字列を縮める
         */
        String regex = "(.{0," + String.valueOf(length) +"}).*";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(originalString);
        String characters;
        if (m.find()) {
            characters = m.group(1);
        } else {
            throw new Exception();
        }
        return characters;
    }
}
