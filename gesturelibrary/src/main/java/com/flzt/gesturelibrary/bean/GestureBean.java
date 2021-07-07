package com.flzt.gesturelibrary.bean;

import android.gesture.Gesture;
import android.text.TextUtils;

import androidx.annotation.Nullable;


/**
 * Created by peter on 12/22/2020.
 */
public class GestureBean {
    private Gesture gesture;
    private String gestureAction;
    private int itemType;

    public GestureBean(Gesture gesture, String gestureAction) {
        this.gesture = gesture;
        this.gestureAction = gestureAction;
    }


    public Gesture getGesture() {
        return gesture;
    }

    public void setGesture(Gesture gesture) {
        this.gesture = gesture;
    }

    public String getGestureAction() {
        return gestureAction;
    }

    public void setGestureAction(String gestureAction) {
        this.gestureAction = gestureAction;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }


    @Override
    public boolean equals(@Nullable Object obj) {
        if (!(obj instanceof GestureBean))
            return false;
        GestureBean holder = (GestureBean) obj;
        return TextUtils.equals(this.gestureAction, holder.gestureAction);
    }
}
