package com.market.ssvip.white.component.interceptor;

import java.io.IOException;

/**
 * Created by LiCola on 2018/7/10.
 */
public class RemoteServerException extends IOException {

  public RemoteServerException(String message) {
    super(message);
  }
}
