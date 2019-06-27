package com.market.ssvip.white.presenter.apply;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.SimpleOnPageChangeListener;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import com.licola.llogger.LLogger;
import com.market.ssvip.white.R;
import com.market.ssvip.white.base.AppBaseActivity;
import com.market.ssvip.white.base.BaseFragment;
import com.market.ssvip.white.component.fragment.FragmentPagerRebuildAdapter;
import com.market.ssvip.white.mode.EnvManager;
import com.market.ssvip.white.mode.bean.UserOrderApplyBean;
import com.market.ssvip.white.presenter.apply.ApplyInfoUserFrag.OnInputOperateListener;
import com.market.ssvip.white.utils.ExceptionHelper;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author LiCola
 * @date 2018/10/31
 */
public class ApplyGroupAty extends AppBaseActivity {

  private static final String KEY_AMOUNT = "key_amount";


  private static final int PAGE_SIZE = 3;

  private static final int POSITION_USER = 0;
  private static final int POSITION_URGENCY = 1;
  private static final int POSITION_CREDIT = 2;


  @IntDef({POSITION_USER, POSITION_URGENCY, POSITION_CREDIT})
  @Retention(RetentionPolicy.SOURCE)
  public @interface Position {

  }

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
  @BindView(R.id.viewpager)
  ViewPager viewpager;
  @BindView(R.id.iv_step_indicate)
  ImageView ivStepIndicate;

  private String userName;
  private String userId;
  private String userUrgencyName;
  private String userUrgencyPhone;
  private String userCreditNumber;
  private int userApplyAoumt;

  public static Intent makeIntent(Context context, int amount) {
    Intent intent = new Intent(context, ApplyGroupAty.class);
    intent.putExtra(KEY_AMOUNT, amount);
    return intent;
  }

  @Override
  protected int getLayoutId() {
    return R.layout.module_aty_apply;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    tvToolbarTitle.setText("基础认证");
    Intent intent = getIntent();
    loadExtras(intent.getExtras());
    ivStepIndicate.setImageResource(R.drawable.foundation_certificate_1);
    TabFragmentAdapter adapter = new TabFragmentAdapter(getSupportFragmentManager(), PAGE_SIZE);
    viewpager.setAdapter(adapter);
    viewpager.addOnPageChangeListener(new SimpleOnPageChangeListener() {
      @Override
      public void onPageScrolled(@Position int position, float positionOffset,
          int positionOffsetPixels) {
        int resId = R.drawable.foundation_certificate_1;
        switch (position) {
          case ApplyGroupAty.POSITION_CREDIT:
            resId = R.drawable.foundation_certificate_3;
            break;
          case ApplyGroupAty.POSITION_URGENCY:
            resId = R.drawable.foundation_certificate_2;
            break;
          case ApplyGroupAty.POSITION_USER:
            resId = R.drawable.foundation_certificate_1;
            break;
        }
        ivStepIndicate.setImageResource(resId);
      }
    });

//    viewpager.postDelayed(new Runnable() {
//      @Override
//      public void run() {
//        finishApply();
//      }
//    }, 2000);
  }

  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
    if (keyCode == KeyEvent.KEYCODE_BACK) {
      int currentItem = viewpager.getCurrentItem();
      if (currentItem > POSITION_USER) {
        viewpager.setCurrentItem(--currentItem);
        return true;
      }
    }
    return super.onKeyDown(keyCode, event);
  }

  private void loadExtras(Bundle extras) {
    if (extras == null) {
      return;
    }

    userApplyAoumt = extras.getInt(KEY_AMOUNT);
  }

  private class TabFragmentAdapter extends FragmentPagerRebuildAdapter<BaseFragment> {

    public TabFragmentAdapter(FragmentManager fm, int pageSize) {
      super(fm, pageSize);
    }

    @Override
    protected BaseFragment createFragment(@Position int position) {
      switch (position) {
        case ApplyGroupAty.POSITION_CREDIT:
          return new ApplyInfoCreditFrag();
        case ApplyGroupAty.POSITION_URGENCY:
          return new ApplyInfoUrgencyFrag();
        case ApplyGroupAty.POSITION_USER:
          return new ApplyInfoUserFrag();
      }
      return null;
    }

    @Override
    protected void bindFragment(BaseFragment fragment, int position) {
      if (fragment instanceof ApplyInfoUserFrag) {
        ((ApplyInfoUserFrag) fragment).setOnInputOperateListener(new OnInputOperateListener() {
          @Override
          public void onOperate(String userName, String userId) {
            updateUserInfo(userName, userId);
          }
        });
      } else if (fragment instanceof ApplyInfoUrgencyFrag) {
        ((ApplyInfoUrgencyFrag) fragment).setOnInputOperateListener(
            new ApplyInfoUrgencyFrag.OnInputOperateListener() {
              @Override
              public void onOperate(String userName, String userPhone) {
                updateUrgency(userName, userPhone);
              }
            });
      } else if (fragment instanceof ApplyInfoCreditFrag) {
        ((ApplyInfoCreditFrag) fragment).setOnInputOperateListener(
            new ApplyInfoCreditFrag.OnInputOperateListener() {
              @Override
              public void onOperate(String creditNumber) {
                updateCreditNumber(creditNumber);
              }
            });
      }
    }

  }

  private void updateCreditNumber(String creditNumber) {
    this.userCreditNumber = creditNumber;
    operateUpdateInfo();
  }

  private void updateUrgency(String userName, String userPhone) {
    this.userUrgencyName = userName;
    this.userUrgencyPhone = userPhone;
    viewpager.setCurrentItem(viewpager.getCurrentItem() + 1);
  }

  private void updateUserInfo(String userName, String userId) {
    this.userName = userName;
    this.userId = userId;
    viewpager.setCurrentItem(viewpager.getCurrentItem() + 1);
  }

  private void operateUpdateInfo() {

    String contactsJson = makeConractJson(userUrgencyName, userUrgencyPhone);
    String idJson = makeIdJson(userName, userId);
    String zhimaJson = makeZhimaJson(userCreditNumber);

    EnvManager envManager = EnvManager.getEnvManager();
    envManager.getBasicsApi()
        .operateUserApply(envManager.getEnvDeviceId(),envManager.getEnvUserId(), userApplyAoumt, contactsJson,
            idJson, zhimaJson).enqueue(
        new Callback<UserOrderApplyBean>() {
          @Override
          public void onResponse(Call<UserOrderApplyBean> call,
              Response<UserOrderApplyBean> response) {
            finishApply();
          }

          @Override
          public void onFailure(Call<UserOrderApplyBean> call, Throwable t) {
            LLogger.d(t);
            showToast(ExceptionHelper.mapperException(t));
          }
        });
  }

  private void finishApply() {
    setResult(Activity.RESULT_OK);
    finish();
  }

  private String makeZhimaJson(String userCreditNumber) {
    JSONObject jsonResult = new JSONObject();
    try {
      jsonResult.put("score", userCreditNumber);
    } catch (JSONException e) {
      e.printStackTrace();
    }
    return jsonResult.toString();
  }

  private String makeIdJson(String userName, String userId) {
    JSONObject jsonResult = new JSONObject();
    try {
      jsonResult.put("realName", userName);
      jsonResult.put("cardNo", userId);
    } catch (JSONException e) {
      e.printStackTrace();
    }

    return jsonResult.toString();
  }

  private String makeConractJson(String userUrgencyName, String userUrgencyPhone) {
    JSONObject jsonResult = new JSONObject();
    try {
      jsonResult.put("type", 0);
      jsonResult.put("name", userUrgencyName);
      jsonResult.put("phone", userUrgencyPhone);
    } catch (JSONException e) {
      e.printStackTrace();
    }

    return jsonResult.toString();
  }





}
