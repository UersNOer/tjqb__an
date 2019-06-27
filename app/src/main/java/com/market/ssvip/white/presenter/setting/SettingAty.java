package com.market.ssvip.white.presenter.setting;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.market.ssvip.white.BuildConfig;
import com.market.ssvip.white.R;
import com.market.ssvip.white.base.AppBaseActivity;

/**
 * @author LiCola
 * @date 2018/10/31
 */
public class SettingAty extends AppBaseActivity {

  @BindView(R.id.iv_toolbar_left)
  ImageView ivToolbarLeft;
  @BindView(R.id.layout_toolbar_left)
  FrameLayout layoutToolbarLeft;
  @BindView(R.id.tv_toolbar_title)
  TextView tvToolbarTitle;
  @BindView(R.id.toolbar_center)
  LinearLayout toolbarCenter;
  @BindView(R.id.iv_toolbar_right)
  ImageView ivToolbarRight;
  @BindView(R.id.layout_toolbar_right)
  FrameLayout layoutToolbarRight;
  @BindView(R.id.toolbar)
  Toolbar toolbar;
  @BindView(R.id.layout_toolbar_group)
  FrameLayout layoutToolbarGroup;
  @BindView(R.id.layout_toolbar_root)
  LinearLayout layoutToolbarRoot;
  @BindView(R.id.tv_app_version)
  TextView tvAppVersion;
  @BindView(R.id.layout_setting_version)
  LinearLayout layoutSettingVersion;
  @BindView(R.id.layout_setting_login_out)
  FrameLayout layoutSettingLoginOut;

  @Override
  protected int getLayoutId() {
    return R.layout.module_aty_setting;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    layoutToolbarGroup.setBackgroundResource(R.color.white);
    tvAppVersion.setText(BuildConfig.VERSION_NAME);
  }

  @OnClick(R.id.layout_setting_version)
  public void onVersionClick(View view) {

  }

  @OnClick(R.id.layout_setting_login_out)
  public void onLoginOutClick(View view) {
    setResult(Activity.RESULT_OK);
    finish();
  }
}
