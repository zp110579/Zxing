package com.zee.zxing.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.zee.zxing.activity.CaptureActivity;
import com.zee.zxing.bean.ZxingConfig;
import com.zee.zxing.bean.ZxingResultListener;
import com.zee.zxing.common.Constant;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class ZxingResultFragment extends Fragment {
    private ZxingResultListener mZxingResultListener;
    private int REQUEST_CODE_SCAN = 111;
    ZxingConfig mZxingConfig;

    public static ZxingResultFragment newInstant() {
        ZxingResultFragment fragment = new ZxingResultFragment();
        return fragment;
    }

    public void prepareRequest(Activity activity, ZxingResultListener listener, ZxingConfig config) {
        //将当前的请求码和对象添加到集合中
        this.mZxingResultListener = listener;
        mZxingConfig = config;
        activity.getFragmentManager().beginTransaction().add(this, activity.getClass().getName()).commit();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        openActivity();
    }

    private void openActivity() {
        AndPermission.with(this)
                .permission(Permission.CAMERA, Permission.READ_EXTERNAL_STORAGE)
                .onGranted(new Action() {
                    @Override
                    public void onAction(List<String> permissions) {
                        Intent intent = new Intent(getActivity(), CaptureActivity.class);
                        intent.putExtra(Constant.INTENT_ZXING_CONFIG, mZxingConfig);
                        startActivityForResult(intent, REQUEST_CODE_SCAN);
                    }
                })
                .onDenied(new Action() {
                    @Override
                    public void onAction(List<String> permissions) {
                        Uri packageURI = Uri.parse("package:" + getActivity().getPackageName());
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        Toast.makeText(getActivity(), "没有权限无法扫描呦", Toast.LENGTH_LONG).show();
                        getFragmentManager().beginTransaction().remove(ZxingResultFragment.this).commit();
                    }
                }).start();

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 扫描二维码/条码回传
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            if (data != null) {
                String content = data.getStringExtra(Constant.CODED_CONTENT);
                if (mZxingResultListener != null) {
                    mZxingResultListener.finish(content);
                }
            }
        }
        getFragmentManager().beginTransaction().remove(this).commit();
    }
}
