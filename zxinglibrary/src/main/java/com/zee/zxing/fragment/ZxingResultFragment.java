package com.zee.zxing.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import com.zee.zxing.activity.CaptureActivity;
import com.zee.zxing.bean.ZxingConfig;
import com.zee.zxing.bean.ZxingResultListener;
import com.zee.zxing.common.Constant;

import java.util.ArrayList;

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
        final String[] PERMISSIONS1 = new String[]{
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
        };

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(PERMISSIONS1, 100);
        }

        Intent intent = new Intent(getActivity(), CaptureActivity.class);

//        intent.putExtra(Constant.INTENT_ZXING_CONFIG, config);
        startActivityForResult(intent, REQUEST_CODE_SCAN);
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
    }

    /**
     * 请求权限的处理
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        ArrayList<String> list = new ArrayList<>();

        if (permissions != null && permissions.length > 0) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    //如果被拒绝，那就记录下。
                    list.add(permissions[i]);
                }
            }
        }

        getFragmentManager().beginTransaction().remove(this).commit();
    }
}
