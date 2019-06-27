package com.market.ssvip.white.component.image;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CenterInside;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.bumptech.glide.request.target.Target;
import com.licola.llogger.LLogger;
import java.io.File;
import java.util.ArrayList;
import jp.wasabeef.glide.transformations.BlurTransformation;

/**
 * Created by 李可乐 on 2017/2/17 0017.
 */

class ImageLoadGlide {

  private static final int DEFAULT_SIZE = 1000;

  private FutureTarget<File> futureTarget;

  ImageLoadGlide(@NonNull ImageLoadBuilder builder) {

    GlideRequests requestManager = getRequestManager(builder);

    switch (builder.model) {
      case ImageLoadBuilder.RemoteUrl:
        loadUrl(builder, getRequestOptions(builder), requestManager);
        break;
      case ImageLoadBuilder.RemoteDownload:
        futureTarget = download(builder, requestManager);
        break;
      case ImageLoadBuilder.NativeGif:
        loadNativeGif(builder, getRequestOptions(builder), requestManager);
        break;
      default:
        throw new IllegalArgumentException("请配置支持的图片加载方式");
    }
  }

  @NonNull
  private RequestOptions getRequestOptions(@NonNull ImageLoadBuilder builder) {
    RequestOptions options = new RequestOptions();
    if (builder.drawableError != null) {
      options.error(builder.drawableError);
    }
    if (builder.drawablePlace != null) {
      options.placeholder(builder.drawablePlace);
    }

    ArrayList<Transformation<Bitmap>> transformations = new ArrayList<>();

    switch (builder.srcType) {
      case ImageLoadBuilder.CENTER_CROP:
        transformations.add(new CenterCrop());
        break;
      case ImageLoadBuilder.CENTER_INSIDE:
        transformations.add(new CenterInside());
        break;
      case ImageLoadBuilder.FIT_CENTER:
        transformations.add(new FitCenter());
        break;
      case ImageLoadBuilder.CIRCLE_CROP:
        transformations.add(new CircleCrop());
        break;
      default:
        transformations.add(new CenterCrop());
        break;
    }

    if (builder.height > 0 && builder.width > 0) {
      options.override(builder.width, builder.height);
    }

    if (builder.blurRadius > 0 && builder.blurSampling > 0) {
      transformations.add(new BlurTransformation(builder.blurRadius, builder.blurSampling));
    }

    if (builder.roundRadius > 0) {
      transformations.add(new RoundedCorners(builder.roundRadius));
    }

    options.transform(new MultiTransformation<>(transformations));

    return options;
  }

  public FutureTarget<File> getFutureTarget() {
    return futureTarget;
  }

  private FutureTarget<File> download(@NonNull final ImageLoadBuilder builder,
      final GlideRequests requestManager) {

    int width = builder.width > 0 ? builder.width : DEFAULT_SIZE;
    int height = builder.height > 0 ? builder.height : DEFAULT_SIZE;

    return requestManager
        .downloadOnly()
        .load(builder.uri)
        .submit(width, height);
  }

  private void loadNativeGif(@NonNull final ImageLoadBuilder builder, RequestOptions options,
      final GlideRequests requestManager) {

    final Uri uri = builder.uri;

    GlideRequest<GifDrawable> gifDrawableGlideRequest = requestManager.asGif();

    gifDrawableGlideRequest
        .apply(options)
        .load(uri)
        .into(new ImageViewTarget<GifDrawable>(builder.imageView) {
          @Override
          protected void setResource(@Nullable GifDrawable resource) {
            LLogger.d(resource);
            if (resource!=null){
              resource.startFromFirstFrame();
              builder.imageView.setImageDrawable(resource);
            }
          }
        });
  }

  private void loadUrl(@NonNull final ImageLoadBuilder builder, RequestOptions options,
      GlideRequests requestManager) {

    final Uri uri = builder.uri;

    RequestBuilder<Drawable> drawableRequestBuilder = requestManager.asDrawable();
    String urlThumbnail = builder.urlThumbnail;
    if (urlThumbnail != null && !urlThumbnail.isEmpty()) {
      RequestBuilder<Drawable> thumbnailRequest =
          getRequestManager(builder).load(urlThumbnail);
      drawableRequestBuilder.thumbnail(thumbnailRequest);
    }

    RequestBuilder<Drawable> requestBuilder = drawableRequestBuilder
        .apply(options)
        .load(uri);
    if (builder.onProgressListener != null) {
      setProgressListener(builder, uri, requestBuilder);
    }

    requestBuilder.into(builder.imageView);
  }

  private void setProgressListener(@NonNull ImageLoadBuilder builder, Uri uri,
      RequestBuilder<Drawable> requestBuilder) {
    final String url = uri.toString();

    final OnProgressListener progressListener =
        CoverProgressListener(url, builder.onProgressListener, builder.imageView.getHandler());
    OkHttpManager.addProgressListener(progressListener);
    requestBuilder.listener(new RequestListener<Drawable>() {
      @Override
      public boolean onLoadFailed(@Nullable GlideException e, Object model,
          Target<Drawable> target, boolean isFirstResource) {
        progressListener.onProgress(url, 0, 0, true, e);//glide实际的失败 进度完成 状态错误
        OkHttpManager.removeProgressListener(progressListener);
        return false;
      }

      @Override
      public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target,
          DataSource dataSource, boolean isFirstResource) {
        progressListener.onProgress(url, 0, 0, true, null);//glide实际的完成 进度完成 状态正确
        OkHttpManager.removeProgressListener(progressListener);
        return false;
      }
    });
  }

  @NonNull
  private OnProgressListener CoverProgressListener(@NonNull final String url,
      @NonNull final OnProgressPercentListener percentListener, final Handler handler) {
    return new OnProgressListener() {

      int lastPercent = 0;

      @Override
      public void onProgress(String imageUrl, final long bytesRead, final long totalBytes,
          final boolean isDone, final GlideException exception) {
        if (!url.equals(imageUrl)) {
          return;
        }

        if (isDone) {
          postMainThread(handler, new Runnable() {
            @Override
            public void run() {
              percentListener.onProgress(100, true, exception);
            }
          });
        } else {
          final int percent = (int) ((bytesRead * 1.0f) / totalBytes * 100.0f);
          if (percent - lastPercent == 0) {
            return;
          }
          lastPercent = percent;
          postMainThread(handler, new Runnable() {
            @Override
            public void run() {
              percentListener.onProgress(percent, false, exception);
            }
          });
        }
      }
    };
  }

  private void postMainThread(Handler handler, Runnable runnable) {
    handler.post(runnable);
  }


  private GlideRequests getRequestManager(@NonNull ImageLoadBuilder builder) {
    GlideRequests requestManager;
    if (builder.fragment != null) {
      requestManager = GlideApp.with(builder.fragment);
    } else {
      requestManager = GlideApp.with(builder.mContext);
    }
    return requestManager;
  }
}
