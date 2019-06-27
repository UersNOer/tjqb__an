package com.market.ssvip.white.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

/**
 * Created by xiao on 2015/8/7.
 */
public class WindowsController {

  /**
   * 设置透明状态栏和导航栏.
   */
  static public void setTranslucentWindows(@NonNull Activity activity) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
      //透明状态栏
      activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
      //透明导航栏
//      activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
    }
  }

  public static void hideBottomNavigationBar(Activity activity) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
      View decorView = activity.getWindow().getDecorView();
      int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
      decorView.setSystemUiVisibility(uiOptions);
    }
  }

  public static int fitsSystemWindows(View layoutToolbarGroup) {
    int toolbarHeight = layoutToolbarGroup.getHeight();
    if (toolbarHeight == 0) {
      throw new RuntimeException("请在view.post中运行该方法");
    }
    int statusBarHeight = WindowsController.getStatusBarHeight(layoutToolbarGroup.getContext());
    int toolbarGroupHeight = toolbarHeight + statusBarHeight;
    layoutToolbarGroup.setMinimumHeight(toolbarGroupHeight);
    return toolbarGroupHeight;
  }

  public static void postFitsSystemWindows(final View layoutToolbarGroup) {
    layoutToolbarGroup.post(new Runnable() {
      @Override
      public void run() {
        fitsSystemWindows(layoutToolbarGroup);
      }
    });
  }

  /**
   * 給状态栏加上一层透明色
   * 配合{@link #setTranslucentWindows(Activity)}方法使用
   * 主要方法为添加一个View并设置背景色添加到系统contentView中
   */
  static public void addStatusBarBackground(@NonNull Activity activity, @DrawableRes int resid) {
    int height;
    height = getStatusBarHeight(activity);
    if (height <= 0) {
      return;
    }
    FrameLayout layout = activity.findViewById(android.R.id.content);
    FrameLayout statusLayout = new FrameLayout(activity);
    statusLayout.setLayoutParams(
        new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));

    statusLayout.setBackgroundResource(resid);
    layout.addView(statusLayout);
  }

  /**
   * 19API以上 读取到状态栏高度才有意义
   */
  static public int getStatusBarHeight(@NonNull Context context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
      int resourceId =
          context.getResources().getIdentifier("status_bar_height", "dimen", "android");
      return resourceId > 0 ? context.getResources().getDimensionPixelSize(resourceId) : 0;
    } else {
      return 0;
    }
  }

  private static DisplayMetrics metrics = new DisplayMetrics();

  /**
   * 底部虚拟按键栏的高度
   */
  public static int getNavigationBarHeightMetrics(@NonNull Activity activity) {
    //这个方法获取可能不是真实屏幕的高度
    activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
    int usableHeight = metrics.heightPixels;//2030
    //获取当前屏幕的真实高度
    activity.getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
    int realHeight = metrics.heightPixels;

    if (realHeight > usableHeight) {
      return realHeight - usableHeight;
    } else {
      return 0;
    }
  }

  public static int getNavigationBarHeightResource(Activity activity) {
    Resources resources = activity.getResources();
    return resources.getDimensionPixelSize(
        resources.getIdentifier("navigation_bar_height", "dimen", "android"));
  }

  static public void setTransparentStatusBar(@NonNull Activity activity) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
    }
  }
}
