package com.market.ssvip.white.utils;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * @author LiCola
 * @date 2018/11/5
 */
public class TimeUtilsTest {

  @Test
  public void formatTime() {
    assertEquals("00:00:00", TimeUtils.formatTime(0));
    assertEquals("00:00:10", TimeUtils.formatTime(10 * 1000));
    assertEquals("00:01:12", TimeUtils.formatTime(72 * 1000));

  }
}