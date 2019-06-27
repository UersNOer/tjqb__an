package com.market.ssvip.white.component.interceptor;

import java.io.IOException;

/**
 * @author LiCola
 * @date 2018/10/31
 */
public class StateCodeException extends IOException {

  public StateCodeException(String message) {
    super(message);
  }
}
