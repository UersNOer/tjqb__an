package com.market.ssvip.white.component.image;

import android.support.annotation.IntRange;
import com.bumptech.glide.load.engine.GlideException;

/**
 * Created by LiCola on 2017/8/28.
 */

public interface OnProgressPercentListener {
  void onProgress(@IntRange(from = 0, to = 100) int progress, boolean isDone,
      GlideException exception);
}
