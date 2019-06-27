package com.market.ssvip.white.presenter.apply;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;

import com.market.ssvip.white.R;
import com.market.ssvip.white.base.BaseFragment;
import com.market.ssvip.white.utils.CheckUtils;

/**
 * @author LiCola
 * @date 2018/10/31
 */
public class ApplyInfoUrgencyFrag extends BaseFragment {


  @BindView(R.id.tv_input_name)
  TextView tvInputName;
  @BindView(R.id.et_input_name)
  EditText etInputName;
  @BindView(R.id.tv_input_phone)
  TextView tvInputPhone;
  @BindView(R.id.et_input_phone)
  EditText etInputPhone;
  @BindView(R.id.tv_apply_operate)
  TextView tvApplyOperate;

  private OnInputOperateListener onInputOperateListener;

  @Override
  protected int getLayoutId() {
    return R.layout.module_frag_apply_info_urgency;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View rootView = super.onCreateView(inflater, container, savedInstanceState);
    return rootView;
  }

  @OnClick(R.id.tv_apply_operate)
  public void onOperateClick(View view) {
    String userName = etInputName.getText().toString();
    String userPhone = etInputPhone.getText().toString();
    if (CheckUtils.isEmpty(userName)) {
      showToast("请填写紧急联系人姓名");
      return;
    }

    if (CheckUtils.isEmpty(userPhone)) {
      showToast("请填写紧急联系人手机号");
      return;
    }

    if (onInputOperateListener != null) {
      onInputOperateListener.onOperate(userName, userPhone);
    }
  }

  public void setOnInputOperateListener(
      OnInputOperateListener onInputOperateListener) {
    this.onInputOperateListener = onInputOperateListener;
  }


  public interface OnInputOperateListener {

    void onOperate(String userName, String userPhone);
  }

}
