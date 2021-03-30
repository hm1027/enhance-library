package com.centerm.oversea.libcamera.model;

import android.os.Bundle;

import androidx.camera.core.CameraSelector;

import java.io.Serializable;

public class ScanParam implements Serializable {
    /**
     * 是否使用后置摄像头
     */
    private int cameraId;
    /**
     * 设置闪光灯使用模式
     */
    private boolean torch;
    /**
     * 设定扫描时间
     */
    private int timeout;
    /**
     * 蜂鸣
     */
    private boolean beep;
    /**
     * 连续扫码
     */
    private boolean continuous;
    /**
     * 增强扫码
     */
    private boolean enhance;

    public ScanParam(Bundle bundle) {
        this.cameraId = bundle.getInt("CAMERA_ID", CameraSelector.LENS_FACING_BACK);
        this.torch = bundle.getBoolean("TORCH", false);
        this.timeout = bundle.getInt("TIMEOUT", 60 * 1000);
        this.beep = bundle.getBoolean("BEEP", false);
        this.continuous = bundle.getBoolean("CONTINUOUS", false);
        this.enhance = bundle.getBoolean("ENHANCE",false);
    }

    public int getCameraId() {
        return cameraId;
    }

    public boolean isTorch() {
        return torch;
    }

    public int getTimeout() {
        return timeout;
    }

    public boolean isBeep() {
        return beep;
    }

    public boolean isContinuous() {
        return continuous;
    }

    public boolean isEnhance() {
        return enhance;
    }
}
