package com.flzt.gesturelibrary.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

public class Dialog {

    public static void editDialog(Context context, String title, View view,
                                  DialogInterface.OnClickListener confirmListener,
                                  DialogInterface.OnClickListener cancelListener) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setView(view)
                .setPositiveButton("确定", confirmListener)
                .setNegativeButton("取消", cancelListener)
                .create()
                .show();
    }

    public static void itemDialog(Context context, CharSequence[] items, DialogInterface.OnClickListener listener) {
        new AlertDialog.Builder(context)
                .setItems(items, listener)
                .create()
                .show();
    }
}
