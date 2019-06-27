package com.market.ssvip.white.view;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.market.ssvip.white.R;

/**
 * Created by LiCola on 2017/8/11.
 */

public class Toaster {

  public static Toast makeToast(Context context, String msg) {
    Toast toast = new Toast(context);
    toast.setGravity(Gravity.CENTER, 0, 0);
    LayoutInflater inflate = (LayoutInflater)
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View toastView = inflate.inflate(R.layout.module_toast_simple, null);
    TextView tvContent = toastView.findViewById(R.id.tv_content);
    tvContent.setText(msg);
    toast.setView(toastView);
    toast.show();
    return toast;
  }
}
