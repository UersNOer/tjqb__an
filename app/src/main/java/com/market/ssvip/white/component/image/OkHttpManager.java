package com.market.ssvip.white.component.image;

import com.bumptech.glide.load.engine.GlideException;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import okhttp3.Call.Factory;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by LiCola on 2017/8/28.
 */

class OkHttpManager {

  private OkHttpManager() {

  }

  public static Factory getOkHttpClient() {
    return okHttpClient;
  }

  /**
   * 静态变量以弱引用方式持有 外部接口实例 且为引入同步锁防止读写不同步
   */
  private static List<WeakReference<OnProgressListener>> progressListeners =
      Collections.synchronizedList(new ArrayList<WeakReference<OnProgressListener>>());

  /**
   * 构造的全局OkHttp实例
   */
  private static OkHttpClient okHttpClient =
      new OkHttpClient.Builder().addNetworkInterceptor(new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
          Request request = chain.request();
          Response response = chain.proceed(request);
          return response.newBuilder()
              .body(new ProgressResponseBody(request.url().toString(), response.body(), Listener))
              .build();
        }
      }).build();

  private static final OnProgressListener Listener = new OnProgressListener() {

    @Override
    public void onProgress(String imageUrl, long bytesRead, long totalBytes, boolean isDone,
        GlideException exception) {
      //LLogger.d("imageUrl:"
      //    + imageUrl
      //    + " bytesRead:"
      //    + bytesRead
      //    + " totalBytes:"
      //    + totalBytes
      //    + " isDone:"
      //    + isDone
      //    + " exception:"
      //    + exception);
      if (progressListeners == null || progressListeners.isEmpty()) {
        return;
      }

      for (int i = 0, size = progressListeners.size(); i < size; i++) {
        WeakReference<OnProgressListener> weakReference = progressListeners.get(i);
        OnProgressListener onProgressListener = weakReference.get();
        if (onProgressListener == null) {
          progressListeners.remove(i);
        } else {
          onProgressListener.onProgress(imageUrl, bytesRead, totalBytes, isDone, exception);
        }
      }
    }
  };

  public static void addProgressListener(OnProgressListener progressListener) {
    if (progressListener == null) {
      return;
    }

    if (findProgressListener(progressListener) == null) {
      progressListeners.add(new WeakReference<OnProgressListener>(progressListener));
    }
  }

  public static void removeProgressListener(OnProgressListener progressListener) {
    if (progressListener == null) {
      return;
    }

    WeakReference<OnProgressListener> listenerWeakReference =
        findProgressListener(progressListener);
    if (listenerWeakReference != null) {
      progressListeners.remove(listenerWeakReference);
    }
  }

  private static WeakReference<OnProgressListener> findProgressListener(
      OnProgressListener progressListener) {
    if (progressListener == null || progressListeners == null || progressListeners.isEmpty()) {
      return null;
    }

    for (int i = 0, size = progressListeners.size(); i < size; i++) {
      WeakReference<OnProgressListener> listenerWeakReference = progressListeners.get(i);
      if (listenerWeakReference.get() == progressListener) {
        return listenerWeakReference;
      }
    }

    return null;
  }
}
