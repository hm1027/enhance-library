package com.centerm.oversea.libcamera.view;

import android.content.Context;
import android.graphics.ImageFormat;
import android.graphics.YuvImage;

import androidx.annotation.NonNull;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;

import com.centerm.oversea.libcamera.listener.ScanCallback;
import com.centerm.oversea.libcamera.model.ScanParam;

import java.nio.ByteBuffer;

/**
 * author：huangmin
 * time：1/28/21
 * describe：
 */
public final class ScanDecodeAnalysis implements ImageAnalysis.Analyzer {
    private static final String TAG = "ScanDecodeAnalysis";
    private ScanParam scanParam;
    private ScanCallback scanCallback;

    public ScanDecodeAnalysis(Context context, ScanParam scanParam, ScanCallback scanCallback) {
        this.scanParam = scanParam;
        this.scanCallback = scanCallback;
    }

    @Override
    public void analyze(@NonNull ImageProxy image) {
        ImageProxy.PlaneProxy[] planes = image.getPlanes();
        //cameraX 获取yuv
        ByteBuffer yBuffer = planes[0].getBuffer();
        ByteBuffer uBuffer = planes[1].getBuffer();
        ByteBuffer vBuffer = planes[2].getBuffer();

        int ySize = yBuffer.remaining();
        int uSize = uBuffer.remaining();
        int vSize = vBuffer.remaining();

        byte[] nv21 = new byte[ySize + uSize + vSize];

        yBuffer.get(nv21, 0, ySize);
        vBuffer.get(nv21, ySize, vSize);
        uBuffer.get(nv21, ySize + vSize, uSize);
        //获取yuvImage
        YuvImage yuvImage = new YuvImage(nv21, ImageFormat.NV21, image.getWidth(), image.getHeight(), null);

        // TODO: 3/30/21
        /**
         DecodeRet ret = decode(scanParam.isEnhance() ? 0 : 1, yuvImage.getYuvData(), yuvImage.getWidth(), yuvImage.getHeight());
         if (ret == null) {
         image.close();
         return;
         }
         if (ret.getDecodeFormat() == 0) {
         image.close();
         return;
         }
         if (scanCallback != null) {
         try {
         scanCallback.onCaptured(ret.getDecodeData());
         } catch (Exception e) {
         e.printStackTrace();
         }
         }
         if (!scanParam.isContinuous()) {
         return;
         }
         **/
        image.close();
    }
}