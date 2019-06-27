package com.market.ssvip.white.component.interceptor;

import android.support.annotation.NonNull;
import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by LiCola on 2018/7/10.
 * 自定义拦截器
 * 只有成功的网络返回（200<=code<300）能够通过。
 * 1：OkHttp只负责网络层通信成功，不负责应用层是否成功（500也是成功的请求，只是服务器错误）。
 * 2：Retrofit依赖OkHttp则通过{@link retrofit2.Response#error(ResponseBody, Response)}返回一个带有错误body的响应体。
 *
 * 该拦截器保证外层使用OkHttp时只有成功（网络层/应用层）的网络返回才能通过
 */
public class OnlySuccessInterceptor implements Interceptor {

  @Override
  public Response intercept(@NonNull Chain chain) throws IOException {
    Request request = chain.request();
    Response response = chain.proceed(request);

    if (!response.isSuccessful()) {
      throw new RemoteServerException("网络请求成功，服务器返回异常");
    }

    return response;
  }
}
