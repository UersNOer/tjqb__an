package com.market.ssvip.white.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.annotation.VisibleForTesting;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.licola.llogger.LLogger;

/**
 * Created by 李可乐 on 2017/3/2 0002.
 */

public abstract class BaseFragment extends Fragment {

  private boolean debug = false;

  private static final String Life = "FragLife:";

  protected Context mContext;

  protected View viewRoot;

  private Unbinder mUnbinder;

  /**
   * 该frag对用户是否可见 主要用在嵌套在ViewPager中的子类
   */
  protected boolean isVisibleToUser;

  //抽象方法 子类必须实现
  protected abstract int getLayoutId();

  /**
   * 刷新方法，由子类选择实现
   */
  public void initView() {

  }

  /**
   * 返回顶部的方法，由子类选择实现
   */
  public void backToTop() {

  }

  /**
   * 交互方法 由子类选择实现
   */
  public void onDispatchClick() {

  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    mContext = context;
    if (debug) {
      LLogger.d(Life + this.toString());
    }
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (debug) {
      LLogger.d(Life + this.toString());
    }
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    viewRoot = inflater.inflate(getLayoutId(), container, false);
    mUnbinder = ButterKnife.bind(this, viewRoot);
    if (debug) {
      LLogger.d(Life + this.toString());
    }
    return viewRoot;
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    if (debug) {
      LLogger.d(Life + this.toString());
    }
  }

  @Override
  public void onStart() {
    super.onStart();
    if (debug) {
      LLogger.d(Life + this.toString());
    }
  }

  @Override
  public void onResume() {
    super.onResume();
    if (debug) {
      LLogger.d(Life + this.toString());
    }
  }

  @Override
  public void onPause() {
    super.onPause();
    if (debug) {
      LLogger.d(Life + this.toString());
    }
  }

  @Override
  public void onStop() {
    super.onStop();
    if (debug) {
      LLogger.d(Life + this.toString());
    }
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    if (mUnbinder != null) {
      mUnbinder.unbind();
    }
    if (debug) {
      LLogger.d(Life + this.toString());
    }
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    if (debug) {
      LLogger.d(Life + this.toString());
    }
  }

  @Override
  public void onDetach() {
    super.onDetach();
    if (debug) {
      LLogger.d(Life + this.toString());
    }
  }

  public boolean onKeyDown(int keyCode, KeyEvent event) {
    return false;
  }

  public void onFinish() {
    FragmentActivity activity = getActivity();
    if (activity == null) {
      return;
    }
    activity.finish();
  }

  @Override
  public void setUserVisibleHint(boolean isVisibleToUser) {
    super.setUserVisibleHint(isVisibleToUser);
    this.isVisibleToUser = isVisibleToUser;
  }

  public void onBlockDialogDismiss() {
    FragmentActivity activity = getActivity();
    if (activity == null) {
      return;
    }
    if (activity instanceof BaseActivity) {
      ((BaseActivity) activity).onBlockDialogDismiss();
    } else {
      LLogger.e("错误的Fragment寄主，没有onBlockDialogDismiss方法");
    }
  }

  public void onBlockDialogShow(int resId) {
    FragmentActivity activity = getActivity();
    if (activity instanceof BaseActivity) {
      ((BaseActivity) activity).onBlockDialogShow(resId);
    } else {
      LLogger.e("错误的Fragment寄主，没有onBlockDialogShow方法");
    }
  }

  public void onBlockDialogShow(String title) {
    FragmentActivity activity = getActivity();
    if (activity instanceof BaseActivity) {
      ((BaseActivity) activity).onBlockDialogShow(title);
    } else {
      LLogger.e("错误的Fragment寄主，没有onBlockDialogShow方法");
    }
  }

  public void showToast(String msg) {
    FragmentActivity activity = getActivity();

    if (activity instanceof BaseActivity) {
      ((BaseActivity) activity).showToast(msg);
    } else {
      LLogger.e("错误的Fragment寄主，没有showToast方法");
    }
  }

  public void showToast(@StringRes int resId) {
    FragmentActivity activity = getActivity();

    if (activity instanceof BaseActivity) {
      ((BaseActivity) activity).showToast(resId);
    } else {
      LLogger.e("错误的Fragment寄主，没有showToast方法");
    }
  }

  @VisibleForTesting
  public View getViewRoot() {
    return viewRoot;
  }
}
