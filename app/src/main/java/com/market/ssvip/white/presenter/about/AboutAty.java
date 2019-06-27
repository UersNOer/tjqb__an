package com.market.ssvip.white.presenter.about;

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
public class AboutAty extends AppBaseActivity {

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
  @BindView(R.id.iv_about_QR)
  ImageView ivAboutQR;

  @Override
  protected int getLayoutId() {
    return R.layout.module_aty_about;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    tvToolbarTitle.setText("关于优钱花");
//    ImageLoadBuilder.load(ivAboutQR, "http://image.vip-black.com/wechat/TJKQRCODE.jpg")
//        .build();
  }
}
