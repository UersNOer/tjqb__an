package com.loveplusplus.update;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Administrator on 2018/12/19.
 */

public class SelectDialog extends AlertDialog {

    private TextView tvContent;

    public SelectDialog(Context context, int theme) {
        super(context, theme);
    }

    String force;

    public SelectDialog(Activity context, int theme, Entity_Update entity_update, String force) {
        super(context, theme);
        this.mContext = context;
        this.data = entity_update;
        this.force = force;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(mContext, R.layout.acitivty_update, null);
        TextView tvContent = (TextView) view.findViewById(R.id.contentTv);
        tvContent.setText(data.content);
        Button btDoaload = (Button) view.findViewById(R.id.bt_dowload);
        btDoaload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToDownload(mContext, data.url);
                if (force.equals("0")) {
                    SelectDialog.this.dismiss();
                }
            }
        });
        setContentView(view);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (force.equals("1")) {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                return true;
            } else {
                return false;
            }
        }

        return super.onKeyDown(keyCode, event);
    }

    public static void goToDownload(Context context, String downloadUrl) {
        Intent intent = new Intent(context.getApplicationContext(), DownloadService.class);
        intent.putExtra(Constants.APK_DOWNLOAD_URL, downloadUrl);
        context.startService(intent);
    }

    Entity_Update data;
    Activity mContext;

}