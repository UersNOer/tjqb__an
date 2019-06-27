package com.market.ssvip.white.view;

import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by 李可乐 on 2017/3/8 0008.
 */

public class SwipeRefreshUtils {

  public static final int DelayedTime = 250;

  public static void delayedLoading(final SwipeRefreshLayout swipeRefresh, final boolean isOpen) {
    if (swipeRefresh == null) {
      return;
    }

    swipeRefresh.postDelayed(new Runnable() {
      @Override
      public void run() {
        swipeRefresh.setRefreshing(isOpen);
      }
    }, isOpen ? 0 : DelayedTime);//post的本质也是 delay=0 使用性能损耗
  }

  public static void disableScroll(final SwipeRefreshLayout swipeRefresh){
    if (swipeRefresh==null){
      return;
    }

    swipeRefresh.setEnabled(false);
  }


  public static void fixScrollConflict(@NonNull final SwipeRefreshLayout mSwipeRefresh,
      @NonNull ViewPager viewPager) {
    viewPager.setOnTouchListener(new View.OnTouchListener() {
      @Override
      public boolean onTouch(View v, @NonNull MotionEvent event) {
        switch (event.getAction()) {
          case MotionEvent.ACTION_MOVE:
            mSwipeRefresh.setEnabled(false);
            break;
          case MotionEvent.ACTION_UP:
          case MotionEvent.ACTION_CANCEL:
            mSwipeRefresh.setEnabled(true);
            break;
        }
        return false;
      }
    });
  }

  public static void fixScrollConflict(@NonNull final SwipeRefreshLayout mSwipeRefresh,
      @NonNull RecyclerView viewPager) {
    viewPager.setOnTouchListener(new View.OnTouchListener() {
      @Override
      public boolean onTouch(View v, @NonNull MotionEvent event) {
        switch (event.getAction()) {
          case MotionEvent.ACTION_MOVE:
            mSwipeRefresh.setEnabled(false);
            break;
          case MotionEvent.ACTION_UP:
          case MotionEvent.ACTION_CANCEL:
            mSwipeRefresh.setEnabled(true);
            break;
        }
        return false;
      }
    });
  }
}
