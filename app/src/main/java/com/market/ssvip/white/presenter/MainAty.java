package com.market.ssvip.white.presenter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.KeyEvent;
import butterknife.BindView;
import com.licola.llogger.LLogger;
import com.market.ssvip.white.R;
import com.market.ssvip.white.base.AppBaseActivity;
import com.market.ssvip.white.base.BaseFragment;
import com.market.ssvip.white.component.fragment.FragmentPagerRebuildAdapter;
import com.market.ssvip.white.presenter.TabBottomFrag.OnMainTabClickListener;
import com.market.ssvip.white.presenter.TabBottomFrag.Position;
import com.market.ssvip.white.presenter.home.HomeFrag;
import com.market.ssvip.white.presenter.loan.LoanFrag;
import com.market.ssvip.white.presenter.moeny.MoneyFrag;
import com.market.ssvip.white.presenter.user.UserFrag;
import com.market.ssvip.white.view.widget.FixedViewPager;


/**
 * @author LiCola
 * @date 2018/10/29
 */

public class MainAty extends AppBaseActivity {


  /**
   * model	Integer	应用模式 1 申请模式 2 贷超模式
   * status	Integer	应用状态 0 不展示申请模式贷超 1 展示申请模式贷超 model : 1 status :
   * 1
   */

  private static final String KEY_MODEL = "key_model";
  private static final String KEY_STATUS = "key_status";

  private static final int MODE_APPLY = 1;
  private static final int MODE_LOAN = 2;

  private static final int PAGE_SIZE = 4;

  @BindView(R.id.viewpager)
  FixedViewPager viewpager;

  private TabBottomFrag tabBottomFrag;

  private TabFragmentAdapter fragmentAdapter;

  public static Intent makeIntent(Context context, int model, int status) {
    Intent intent = new Intent(context, MainAty.class);
    intent.putExtra(KEY_MODEL, model);
    intent.putExtra(KEY_STATUS, status);
//    intent.putExtra(KEY_MODEL, MODE_APPLY);
//    intent.putExtra(KEY_STATUS, 0);
    return intent;
  }

  @Override
  protected int getLayoutId() {
    return R.layout.module_aty_main;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    try {
      //包名
      String pkName = this.getPackageName();
      //versionName
      String versionName = this.getPackageManager().getPackageInfo(
              pkName, 0).versionName;
      //versionCode
      int versionCode = this.getPackageManager()
              .getPackageInfo(pkName, 0).versionCode;
      Log.i("TAG----", pkName + "   " + versionName + "  " + versionCode);
    } catch (Exception e) {
    }
    FragmentManager fragmentManager = getSupportFragmentManager();
    fragmentAdapter = new TabFragmentAdapter(fragmentManager, PAGE_SIZE);
    viewpager.setAdapter(fragmentAdapter);
    tabBottomFrag = (TabBottomFrag) fragmentManager
        .findFragmentById(R.id.fragment_main_tab);
    tabBottomFrag.registerViewChange(viewpager);
    tabBottomFrag.setOnMainTabClickListener(new MyOnTabClickListener());

    viewpager.setOffscreenPageLimit(PAGE_SIZE);

    Intent intent = getIntent();
    loadExtras(intent.getExtras());
  }

  private void loadExtras(Bundle extras) {
    if (extras == null) {
      return;
    }

    int model = extras.getInt(KEY_MODEL);
    final int status = extras.getInt(KEY_STATUS);
    int curPosition = TabBottomFrag.POSITION_MINE;
    if (MODE_APPLY == model) {
      LLogger.d("申请模式");
      curPosition = tabBottomFrag.showTabMode(true);
    } else if (MODE_LOAN == model) {
      LLogger.d("贷超模式");
      curPosition = tabBottomFrag.showTabMode(false);
    }

    viewpager.setCurrentItem(curPosition);
    viewpager.post(new Runnable() {
      @Override
      public void run() {
        BaseFragment fragment = fragmentAdapter.getFragmentByPosition(TabBottomFrag.POSITION_HOME);
        if (fragment instanceof HomeFrag) {
          ((HomeFrag) fragment).setShowMore(status);
        }
      }
    });
  }


  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {

    if (KeyEvent.KEYCODE_BACK == keyCode) {
      if (dispatchKeyDownBack(keyCode, event)) {
        return true;
      }
    }
    return super.onKeyDown(keyCode, event);
  }

  private boolean dispatchKeyDownBack(int keyCode, KeyEvent event) {

    BaseFragment currentItem = fragmentAdapter.getFragmentByCurrentItem(viewpager);
    if (currentItem == null) {
      return false;
    }

    return currentItem.onKeyDown(keyCode, event);
  }

  private class MyOnTabClickListener implements OnMainTabClickListener {

    @Override
    public void onClick(@Position int position) {
      viewpager.setCurrentItem(position);
    }
  }

  private static final class TabFragmentAdapter extends FragmentPagerRebuildAdapter<BaseFragment> {

    public TabFragmentAdapter(FragmentManager fm, int pageSize) {
      super(fm, pageSize);
    }

    @Override
    protected BaseFragment createFragment(@Position int position) {
      switch (position) {
        case TabBottomFrag.POSITION_LOAN:
          return new LoanFrag();
        case TabBottomFrag.POSITION_MONEY:
          return new MoneyFrag();
        case TabBottomFrag.POSITION_HOME:
          return new HomeFrag();
        case TabBottomFrag.POSITION_MINE:
          return new UserFrag();
        default:
          return null;
      }
    }

    @Override
    protected void bindFragment(BaseFragment fragment, int position) {

    }
  }
}
