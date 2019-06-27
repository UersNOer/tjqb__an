package com.market.ssvip.white.component.image;

import com.bumptech.glide.load.engine.GlideException;

/**
 * Created by LiCola on 2017/8/28.
 */

public interface OnProgressListener {
  void onProgress(String imageUrl, long bytesRead, long totalBytes, boolean isDone,
      GlideException exception);
}
