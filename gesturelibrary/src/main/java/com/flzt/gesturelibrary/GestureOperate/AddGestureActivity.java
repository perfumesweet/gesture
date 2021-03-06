package com.flzt.gesturelibrary.GestureOperate;

import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.flzt.gesturelibrary.R;
import com.flzt.gesturelibrary.bean.GestureBean;

import static com.flzt.gesturelibrary.adapter.ExistGestureAdapter.addGesture;
import static com.flzt.gesturelibrary.dialog.Dialog.editDialog;
import static com.flzt.gesturelibrary.util.Utils.toastS;


public class AddGestureActivity extends AppCompatActivity {

    private GestureLibrary gLib;
    private boolean gestureDrawStart;
    private Gesture currentGesture;
    private GestureOverlayView gestureOverlayView;

    /**
     * gesture listener
     */
    private GestureOverlayView.OnGestureListener gestureListener = new GestureOverlayView.OnGestureListener() {
        @Override
        public void onGestureStarted(GestureOverlayView overlay, MotionEvent event) {
//            S.s("  onGestureStarted ");
            gestureDrawStart = true;
        }

        @Override
        public void onGesture(GestureOverlayView overlay, MotionEvent event) {
//            S.s("  onGesture ");
        }

        @Override
        public void onGestureEnded(GestureOverlayView overlay, MotionEvent event) {
            currentGesture = overlay.getGesture();
//            S.s("  onGestureEnded ");
        }

        @Override
        public void onGestureCancelled(GestureOverlayView overlay, MotionEvent event) {
//            S.s("  onGestureCancelled ");
        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_gesture);
//        S.s("path = " + Environment.getExternalStorageDirectory().getAbsolutePath() +
//                "  " + getExternalFilesDir(null));


        gLib = GestureLibraries.fromFile(getExternalFilesDir(null) + "/" + "gesture");
        gLib.load();

        gestureOverlayView = (GestureOverlayView) findViewById(R.id.add_gesture);

        //??????
        //???????????????????????????????????????????????????????????????
        gestureOverlayView.setGestureStrokeType(GestureOverlayView.GESTURE_STROKE_TYPE_MULTIPLE);
        //?????????????????????(??????)
        gestureOverlayView.setGestureColor(Color.BLUE);//????????????
        //????????????????????????????????????????????????(??????)
        gestureOverlayView.setUncertainGestureColor(Color.RED);//????????????
        //?????????????????????
        gestureOverlayView.setGestureStrokeWidth(9);

        gestureOverlayView.addOnGestureListener(gestureListener);

    }


    public void redraw(View view) {
        gestureDrawStart = false;
        currentGesture = null;
        gestureOverlayView.clear(false);
    }

    public void saveGesture(View view) {
        if (!gestureDrawStart) {
            toastS("???????????????");
            return;
        }
        EditText editText = new EditText(this);
        editDialog(this, "???????????????????????????????????????", editText,
                (dialog, which) -> {
                    String str = editText.getText().toString();
                    if (TextUtils.isEmpty(str)) {
                        saveGesture(view);
                        toastS("????????????????????????");
                    } else {
                        storeGesture(str);
                    }
                }, null);
    }

    private void storeGesture(String entryName) {
        gLib.addGesture(entryName, currentGesture);
        if (gLib.save()) {
            toastS("??????????????????");
            addGesture("MainActivity",new GestureBean(currentGesture,entryName));
        } else {
            toastS( "??????????????????");
        }
        this.finish();
    }


}
