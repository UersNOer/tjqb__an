package com.loveplusplus.update;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

public class UpdateChecker {


    public static void checkForDialog(Activity context) {
        if (context != null) {
            new CheckUpdateTask(context, Constants.TYPE_DIALOG, true).execute();
        } else {
            Log.e(Constants.TAG, "The arg context is null");
        }
    }

    public static void checkForDialog(Activity context, int ver, String msg, String url, String fouce) {
        if (context != null) {
            new CheckUpdateTask(context, Constants.TYPE_DIALOG, true, ver, msg, url,fouce).execute();
        } else {
            Log.e(Constants.TAG, "The arg context is null");
        }
    }


    public static void checkForNotification(Activity context) {
        if (context != null) {
            new CheckUpdateTask(context, Constants.TYPE_NOTIFICATION, false).execute();
        } else {
            Log.e(Constants.TAG, "The arg context is null");
        }

    }


}
