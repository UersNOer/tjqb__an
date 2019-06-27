package com.market.ssvip.white.utils;

/**
 * @author LiCola
 * @date 2018/10/31
 */
public class ExceptionHelper {

  public static String mapperException(Throwable throwable) {
    if (throwable == null) {
      return "未知错误";
    }

    String message = throwable.getMessage();
    if (CheckUtils.isEmpty(message)) {
      message = "未知异常类型";
    }
    return message;
  }
}
