/*
 * http://www.javaroad.jp/java_date2.htm
 */
package org.freeplane.plugin.util;

import java.util.Calendar;

public class Clock_ver2 {

    //フィールド
    private int year;        //(2)現在の年
    private int month;  //(3)現在の月
    private int day;         //(4)現在の日
    private int hour; //(5)現在の時
    private int minute;    //(6)現在の分
    private int second;    //(7)現在の秒
    private StringBuffer dow; //(8)現在の曜日を取得

    //コンストラクタ
    public Clock_ver2() {
        Calendar cal1 = Calendar.getInstance();  //(1)オブジェクトの生成

        year = cal1.get(Calendar.YEAR);        //(2)現在の年を取得
        month = cal1.get(Calendar.MONTH) + 1;  //(3)現在の月を取得
        day = cal1.get(Calendar.DATE);         //(4)現在の日を取得
        hour = cal1.get(Calendar.HOUR_OF_DAY); //(5)現在の時を取得
        minute = cal1.get(Calendar.MINUTE);    //(6)現在の分を取得
        second = cal1.get(Calendar.SECOND);    //(7)現在の秒を取得

        dow = new StringBuffer();
        switch (cal1.get(Calendar.DAY_OF_WEEK)) {  //(8)現在の曜日を取得
            case Calendar.SUNDAY:
                dow.append("日曜日");
                break;
            case Calendar.MONDAY:
                dow.append("月曜日");
                break;
            case Calendar.TUESDAY:
                dow.append("火曜日");
                break;
            case Calendar.WEDNESDAY:
                dow.append("水曜日");
                break;
            case Calendar.THURSDAY:
                dow.append("木曜日");
                break;
            case Calendar.FRIDAY:
                dow.append("金曜日");
                break;
            case Calendar.SATURDAY:
                dow.append("土曜日");
                break;


        }
    }
    
    /**
     * 「2015_4_29 水曜日 15:33:2」
     * の形式で日時を取得
     * @return 
     */
    public String getCurrentTime_Format1(){
         return year + "_" + month + "_" + day + " " + dow 
                + " " + hour + ":" + minute + ":" + second;
    }
    
    /**
     * 「2015_4_29 水曜日 15:33:2」
     * の形式で日時を取得
     * @return 
     */
    public String getCurrentTime_Format2(){
        return String.format("%04d-%02d-%02d-%02d-%02d-%02d",year,month,day,hour,minute,second);
    }
    
    

    public static void main(String[] args) {
        
        Clock_ver2 clock = new Clock_ver2();
        System.out.println(clock.getCurrentTime_Format1());
        System.out.println(clock.getCurrentTime_Format2());
       
    }
}
