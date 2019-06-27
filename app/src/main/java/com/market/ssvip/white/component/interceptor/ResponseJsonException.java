package com.market.ssvip.white.component.interceptor;

import java.io.IOException;

/**
 * Created by LiCola on 2018/5/19.
 * 网络返回体异常
 * 如本应该返回json格式，但是返回其他格式内容，无法构造json解析数据
 */
public class ResponseJsonException extends IOException {

  public ResponseJsonException(String message, Throwable cause) {
    super(message, cause);
  }
}
