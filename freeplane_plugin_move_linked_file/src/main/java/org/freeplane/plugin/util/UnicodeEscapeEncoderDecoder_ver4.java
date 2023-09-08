/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.freeplane.plugin.util;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * ユニコードエスケープ形式
 *
 * - #u1234; 形式 - &#1234; 形式 （HTML用10進数） - &#x1234;形式（HTML用16進数）
 *
 * Source: - http://yasu0120.blog130.fc2.com/blog-entry-17.html -
 * Google「Javaでの文字列コードの扱い」「MyMemoWiki」
 */
public class UnicodeEscapeEncoderDecoder_ver4 {

    public static void main(String[] args) {


        //System.out.println(UnicodeEscapeEncode("テストクラす"));
        //System.out.println(UnicodeEscapeEncode(" "));
        //System.out.println(UnicodeEscapeEncode("あ"));
        //System.out.println(UnicodeEscapeEncode("\n"));
        //System.out.println(UnicodeEscapeEncode("&"));
        //System.out.println(UnicodeEscape16Decode("9589 585e"));

        //System.out.println(UnicodeEscape10Decode("&#12354"));

        //System.out.println(UnicodeEscape10Decode("&#12354;&#12356;&#12358;"));
        System.out.println(isUnicodeEscaped("テストクラす"));
        //System.out.println(isUnicodeEscaped("&#12354;&#12356;&#12358;"));
        System.out.println(isUnicodeEscaped("abcd&#12354;12356&#12358;"));
        System.out.println(UnicodeEscape10Decode("abcd&#12354;12356;&#123;"));
        System.out.println(UnicodeEscape10Decode("CAGDAS ALTIN&#27663;"));
        

    }

    public static boolean isUnicodeEscaped(String escapedString) {
        escapedString = escapedString.replaceAll("&#", "■&#");
        escapedString = escapedString.replaceAll(";", ";■");
        //System.out.println(escapedString);
        String[] escapedCharacter = escapedString.split("■");
        String regex = "&#([0-9]{5});";
        Pattern p = Pattern.compile(regex);
        int result = 0;

        for (String str : escapedCharacter) {
            Matcher m = p.matcher(str);
            if (m.find()) {
                //System.out.println("部分マッチしました ");
                result += 1;
            } else {
                //System.out.println(" 部分マッチしませんでした");
                result += 0;
            }
        }
        return result>0;
    }

    /**
     * 「&#12354;&#12356;&#12358;」⇒「あいう」と変換するメソッド
     * 以下の様な混在タイプにも対応
     *「abcd&#12354;12356;&#12358;」⇒「abcdあ12356;う」
     * 「abcd&#12354;12356;&#123;」⇒「abcdあ12356;&#123;」
     * @param escapedString
     * @param type
     * @return
     */
    public static String UnicodeEscape10Decode(String escapedString) {
        escapedString = escapedString.replaceAll("&#", "■&#");
        escapedString = escapedString.replaceAll(";", ";■");
        //System.out.println(escapedString);
        String[] escapedCharacter = escapedString.split("■");
        String regex = "&#([0-9]{5});";
        Pattern p = Pattern.compile(regex);
        String resultString = "";

        for (String str : escapedCharacter) {
            Matcher m = p.matcher(str);
            if (m.find()) {
                //System.out.println("部分マッチしました ");
                resultString += (new Character((char) Integer.parseInt(m.group(1), 10))).toString();//ユニコード文字に変換して戻り値へ
            } else {
                //System.out.println(" 部分マッチしませんでした");
                resultString += str;//そのまま変換なしに戻り値へ
            }
        }
        return resultString;
    }

    public static String UnicodeEscape16Decode(String x1234s) {

        StringBuffer result = new StringBuffer();
        StringTokenizer tok = new StringTokenizer(x1234s.trim(), " ");//StringTokenizerの使用は非推奨とのこと

        List list = new ArrayList();
        while (tok.hasMoreTokens()) {
            list.add(new Character((char) Integer.parseInt(tok.nextToken(), 16)));
        }
        char[] buf = new char[list.size()];
        for (int i = 0; i < list.size(); i++) {
            buf[i] = ((Character) list.get(i)).charValue();
        }
        return (new String(buf));
    }

    public static String Escape10ToUnicode(String x1234s) {

        StringBuffer result = new StringBuffer();
        StringTokenizer tok = new StringTokenizer(x1234s.trim(), " ");//StringTokenizerの使用は非推奨とのこと

        List list = new ArrayList();
        while (tok.hasMoreTokens()) {
            list.add(new Character((char) Integer.parseInt(tok.nextToken(), 10)));
        }
        char[] buf = new char[list.size()];
        for (int i = 0; i < list.size(); i++) {
            buf[i] = ((Character) list.get(i)).charValue();
        }
        return (new String(buf));
    }

    /**
     * 「&#12345」といった１０進数ユニコードエスケープ形式の文字列に当てはまった場合にユニコード文字にデコードする。
     */
    public static String UnicodeEscapeEncode(String value) {

        /*
         * 「￥uXXXX」形式で表記された対象の文字列をUnicoddeに変換する _0-9a-zA-Zはそのまま表示する
         *
         */

        if (value == null) {
            return "";
        }


        char[] charValue = value.toCharArray();

        StringBuilder result = new StringBuilder();

        for (char ch : charValue) {
            if (ch != '_' && !(ch >= '0' && '9' >= ch) && !(ch >= 'a' && 'z' >= ch) && !(ch >= 'A' && 'Z' >= ch)) {
                String unicodeCh = Integer.toHexString((int) ch);

                result.append("\\u");

                for (int i = 0; i < 4 - unicodeCh.length(); i++) {
                    result.append("0");
                }
                result.append(unicodeCh);

            } else {
                result.append(ch);
            }

        }

        return result.toString();

    }
}
