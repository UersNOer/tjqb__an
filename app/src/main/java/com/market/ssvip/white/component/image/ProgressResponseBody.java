package com.market.ssvip.white.component.image;

import java.io.IOException;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * Created by LiCola on 2017/8/28.
 */

class ProgressResponseBody extends ResponseBody {

  private String imageUrl;
  private ResponseBody responseBody;
  private OnProgressListener progressListener;
  private BufferedSource bufferedSource;

  public ProgressResponseBody(String imageUrl, ResponseBody responseBody,
      OnProgressListener progressListener) {
    this.imageUrl = imageUrl;
    this.responseBody = responseBody;
    this.progressListener = progressListener;
  }

  @Override
  public MediaType contentType() {
    return responseBody.contentType();
  }

  @Override
  public long contentLength() {
    return responseBody.contentLength();
  }

  @Override
  public BufferedSource source() {
    if (bufferedSource == null) {
      bufferedSource = Okio.buffer(source(responseBody.source()));
    }
    return bufferedSource;
  }

  private Source source(Source source) {
    return new ForwardingSource(source) {

      long totalBytesRead = 0;

      @Override
      public long read(Buffer sink, long byteCount) throws IOException {
        long bytesRead = super.read(sink, byteCount);
        totalBytesRead += (bytesRead == -1) ? 0 : bytesRead;//累加已读到的字节
        if (progressListener != null) {
          progressListener.onProgress(imageUrl, totalBytesRead, contentLength(), false, null);/*回调接口 网络body只回调字节信息 不能处理完成和发出异常*/
        }
        return bytesRead;
      }
    };
  }
}
