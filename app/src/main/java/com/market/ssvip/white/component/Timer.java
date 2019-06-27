package com.market.ssvip.white.component;


import android.os.Handler;
import android.os.SystemClock;

/**
 * Created by LiCola on 2018/5/31.
 */
public class Timer implements Runnable {

  private static final long GAP_TIME = 1000;

  private Handler handler;
  private long beginTime;
  private long endTime;

  private boolean isStop = false;

  private OnTimeListener onTimeListener;

  public Timer(Handler handler, OnTimeListener onTimeListener) {
    this.handler = handler;
    this.onTimeListener = onTimeListener;
  }

  @Override
  public void run() {

    if (isStop) {
      return;
    }

    long now = SystemClock.uptimeMillis();
    if (now >= endTime) {
      onTimeListener.onEnd();
    } else {
      onTimeListener.onWork(now - beginTime);
      handler.postDelayed(this, GAP_TIME);
    }
  }

  private void start() {
    handler.post(this);
  }

  public void stop() {
    this.isStop = true;
    handler.removeCallbacks(this);
  }

  public void restart(long endMillis) {
    this.beginTime = SystemClock.uptimeMillis();
    this.endTime = beginTime + endMillis;
    start();
  }

  public interface OnTimeListener {

    void onWork(long duration);

    void onEnd();
  }
}
