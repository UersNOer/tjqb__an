package com.market.ssvip.white.component.image;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntDef;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;
import com.market.ssvip.white.R;
import java.io.File;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by 李可乐 on 2017/2/17 0017.
 */

public class ImageLoadBuilder {

  public static final int RemoteUrl = 1;
  public static final int RemoteDownload = 2;
  public static final int NativeGif = 3;

  @IntDef({RemoteUrl, NativeGif, RemoteDownload})
  @Retention(RetentionPolicy.SOURCE)
  public @interface Mode {

  }

  public static final int FIT_CENTER = 1;
  public static final int CENTER_CROP = 2;
  public static final int CENTER_INSIDE = 3;
  public static final int CIRCLE_CROP = 4;

  @IntDef({FIT_CENTER, CENTER_CROP, CENTER_INSIDE, CIRCLE_CROP})
  @Retention(RetentionPolicy.SOURCE)
  public @interface SrcType {

  }

  private static final int RES_ID_PLACE = R.color.place;
  private static final int RES_ID_ERROR = R.color.error;

  Context mContext;
  Fragment fragment;

  /**
   * 图片显示控件 当用于下载是 可为null
   */
  ImageView imageView;

  /**
   * 图片加载类型
   */
  @Mode
  int model;
  /**
   * 图片加载的uri
   */
  Uri uri;

  /**
   * 图片加载的缩略图 url
   */
  String urlThumbnail;

  /**
   * 图片缩放方式 有默认值
   */
  @SrcType
  int srcType = CENTER_CROP;

  /**
   * 占位显示 有默认值
   */
  Drawable drawablePlace;
  /**
   * 错误显示 有默认值
   */
  Drawable drawableError;

  /**
   * 外部设置的图片宽高 默认情况下 图片框架会从显示的View大小中读取 特殊用于下载图片时
   */
  int width;
  int height;


  /**
   * 模糊参数
   */
  @IntRange(from = 0, to = 25)
  int blurRadius;
  int blurSampling;

  int roundRadius;

  OnProgressPercentListener onProgressListener;


  private final static long FADE_TIME = 300;

  public static ImageLoadBuilder download(Context context, String uri) {
    return new ImageLoadBuilder(RemoteDownload, null, context, null, Uri.parse(uri));
  }

  public static ImageLoadBuilder download(Context context, Uri uri) {
    return new ImageLoadBuilder(RemoteDownload, null, context, null, uri);
  }

  public static ImageLoadBuilder loadGif(@NonNull ImageView imageView, File file) {
    return new ImageLoadBuilder(NativeGif, imageView, imageView.getContext(), null,
        Uri.fromFile(file));
  }

  public static ImageLoadBuilder load(@NonNull ImageView imageView, String uri) {
    return new ImageLoadBuilder(RemoteUrl, imageView, imageView.getContext(), null, Uri.parse(uri));
  }

  public static ImageLoadBuilder load(@NonNull ImageView imageView, File file) {
    return new ImageLoadBuilder(RemoteUrl, imageView, imageView.getContext(), null,
        Uri.fromFile(file));
  }

  public static ImageLoadBuilder load(@NonNull ImageView imageView, Uri uri) {
    return new ImageLoadBuilder(RemoteUrl, imageView, imageView.getContext(), null, uri);
  }


  private ImageLoadBuilder(@Mode int model, ImageView imageView, Context context, Fragment fragment,
      Uri uri) {
    this.model = model;
    this.mContext = context;
    this.imageView = imageView;
    this.uri = uri;
    this.fragment = fragment;
  }

  @NonNull
  public ImageLoadGlide build() {

    if (checkUserVisible()) {
      //当前加载对用户可见 如果占位/错误等提示图为空 则设置默认值
      if (drawablePlace == null) {
        drawablePlace = ContextCompat.getDrawable(mContext, RES_ID_PLACE);
      }
      if (drawableError == null) {
        drawableError = ContextCompat.getDrawable(mContext, RES_ID_ERROR);
      }
    }

    //if (onProgressListener != null && !checkRemoteMode()) {
    //  //百分比进度接口实例非空 且非远程加载方式 错误的配置 则置空
    //  onProgressListener = null;
    //}
    return new ImageLoadGlide(this);
  }

  private boolean checkRemoteMode() {
    return model == RemoteUrl || model == RemoteDownload;
  }

  private boolean checkUserVisible() {
    return model != RemoteDownload;
  }

  @NonNull
  public ImageLoadBuilder setDrawablePlace(@DrawableRes int drawablePlaceId) {
    return setDrawablePlace(ContextCompat.getDrawable(mContext, drawablePlaceId));
  }

  @NonNull
  public ImageLoadBuilder setDrawablePlace(Drawable drawablePlace) {
    this.drawablePlace = drawablePlace;
    return this;
  }

  @NonNull
  public ImageLoadBuilder setDrawableError(@DrawableRes int drawableErrorId) {
    return setDrawableError(ContextCompat.getDrawable(mContext, drawableErrorId));
  }

  @NonNull
  public ImageLoadBuilder setDrawableError(Drawable drawableError) {
    this.drawableError = drawableError;
    return this;
  }

  @NonNull
  public ImageLoadBuilder setSrcType(@SrcType int srcType) {
    this.srcType = srcType;
    return this;
  }

  @NonNull
  public ImageLoadBuilder setDrawableSize(int width, int height) {
    this.width = width;
    this.height = height;
    return this;
  }

  @NonNull
  public ImageLoadBuilder setOnProgressListener(OnProgressPercentListener onProgressListener) {
    this.onProgressListener = onProgressListener;
    return this;
  }

  @NonNull
  public ImageLoadBuilder setBlur(int blurRadius, int blurSampling) {
    this.blurRadius = blurRadius;
    this.blurSampling = blurSampling;
    return this;
  }

  @NonNull
  public ImageLoadBuilder setRoundRadius(int roundRadius) {
    this.roundRadius = roundRadius;
    return this;
  }

  @NonNull
  public ImageLoadBuilder setRoundRadiusDimen(@DimenRes int dimenId) {
    this.roundRadius = mContext.getResources().getDimensionPixelSize(dimenId);
    return this;
  }


}
