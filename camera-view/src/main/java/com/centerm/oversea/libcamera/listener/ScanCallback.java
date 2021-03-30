package com.centerm.oversea.libcamera.listener;

import java.io.Serializable;

/**
 * author：huangmin
 * time：2/3/21
 * describe：
 */
public interface ScanCallback extends Serializable {
    /**
     * 扫码成功
     */
    void onCaptured(byte[] result) throws Exception;

    /**
     * 扫码失败
     */
    void onFailed(int errorCode,String errMsg) throws Exception;
}
