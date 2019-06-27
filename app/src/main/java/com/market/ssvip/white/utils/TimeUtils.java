package com.market.ssvip.white.utils;

import java.util.Locale;

/**
 * @author LiCola
 * @date 2018/11/5
 */
public class TimeUtils {

  /**
   * 时间单位 毫秒
   * @param timeMill
   * @return
   */
  public static String formatTime(long timeMill){

    int timeSecond= (int) (timeMill/1000);
    String format="%02d:%02d:%02d";
    return String.format(Locale.CHINA,format,timeSecond/360,timeSecond/60,timeSecond%60);
  }
}
