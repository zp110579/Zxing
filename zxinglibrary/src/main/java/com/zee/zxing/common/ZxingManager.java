package com.zee.zxing.common;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.zee.zxing.bean.ZxingConfig;
import com.zee.zxing.bean.ZxingResultListener;
import com.zee.zxing.fragment.ZxingResultFragment;

public class ZxingManager {
    private ZxingConfig mZxingConfig;
    private ZxingResultListener mZxingResultListener;
    private Activity mActivity;

    public static ZxingManager newInstance() {
        ZxingManager fragment = new ZxingManager();
        return fragment;
    }

    public ZxingManager with(Activity activity) {
        this.mActivity = activity;
        return this;
    }

    public ZxingManager with(Fragment fragment) {
        mActivity = fragment.getActivity();
        return this;
    }

    public ZxingManager with(android.app.Fragment fragment) {
        mActivity = fragment.getActivity();
        return this;
    }

    public ZxingManager setZxingConfig(ZxingConfig config) {
        this.mZxingConfig = config;
        return this;
    }

    public ZxingManager setZxingResultListener(ZxingResultListener zxingResultListener) {
        mZxingResultListener = zxingResultListener;
        return this;
    }

    public void letsGo() {
        /*ZxingConfig是配置类
         *可以设置是否显示底部布局，闪光灯，相册，
         * 是否播放提示音  震动
         * 设置扫描框颜色等
         * 也可以不传这个参数
         * */
        // config.setPlayBeep(false);//是否播放扫描声音 默认为true
        // config.setShake(false);//是否震动  默认为true
        // config.setDecodeBarCode(false);//是否扫描条形码 默认为true
        // config.setReactColor(R.color.colorAccent);//设置扫描框四个角的颜色 默认为白色
        // config.setFrameLineColor(R.color.colorAccent);//设置扫描框边框颜色 默认无色
        // config.setScanLineColor(R.color.colorAccent);//设置扫描线的颜色 默认白色
        //config.setFullScreenScan(false);//是否全屏扫描  默认为true  设为false则只会在扫描框中扫描

        ZxingResultFragment.newInstant().prepareRequest(mActivity, mZxingResultListener, mZxingConfig);
    }
}
