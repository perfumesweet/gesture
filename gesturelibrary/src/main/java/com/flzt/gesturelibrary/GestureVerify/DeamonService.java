package com.flzt.gesturelibrary.GestureVerify;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.flzt.gesturelibrary.R;

import java.util.ArrayList;

import static com.flzt.gesturelibrary.GestureVerify.CreateFloatWindow.createGestureView;
import static com.flzt.gesturelibrary.GestureVerify.CreateFloatWindow.createImage;
import static com.flzt.gesturelibrary.S.s;
import static com.flzt.gesturelibrary.util.Utils.getApp;
import static com.flzt.gesturelibrary.util.Utils.toastS;


public class DeamonService extends Service {

    private static GestureLibrary gLib = GestureLibraries.fromFile(getApp().getExternalFilesDir(null) + "/" + "gesture");

    private boolean isShow;

    private View displayView;


    @Override
    public void onCreate() {

        super.onCreate();


        goFrontGround();

        gLib.load();

        s("    service  onCreate   ");
    }


    public void goFrontGround() {

        //前台服务
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelName = getString(R.string.channel_name);
            //设置通知的重要程度
            int importance = NotificationManager.IMPORTANCE_HIGH;
            //构建通知渠道
            NotificationChannel channel = new NotificationChannel("1", channelName, importance);
            channel.setDescription("ok");
            //在创建的通知渠道上发送通知
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "1");
            builder.setSmallIcon(R.drawable.ic_baseline_pan_tool_24) //设置通知图标
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(),
                            R.drawable.ic_baseline_pan_tool_24))
                    .setContentTitle(getApplication().getString(R.string.channel_name))//设置通知标题
                    .setContentText(getApplication().getString(R.string.channel_description))//设置通知内容
                    .setAutoCancel(true) //用户触摸时，自动关闭
                    .setOngoing(true);//设置处于运行状态
            //向系统注册通知渠道，注册后不能改变重要性以及其他通知行为
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
            //将服务置于启动状态 NOTIFICATION_ID指的是创建的通知的ID
            startForeground(1, builder.build());
        } else {
            Notification notification = new NotificationCompat.Builder(this)
                    .setContentTitle(getApplication().getString(R.string.channel_name))
                    .setContentText(getApplication().getString(R.string.channel_description))
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.drawable.ic_baseline_pan_tool_24)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(),
                            R.drawable.ic_baseline_pan_tool_24))
                    //.setContentIntent(pi)
                    .build();
            startForeground(1, notification);
        }
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startID) {

        if (!isShow) {
            displayView = createImage(this);
            isShow = true;
        } else if (displayView != null) {
            ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).removeView(displayView);
            displayView = null;
            isShow = false;
        }

        if (displayView != null) {
            displayView.setOnTouchListener(new View.OnTouchListener() {
                GestureOverlayView gestureView;

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            displayView.setBackgroundColor(0x00000000);
                            gestureView = createGestureView(DeamonService.this);
                            gestureView.addOnGesturePerformedListener(new GestureOverlayView.OnGesturePerformedListener() {
                                @Override
                                public void onGesturePerformed(GestureOverlayView gestureView,
                                                               Gesture gesture) {

                                    ArrayList<Prediction> predictions = gLib.recognize(gesture);
                                    s("  xxx  " + gesture.getStrokesCount() + "  " + predictions.size());
                                    for (Prediction prediction : predictions) {
                                        s("  yyy  " + prediction.name + " " + prediction.score);
                                    }
                                    // one prediction needed
                                    if (predictions.size() > 0) {
                                        Prediction prediction = predictions.get(0);
                                        // checking prediction
                                        if (prediction.score > 2.0) {
                                            toastS(prediction.name);
                                        } else {
                                            toastS("未设置此手势");
                                        }
                                    }
                                    ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).removeView(gestureView);
                                }
                            });
                            MotionEvent cloneEvent1 = MotionEvent.obtain(event);
                            cloneEvent1.setLocation(event.getRawX(), event.getRawY());
                            gestureView.dispatchTouchEvent(cloneEvent1);
                            cloneEvent1.recycle();

                            break;
                        case MotionEvent.ACTION_MOVE:
                            MotionEvent cloneEvent2 = MotionEvent.obtain(event);
                            cloneEvent2.setLocation(event.getRawX(), event.getRawY());
                            gestureView.dispatchTouchEvent(cloneEvent2);
                            cloneEvent2.recycle();
                            break;
                        case MotionEvent.ACTION_UP:
                            MotionEvent cloneEvent3 = MotionEvent.obtain(event);
                            cloneEvent3.setLocation(event.getRawX(), event.getRawY());
                            gestureView.dispatchTouchEvent(cloneEvent3);
                            cloneEvent3.recycle();

                            displayView.setBackgroundColor(0x55FF0000);
                            break;
                        default:
                            break;
                    }
                    return true;
                }
            });
        }


        return START_STICKY;
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static void reLoadGestureLibrary() {
        gLib.load();
    }


}