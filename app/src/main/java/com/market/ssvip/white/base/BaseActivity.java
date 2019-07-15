package com.market.ssvip.white.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import butterknife.ButterKnife;

import com.licola.llogger.LLogger;
import com.market.ssvip.white.R;
import com.market.ssvip.white.utils.StatusBarUtil;
import com.market.ssvip.white.view.DoubleClickViewControl;
import com.market.ssvip.white.view.DoubleClickViewControl.OnDoubleClickListener;
import com.market.ssvip.white.view.WindowsController;

/**
 * Created by LiCola on 2018/4/21.
 */
public abstract class BaseActivity extends AppCompatActivity {

    private boolean debug = false;

    private static final String Life = "AtyLife:";

    //提供给子类 统一使用
    protected Context mContext;

    //抽象方法 子类必须实现
    protected abstract int getLayoutId();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;

        if (debug) {
            LLogger.d(Life + this.toString());
        }

        setContentView(getLayoutId());


        if (isButterKnifeBind()) {
            ButterKnife.bind(this);
        }

        if (isTransAndToolbar()) {
            WindowsController.setTranslucentWindows(this);
//            WindowsController.addStatusBarBackground(this, R.color.black_A12);
            StatusBarUtil.setStatusBarMode(this, true, R.color.white);

//      setToolbarFitsSystemWindows(getFitsSystemWindows());
            setToolbarLeftBack(getToolbarLeft());
            setToolbarDouble(getToolbarGroup());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (debug) {
            LLogger.d(Life + this.toString());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (debug) {
            LLogger.d(Life + this.toString());
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (onClickBack()) {
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    protected void onToolbarDoubleClick() {

    }

    /**
     * toolbar 或 返回键 响应方法
     *
     * @return true 表示拦截操作由子类处理返回逻辑 false表示不拦截 调用默认处理
     */
    protected boolean onClickBack() {
        return false;
    }

    /**
     * ButterKnife 的绑定控制方法 默认返回true使用 当子类Aty或者frag没有使用ButterKnife的注解时 不会生成 XAty_ViewBinding辅助类 绑定时反射为空
     * try会尝试从父类递归 直至系统类基类结束 目前在项目中就会递归3次 故建议没有的话直接不使用bind
     */
    protected boolean isButterKnifeBind() {
        return true;
    }

    /**
     * 是否使用透明状态栏和自定义Toolbar栏的开关方法 默认返回true使用 当不需要时候，重写返回false
     */
    protected boolean isTransAndToolbar() {
        return true;
    }


    protected View getFitsSystemWindows() {
        return null;
    }

    protected View getToolbarLeft() {
        return null;
    }

    protected View getToolbarGroup() {
        return null;
    }

    /**
     * 刷新方法，由子类选择实现
     */
    public void initView() {

    }

    private void setToolbarDouble(View toolbar) {
        if (toolbar == null) {
            return;
        }
        new DoubleClickViewControl(toolbar, new OnDoubleClickListener() {
            @Override
            public void doubleClick() {
                onToolbarDoubleClick();
            }
        });
    }

    /**
     * 设置返回按键.
     */
    private void setToolbarLeftBack(View toolbarLeft) {
        if (toolbarLeft == null) {
            return;
        }
        toolbarLeft.setVisibility(View.VISIBLE);
        toolbarLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!onClickBack()) {
                    finish();
                }
            }
        });
    }

    private void setToolbarFitsSystemWindows(View layoutToolbarGroup) {
        if (layoutToolbarGroup == null) {
            return;
        }

        WindowsController.postFitsSystemWindows(layoutToolbarGroup);
    }


    protected AlertDialog dialogLoading;

    public void onBlockDialogShow(String title) {
        if (dialogLoading == null) {
            dialogLoading = getDialogLoading(this, title);
        }
        dialogLoading.show();
    }

    public void onBlockDialogShow(@StringRes int resId) {
        onBlockDialogShow(getString(resId));
    }

    public void onBlockDialogDismiss() {
        if (dialogLoading == null) {
            return;
        }
        dialogLoading.dismiss();
        //EspressoIdlingResource.decrement();
    }

    public void showToast(String msg) {
        final Toast toast = getToast(mContext, msg);

        if (Looper.getMainLooper().getThread() == Thread.currentThread()) {
            toast.show();
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    toast.show();
                }
            });
        }

    }

    public void showToast(@StringRes int resId) {
        showToast(getString(resId));
    }

    protected abstract AlertDialog getDialogLoading(Activity activity, String title);

    protected abstract Toast getToast(Context context, String msg);

}
