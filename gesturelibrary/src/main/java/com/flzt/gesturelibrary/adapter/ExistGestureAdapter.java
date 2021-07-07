package com.flzt.gesturelibrary.adapter;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.flzt.gesturelibrary.R;
import com.flzt.gesturelibrary.bean.GestureBean;

import java.util.HashMap;
import java.util.List;

import static com.flzt.gesturelibrary.GestureOperate.EditGesture.delete;
import static com.flzt.gesturelibrary.GestureOperate.EditGesture.modify;
import static com.flzt.gesturelibrary.GestureVerify.DeamonService.reLoadGestureLibrary;
import static com.flzt.gesturelibrary.dialog.Dialog.itemDialog;

public class ExistGestureAdapter extends BaseQuickAdapter<GestureBean, BaseViewHolder> {

    public static HashMap<String, RefreshHandler> reFreshMap = new HashMap<>();

    public ExistGestureAdapter(@Nullable List<GestureBean> data, String activityLabel) {
        super(R.layout.item_gesture, data);
        reFreshMap.put(activityLabel, new RefreshHandler(Looper.getMainLooper()));
    }


    @Override
    protected void convert(BaseViewHolder holder, GestureBean gestureBean) {
        holder.setImageBitmap(R.id.gesture_image,
                gestureBean.getGesture().toBitmap(100, 100, 3, Color.YELLOW));
        holder.setText(R.id.gesture_action, gestureBean.getGestureAction());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemDialog(holder.itemView.getContext(), new CharSequence[]{"修改手势名", "删除手势"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                modify(holder.itemView.getContext(), gestureBean);
                                break;
                            case 1:
                                delete(holder.itemView.getContext(), gestureBean);
                                break;
                            default:
                                break;
                        }
                    }
                });
            }
        });
    }


    /**
     * 用于多个adapter时的刷新
     * by sweet
     */

    class RefreshHandler extends Handler {
        GestureBean gestureBean1, gestureBean2;

        public RefreshHandler(@NonNull Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 0:
                    ExistGestureAdapter.this.addData(gestureBean1);
                    break;
                case 1:
                    ExistGestureAdapter.this.remove(gestureBean1);
                    break;
                case 2:
                    ExistGestureAdapter.this.setData(
                            ExistGestureAdapter.this.getData().indexOf(gestureBean1), gestureBean2);
                    break;
                default:
                    break;
            }
            reLoadGestureLibrary();
        }
    }


    public static void addGesture(String activityLabel, GestureBean... bean) {
        editGestureHelp(activityLabel, 0, bean);
    }

    public static void delGesture(String activityLabel, GestureBean... bean) {
        editGestureHelp(activityLabel, 1, bean);
    }

    public static void updGesture(String activityLabel, GestureBean... bean) {
        editGestureHelp(activityLabel, 2, bean);
    }

    private static void editGestureHelp(String activityLabel, int operate, GestureBean... bean) {
        if (bean.length <= 0)
            return;
        RefreshHandler handler = reFreshMap.get(activityLabel);
        if (handler != null) {
            Message msg = Message.obtain();
            msg.what = operate;
            handler.gestureBean1 = bean[0];
            if (bean.length == 2)
                handler.gestureBean2 = bean[1];
            handler.sendMessage(msg);
        }
    }


}
