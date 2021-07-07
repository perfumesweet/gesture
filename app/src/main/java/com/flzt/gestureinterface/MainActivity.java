package com.flzt.gestureinterface;

import android.Manifest;
import android.content.Intent;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import com.flzt.gestureinterface.databinding.ActivityMainBinding;
import com.flzt.gesturelibrary.GestureOperate.AddGestureActivity;
import com.flzt.gesturelibrary.GestureVerify.DeamonService;
import com.flzt.gesturelibrary.adapter.ExistGestureAdapter;
import com.flzt.gesturelibrary.bean.GestureBean;
import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.XXPermissions;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.flzt.gesturelibrary.S.s;

public class MainActivity extends AppCompatActivity {

    public static ActivityMainBinding binding;
    private ArrayList<GestureBean> gestureList;
    private GestureLibrary gLib;
    public ExistGestureAdapter existGestureAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        init();

        checkPermission();
    }

    private void init() {
        listGesture();

        binding.gestureList.setLayoutManager(new GridLayoutManager(this, 6));
        existGestureAdapter = new ExistGestureAdapter(gestureList, this.getClass().getSimpleName());
        binding.gestureList.setAdapter(existGestureAdapter);
    }

    private void listGesture() {
        try {
            gestureList = new ArrayList<>();
            gLib = GestureLibraries.fromFile(getExternalFilesDir(null) + "/" + "gesture");
            gLib.load();
            Set<String> gestureEntrySet = gLib.getGestureEntries();
            for (String gestureEntry : gestureEntrySet) {
//                s("   listGesture    " + gestureEntry);
                ArrayList<Gesture> list = gLib.getGestures(gestureEntry);
                for (Gesture g : list) {
//                    s("   Gesture   " + g + "  " + list.size());
                    gestureList.add(new GestureBean(g, gestureEntry));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void verifyGesture(View view) {
        startService(new Intent(this, DeamonService.class));
    }

    public void addGesture(View view) {
        startActivity(new Intent(this, AddGestureActivity.class));
    }

    public void setting(View view) {
        // TODO: 2021/7/5
    }


    private void checkPermission() {
        if (!XXPermissions.isGranted(this, Manifest.permission.SYSTEM_ALERT_WINDOW)) {
            XXPermissions.with(this).permission(Manifest.permission.SYSTEM_ALERT_WINDOW)
                    .request(new OnPermissionCallback() {
                        @Override
                        public void onGranted(List<String> permissions, boolean all) {
                            if (all) {
                                s("授予成功");
                            } else {
                                s("授予失败");
                            }
                        }

                        @Override
                        public void onDenied(List<String> permissions, boolean never) {
                            if (never) {
                                // 如果是被永久拒绝就跳转到应用权限系统设置页面
                                XXPermissions.startPermissionActivity(MainActivity.this, permissions);
                            }
                        }
                    });
        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}