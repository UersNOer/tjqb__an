package com.market.ssvip.white.presenter.user;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import com.market.ssvip.white.R;
import com.market.ssvip.white.base.AppBaseActivity;

/**
 * @author LiCola
 * @date 2018/10/31
 */
public class MineApplyAty extends AppBaseActivity {

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

  @Override
  protected int getLayoutId() {
    return R.layout.module_aty_mine_apply;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    tvToolbarTitle.setText("我的申请");
  }
}
