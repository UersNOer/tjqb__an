package com.loveplusplus.update;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.LayoutInflaterCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * @author feicien (ithcheng@gmail.com)
 * @since 2016-07-05 19:21
 */
class CheckUpdateTask extends AsyncTask<Void, Void, String> {

    private final String url;
    private ProgressDialog dialog;
    private Activity mContext;
    private int mType;
    private String force;
    private boolean mShowProgressDialog;

    //通过接口获取
    private boolean needparse = true;
    private int ver = 0;
    private String msg = "";

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                final Entity_Update entity_update = (Entity_Update) msg.obj;
                SelectDialog selectDialog = new SelectDialog(mContext, R.style.dialog, entity_update, force);//创建Dialog并设置样式主题
                selectDialog.setCanceledOnTouchOutside(force.equals("0"));//设置点击Dialog外部任意区域关闭Dialog
                selectDialog.show();
            }
        }
    };


    CheckUpdateTask(Activity context, int type, boolean showProgressDialog) {

        this.mContext = context;
        this.mType = type;
        this.mShowProgressDialog = showProgressDialog;
        url = Constants.UPDATE_URL;
    }

    CheckUpdateTask(Activity context, int type, boolean showProgressDialog, int ver, String msg, String url, String force) {

        this.mContext = context;
        this.mType = type;
        this.mShowProgressDialog = showProgressDialog;
        this.force = force;
        this.url = url;
        this.ver = ver;
        this.msg = msg;
        needparse = false;

    }


    protected void onPreExecute() {
//        if (mShowProgressDialog) {
//            dialog = new ProgressDialog(mContext);
//            dialog.setMessage(mContext.getString(R.string.android_auto_update_dialog_checking));
//            dialog.show();
//        }
    }


    @Override
    protected void onPostExecute(String result) {

        if (needparse) {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }

            if (!TextUtils.isEmpty(result)) {
                parseJson(result);
            }
        } else {
            int versionCode = AppUtils.getVersionCode(mContext);
            if (ver > versionCode) {
                showDialog(mContext, msg, url);
            }
        }

    }

    private void parseJson(String result) {
        try {
            JSONObject obj = new JSONObject(result);
            String updateMessage = obj.getString(Constants.APK_UPDATE_CONTENT);
            String apkUrl = obj.getString(Constants.APK_DOWNLOAD_URL);
            int apkCode = obj.getInt(Constants.APK_VERSION_CODE);

            int versionCode = AppUtils.getVersionCode(mContext);

            if (apkCode > versionCode) {
                if (mType == Constants.TYPE_NOTIFICATION) {
                    showNotification(mContext, updateMessage, apkUrl);
                } else if (mType == Constants.TYPE_DIALOG) {
                    showDialog(mContext, updateMessage, apkUrl);
                }
            } else if (mShowProgressDialog) {
//                Toast.makeText(mContext, mContext.getString(R.string.android_auto_update_toast_no_new_update), Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            Log.e(Constants.TAG, "parse json error");
        }
    }


    /**
     * Show dialog
     */
    private void showDialog(Context context, String content, String apkUrl) {
//        UpdateDialog.show(context, content, apkUrl);
        Entity_Update entity_update = new Entity_Update();
        entity_update.content = content;
        entity_update.url = apkUrl;
        Message message = new Message();
        message.what = 1;
        message.obj = entity_update;
        handler.sendMessage(message);
    }

    /**
     * Show Notification
     */
    private void showNotification(Context context, String content, String apkUrl) {
        Intent myIntent = new Intent(context, DownloadService.class);
        myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        myIntent.putExtra(Constants.APK_DOWNLOAD_URL, apkUrl);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        int smallIcon = context.getApplicationInfo().icon;
        Notification notify = new NotificationCompat.Builder(context)
                .setTicker(context.getString(R.string.android_auto_update_notify_ticker))
                .setContentTitle(context.getString(R.string.android_auto_update_notify_content))
                .setContentText(content)
                .setSmallIcon(smallIcon)
                .setContentIntent(pendingIntent).build();

        notify.flags = android.app.Notification.FLAG_AUTO_CANCEL;
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notify);
    }

    @Override
    protected String doInBackground(Void... args) {
        if (needparse)
            return HttpUtils.get(url);
        else {
            return "";
        }

    }
}
