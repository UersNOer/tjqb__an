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
public class ApplyInfoCreditFrag extends BaseFragment {


  @BindView(R.id.tv_input_number)
  TextView tvInputNumber;
  @BindView(R.id.et_input_number)
  EditText etInputNumber;
  @BindView(R.id.tv_apply_operate)
  TextView tvApplyOperate;
  private OnInputOperateListener onInputOperateListener;

  @Override
  protected int getLayoutId() {
    return R.layout.module_frag_apply_info_credit;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View rootView = super.onCreateView(inflater, container, savedInstanceState);
    return rootView;
  }

  @OnClick(R.id.tv_apply_operate)
  public void onOperateClick(View view) {
    String creditNumber = etInputNumber.getText().toString();
    if (CheckUtils.isEmpty(creditNumber)) {
      showToast("请填写您的芝麻分");
      return;
    }


    if (onInputOperateListener != null) {
      onInputOperateListener.onOperate(creditNumber);
    }
  }

  public void setOnInputOperateListener(
      OnInputOperateListener onInputOperateListener) {
    this.onInputOperateListener = onInputOperateListener;
  }


  public interface OnInputOperateListener {

    void onOperate(String creditNumber);
  }

}
