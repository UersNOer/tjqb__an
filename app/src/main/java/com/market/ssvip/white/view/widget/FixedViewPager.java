package com.market.ssvip.white.view.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by st0rm23 on 2016/2/10.
 * 固定的不可滑动的 FixedViewPager 通过不拦截点击滑动事件实现 不可滑动
 */
public class FixedViewPager extends ViewPager {

  private boolean isPagingEnabled = false;

  public FixedViewPager(Context context) {
    super(context);
  }

  public FixedViewPager(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    return this.isPagingEnabled && super.onTouchEvent(event);
  }

  @Override
  public boolean onInterceptTouchEvent(MotionEvent event) {
    return this.isPagingEnabled && super.onInterceptTouchEvent(event);
  }

  public void setPagingEnabled(boolean b) {
    this.isPagingEnabled = b;
  }
}
