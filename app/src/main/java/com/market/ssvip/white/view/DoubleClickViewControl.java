package com.market.ssvip.white.view;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by 李可乐 on 2017/1/10 0010.
 * 双击返回的控制类
 */

public class DoubleClickViewControl {

  private OnDoubleClickListener onDoubleClickListener;
  private View detectorView;

  public DoubleClickViewControl(View detectorView,OnDoubleClickListener onDoubleClickListener) {
    this.detectorView = detectorView;
    this.onDoubleClickListener = onDoubleClickListener;
    initListener();
  }

  private final GestureDetector.SimpleOnGestureListener gestureListener =
      new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onDoubleTap(MotionEvent e) {
          onDoubleClickListener.doubleClick();
          return super.onDoubleTap(e);
        }
      };

  private void initListener() {
    final GestureDetector gestureDetector =
        new GestureDetector(detectorView.getContext(), gestureListener);

    detectorView.setOnTouchListener(new View.OnTouchListener() {
      @Override
      public boolean onTouch(View v, MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
      }
    });
  }

  public interface OnDoubleClickListener {

    void doubleClick();
  }
}
