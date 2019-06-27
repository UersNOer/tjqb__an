package com.market.ssvip.white.presenter;

import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.SimpleOnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.market.ssvip.white.R;
import com.market.ssvip.white.base.BaseFragment;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author LiCola
 * @date 2018/10/30
 */
public class TabBottomFrag extends BaseFragment {

  public static final int POSITION_LOAN = 0;
  public static final int POSITION_MONEY = 1;
  public static final int POSITION_HOME = 2;
  public static final int POSITION_MINE = 3;


  @IntDef({POSITION_LOAN, POSITION_MONEY, POSITION_HOME, POSITION_MINE})
  @Retention(RetentionPolicy.SOURCE)
  public @interface Position {

  }

  @BindView(R.id.tv_tab_loan)
  TextView tvTabLoad;
  @BindView(R.id.layout_tab_loan)
  FrameLayout layoutTabLoad;
  @BindView(R.id.tv_tab_money)
  TextView tvTabMoney;
  @BindView(R.id.layout_tab_money)
  FrameLayout layoutTabMoney;
  @BindView(R.id.tv_tab_home)
  TextView tvTabHome;
  @BindView(R.id.layout_tab_home)
  FrameLayout layoutTabHome;
  @BindView(R.id.tv_tab_mine)
  TextView tvTabMine;
  @BindView(R.id.layout_tab_mine)
  FrameLayout layoutTabMine;

  private ViewGroup viewActiveTab;

  private OnMainTabClickListener onMainTabClickListener;

  @Override
  protected int getLayoutId() {
    return R.layout.module_frag_tab_bottom;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View rootView = super.onCreateView(inflater, container, savedInstanceState);
    return rootView;
  }

  @OnClick({R.id.layout_tab_loan, R.id.layout_tab_money, R.id.layout_tab_home,
      R.id.layout_tab_mine})
  public void onTabClick(View view) {
    if (onMainTabClickListener == null) {
      return;
    }

    int id = view.getId();
    switch (id) {
      case R.id.layout_tab_loan:
        onMainTabClickListener.onClick(POSITION_LOAN);
        break;
      case R.id.layout_tab_money:
        onMainTabClickListener.onClick(POSITION_MONEY);
        break;
      case R.id.layout_tab_home:
        onMainTabClickListener.onClick(POSITION_HOME);
        break;
      case R.id.layout_tab_mine:
        onMainTabClickListener.onClick(POSITION_MINE);
        break;
      default:
        break;
    }
  }

  public void setOnMainTabClickListener(
      OnMainTabClickListener onMainTabClickListener) {
    this.onMainTabClickListener = onMainTabClickListener;
  }

  public void registerViewChange(ViewPager viewPager) {
    if (viewPager == null) {
      return;
    }

    viewPager.addOnPageChangeListener(new SimpleOnPageChangeListener() {
      @Override
      public void onPageSelected(@Position int position) {
        updateTabGroup(position);
      }
    });
  }

  public int showTabMode(boolean isShowApply) {
    if (isShowApply) {
      layoutTabLoad.setVisibility(View.GONE);
      layoutTabMoney.setVisibility(View.GONE);
      layoutTabHome.setVisibility(View.VISIBLE);
      layoutTabMine.setVisibility(View.VISIBLE);
      int positionHome = POSITION_HOME;
      updateTabGroup(positionHome);
      return positionHome;
    } else {
      layoutTabLoad.setVisibility(View.VISIBLE);
      layoutTabMoney.setVisibility(View.VISIBLE);
      layoutTabHome.setVisibility(View.GONE);
      layoutTabMine.setVisibility(View.VISIBLE);
      int positionLoan = POSITION_LOAN;
      updateTabGroup(positionLoan);
      return positionLoan;
    }
  }

  public void updateTabGroup(@Position int position) {

    ViewGroup selectTabGroup;
    switch (position) {
      case TabBottomFrag.POSITION_LOAN:
        selectTabGroup = layoutTabLoad;
        break;
      case TabBottomFrag.POSITION_MONEY:
        selectTabGroup = layoutTabMoney;
        break;
      case TabBottomFrag.POSITION_HOME:
        selectTabGroup = layoutTabHome;
        break;
      case TabBottomFrag.POSITION_MINE:
        selectTabGroup = layoutTabMine;
        break;
      default:
        selectTabGroup = null;
        break;
    }

    if (selectTabGroup == null) {
      return;
    }

    if (viewActiveTab != null) {
      viewActiveTab.setSelected(false);
    }
    selectTabGroup.setSelected(true);
    viewActiveTab = selectTabGroup;
  }

  public interface OnMainTabClickListener {

    void onClick(@Position int position);

  }
}
