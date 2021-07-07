package com.flzt.gesturelibrary.GestureVerify;

import android.content.Context;
import android.gesture.GestureOverlayView;
import android.graphics.PixelFormat;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;


public class CreateFloatWindow {


    public static View createImage(Context context) {
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        //设置type.系统提示型窗口，一般都在应用程序窗口之上.
        //Android8.0行为变更，对8.0进行适配https://developer.android.google.cn/about/versions/oreo/android-8.0-changes#o-apps
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY; //2038
        } else {
            layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;  //2002
        }
        //设置效果为背景透明.
        layoutParams.format = PixelFormat.RGBA_8888;
        //设置flags.不可聚焦及不可使用按钮对悬浮窗进行操控.
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
//                | WindowManager.LayoutParams.FLAG_FULLSCREEN
//                | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
//                // API 19 以上则还可以开启透明状态栏与导航栏
//                | WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
//                | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;


        //设置窗口初始停靠位置
        layoutParams.gravity = Gravity.BOTTOM | Gravity.CENTER;
        //设置悬浮窗口长宽数据
        layoutParams.width = 160;
        layoutParams.height = 160;
        layoutParams.y = 500;
        layoutParams.x = 0;
        //获取浮动窗口视图所在的布局
        View displayView = new View(context);
        displayView.setBackgroundColor(0x55FF0000);

        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.addView(displayView, layoutParams);
        return displayView;
    }

    public static GestureOverlayView createGestureView(Context context) {
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        //设置type.系统提示型窗口，一般都在应用程序窗口之上.
        //Android8.0行为变更，对8.0进行适配https://developer.android.google.cn/about/versions/oreo/android-8.0-changes#o-apps
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY; //2038
        } else {
            layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;  //2002
        }
        //设置效果为背景透明.
        layoutParams.format = PixelFormat.RGBA_8888;
        //设置flags.不可聚焦及不可使用按钮对悬浮窗进行操控.
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
//                | WindowManager.LayoutParams.FLAG_FULLSCREEN
//                | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
//                // API 19 以上则还可以开启透明状态栏与导航栏
//                | WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
//                | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;


        //设置窗口初始停靠位置
        layoutParams.gravity = Gravity.START | Gravity.TOP;
        //设置悬浮窗口长宽数据
        layoutParams.width = context.getResources().getDisplayMetrics().widthPixels;
        layoutParams.height = context.getResources().getDisplayMetrics().heightPixels;
        //获取浮动窗口视图所在的布局
        GestureOverlayView gestureView = new GestureOverlayView(context);
        gestureView.setFadeOffset(300);
        gestureView.setGestureStrokeType(GestureOverlayView.GESTURE_STROKE_TYPE_MULTIPLE);

        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.addView(gestureView, layoutParams);
        return gestureView;
    }


}
