package com.flzt.gesturelibrary.GestureOperate;

import android.content.Context;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.text.TextUtils;
import android.widget.EditText;

import com.flzt.gesturelibrary.bean.GestureBean;

import java.util.ArrayList;

import static com.flzt.gesturelibrary.adapter.ExistGestureAdapter.delGesture;
import static com.flzt.gesturelibrary.adapter.ExistGestureAdapter.updGesture;
import static com.flzt.gesturelibrary.dialog.Dialog.editDialog;
import static com.flzt.gesturelibrary.util.Utils.toastS;

public class EditGesture {


    public static void modify(Context context, GestureBean gestureBean) {
        EditText editText = new EditText(context);
        editDialog(context, "模拟输入该手势要执行的命令", editText,
                (dialog, which) -> {
                    String str = editText.getText().toString();
                    if (TextUtils.isEmpty(str)) {
                        modify(context, gestureBean);
                        toastS("请输入有效命令名");
                    } else {
                        GestureLibrary gLib = GestureLibraries.fromFile(context.getExternalFilesDir(null) + "/" + "gesture");
                        gLib.load();

                        ArrayList<Gesture> list = gLib.getGestures(gestureBean.getGestureAction());
                        if (list.size() > 0) {
                            gLib.removeEntry(gestureBean.getGestureAction());
                            gLib.addGesture(str, list.get(0));
                            if (gLib.save()) {
                                toastS("手势更名成功");
                                updGesture("MainActivity", gestureBean, new GestureBean(list.get(0), str));
                            }
                        }
                    }
                }, null);
    }

    public static void delete(Context context, GestureBean gestureBean) {
        GestureLibrary gLib = GestureLibraries.fromFile(context.getExternalFilesDir(null) + "/" + "gesture");
        gLib.load();
        gLib.removeGesture(gestureBean.getGestureAction(), gestureBean.getGesture());
        gLib.removeEntry(gestureBean.getGestureAction());
        if (gLib.save()) {
            toastS("手势删除成功");
            delGesture("MainActivity", gestureBean);
        }
    }

}
