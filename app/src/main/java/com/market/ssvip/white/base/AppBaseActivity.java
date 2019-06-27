package com.market.ssvip.white.base;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Toast;
import com.market.ssvip.white.R;
import com.market.ssvip.white.view.Toaster;

/**
 * Created by LiCola on 2018/4/21.
 */
public abstract class AppBaseActivity extends BaseActivity {

  @Override
  protected View getToolbarLeft() {
    return findViewById(R.id.layout_toolbar_left);
  }

  @Override
  protected View getFitsSystemWindows() {
    return findViewById(R.id.layout_toolbar_group);
  }

  @Override
  protected AlertDialog getDialogLoading(Activity activity, String title) {
    return null;
  }

  @Override
  protected Toast getToast(Context context, String msg) {
    Toast toast = Toaster.makeToast(context, msg);
    toast.setDuration(Toast.LENGTH_SHORT);
    return toast;
  }
}
