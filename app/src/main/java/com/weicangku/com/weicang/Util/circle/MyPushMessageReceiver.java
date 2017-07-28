package com.weicangku.com.weicang.Util.circle;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.weicangku.com.weicang.Activity.HomeActivity;
import com.weicangku.com.weicang.R;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import cn.bmob.push.PushConstants;

/**
 * Created by Administrator on 2017/2/10.
 */

public class MyPushMessageReceiver extends BroadcastReceiver {
    private final static String TAG = "MyPushMessageReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(PushConstants.ACTION_MESSAGE)) {
            Log.e(TAG, "===bmob push   reciever===" + intent.getStringExtra(PushConstants.EXTRA_PUSH_MESSAGE_STRING));
            String msg = intent.getStringExtra(PushConstants.EXTRA_PUSH_MESSAGE_STRING);
            JSONTokener jsonTokener = new JSONTokener(msg);
            String message = null;
            try {
                JSONObject jsonObject = (JSONObject) jsonTokener.nextValue();
                message = jsonObject.getString("alert");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.emoji_6);

            PendingIntent it=PendingIntent.getActivity(context,0,new Intent(context, HomeActivity.class),0);

            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationCompat.Builder notification = new NotificationCompat.Builder(context);
            notification.setContentTitle("微仓库")
                    .setContentText(message)
                    .setLargeIcon(bitmap)
                    .setWhen(System.currentTimeMillis())
                    .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentIntent(it)
                    .setAutoCancel(true);

            manager.notify(1, notification.build());
        }
    }
}
