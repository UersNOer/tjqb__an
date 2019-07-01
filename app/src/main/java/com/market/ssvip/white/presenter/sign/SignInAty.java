package com.market.ssvip.white.presenter.sign;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.licola.llogger.LLogger;
import com.market.ssvip.white.R;
import com.market.ssvip.white.base.AppBaseActivity;
import com.market.ssvip.white.mode.EnvManager;
import com.market.ssvip.white.mode.api.BasicsApi;
import com.market.ssvip.white.mode.bean.UserBean;
import com.market.ssvip.white.mode.bean.VersionBean;
import com.market.ssvip.white.presenter.MainAty;
import com.market.ssvip.white.presenter.web.WebAty;
import com.market.ssvip.white.utils.ExceptionHelper;
import com.market.ssvip.white.utils.TimeCountUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.market.ssvip.white.mode.api.BasicsApi.CODE_TYPE_LOGIN;

/**
 * @author LiCola
 * @date 2018/10/29
 */
public class SignInAty extends AppBaseActivity {

  @BindView(R.id.et_input_phone)
  EditText etInputPhone;
  @BindView(R.id.tv_fetch_code)
  TextView tvFetchCode;
  @BindView(R.id.et_input_code)
  EditText etInputCode;
  @BindView(R.id.tv_sign_in_operate)
  TextView tvSignInOperate;
  TimeCountUtil timeCountUtil;
  private boolean phoneValid = false;
  private boolean codeValid = false;

  @Override
  protected int getLayoutId() {
    return R.layout.module_aty_sign_in;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    tvFetchCode.setEnabled(false);
    tvSignInOperate.setEnabled(false);

    etInputPhone.addTextChangedListener(new MyPhoneNumberTextChangedListener());
    etInputCode.addTextChangedListener(new MyCodeNumberTextChangedListener());
  }

  @OnClick(R.id.tv_sign_protocol)
  public void onProtocolClick(View view) {
    startActivity(
        WebAty.makeIntent(mContext, "http://api.lpkjb.cn/protocol/index.html"));
  }

  @OnClick(R.id.tv_fetch_code)
  public void onFetchCodeClick(View view) {
    String phone = etInputPhone.getText().toString();
    onFetchCode(phone);
  }

  private void onFetchCode(String phone) {
    tvFetchCode.setEnabled(false);
    tvFetchCode.setText("验证码正在发送...");
    EnvManager envManager = EnvManager.getEnvManager();
    envManager.getBasicsApi().operateSendSmsCode(envManager.getEnvDeviceId(),phone, BasicsApi.CODE_TYPE_LOGIN)
        .enqueue(
            new Callback<Void>() {
              @Override
              public void onResponse(Call<Void> call, Response<Void> response) {
                showToast("验证码已经发送");
                tvFetchCode.setEnabled(true);
                timeCountUtil=new TimeCountUtil(60000, 1000, tvFetchCode, 0);
                timeCountUtil.start();
              }

              @Override
              public void onFailure(Call<Void> call, Throwable t) {
//                showToast(ExceptionHelper.mapperException(t));
                showToast("验证码获取失败,请重新获取");
                tvFetchCode.setEnabled(true);
                tvFetchCode.setText("重新获取");
                LLogger.e(t);
              }
            });
  }

  @OnClick(R.id.tv_sign_in_operate)
  public void onSignInOperateClick(View view) {
    String phone = etInputPhone.getText().toString();
    String code = etInputCode.getText().toString();
    onSignIn(phone, code);
  }

  private void onSignIn(String phone, String code) {
    LLogger.d(phone, code);
    tvSignInOperate.setEnabled(false);

    final EnvManager envManager = EnvManager.getEnvManager();
    envManager.getBasicsApi().fetchUserByWithCode(envManager.getEnvDeviceId(),phone, code,CODE_TYPE_LOGIN)
        .enqueue(new Callback<UserBean>() {
          @Override
          public void onResponse(Call<UserBean> call, Response<UserBean> response) {
            envManager.updateEnvUser(response.body());
            showToast("登陆成功");
            tvSignInOperate.setEnabled(true);
            jumpMainByType();
          }

          @Override
          public void onFailure(Call<UserBean> call, Throwable t) {
            tvSignInOperate.setEnabled(true);
            showToast(ExceptionHelper.mapperException(t));
          }
        });
  }

  private void jumpMainByType() {
    EnvManager envManager = EnvManager.getEnvManager();
    BasicsApi basicsApi = envManager.getBasicsApi();
    basicsApi.fetchVersion(envManager.getEnvDeviceId()).enqueue(new Callback<VersionBean>() {
      @Override
      public void onResponse(Call<VersionBean> call, Response<VersionBean> response) {
        toMain(response.body());
      }

      @Override
      public void onFailure(Call<VersionBean> call, Throwable t) {
        VersionBean versionBean = new VersionBean();
        versionBean.setModel(2);
        versionBean.setStatus(0);
        toMain(versionBean);
      }
    });
    basicsApi.operateActionClickRecord(envManager.getEnvDeviceId(),envManager.getEnvUserId(), "sms.login.button")
    .enqueue(new Callback<Void>() {
      @Override
      public void onResponse(Call<Void> call, Response<Void> response) {

      }

      @Override
      public void onFailure(Call<Void> call, Throwable t) {

      }
    });

  }

  private void toMain(VersionBean versionBean) {
    startActivity(MainAty.makeIntent(SignInAty.this, versionBean.getModel(),versionBean.getStatus() ));
    finish();
  }

  private void updateCodeEnable() {
    tvFetchCode.setEnabled(phoneValid);
  }

  private void updateOperateEnable() {
    tvSignInOperate.setEnabled(phoneValid && codeValid);
  }

  private class MyPhoneNumberTextChangedListener implements TextWatcher {

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

      boolean enable = s.length() >= 11;
      phoneValid = enable;

      updateCodeEnable();
      updateOperateEnable();
    }
  }

  private class MyCodeNumberTextChangedListener implements TextWatcher {

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
      boolean enable = s.length() > 0;
      codeValid = enable;
      updateOperateEnable();
    }
  }
}
