package com.centerm.oversea.libcamera.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.TorchState;
import androidx.camera.view.CameraController;
import androidx.camera.view.LifecycleCameraController;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import com.centerm.oversea.libcamera.R;
import com.centerm.oversea.libcamera.listener.ScanCallback;
import com.centerm.oversea.libcamera.model.ScanParam;
import com.centerm.oversea.libcamera.util.ScanTimer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * author：huangmin
 * time：2/4/21
 * describe：
 */
public final class ScanView extends FrameLayout {
    private static final String TAG = "ScanView";
    private Context mContext;
    private LifecycleCameraController controller;
    private CameraSelector cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA;
    private final ExecutorService cameraExecutor = Executors.newSingleThreadExecutor();
    private ScanCallback scanCallback;
    private ScanParam scanParam;
    private ScanTimer timer;

    public ScanView(@NonNull Context context) {
        this(context, null);
    }

    public ScanView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        View v = inflate(context, R.layout.lib_camera_view, this);
        PreviewView viewFinder = v.findViewById(R.id.view_finder);
        this.controller = new LifecycleCameraController(mContext);
        //PreviewView要先设置controller，不然会创建不了Camera对象
        viewFinder.setController(controller);
    }

    public void start(Bundle scanParam, ScanCallback scanCallback, LifecycleOwner lifecycleOwner) {
        this.scanParam = new ScanParam(scanParam);
        this.cameraSelector = new CameraSelector.Builder().requireLensFacing(this.scanParam.getCameraId()).build();
        this.scanCallback = scanCallback;
        bindCameraUseCases(lifecycleOwner);
        //开始定时器
        timer = new ScanTimer(this.scanParam.getTimeout()) {
            @Override
            public void onFinish() {
                if (scanCallback != null) {
                    try {
                        scanCallback.onFailed(2000, "timeout");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        timer.start();
    }

    public void stop() {
        if (controller != null) {
            controller.unbind();
        }
        if (timer != null) {
            timer.cancel();
        }
    }

    /**
     * Declare and bind mPreview, capture and analysis use cases
     */
    private void bindCameraUseCases(LifecycleOwner lifecycleOwner) {
        this.controller.getInitializationFuture().addListener(() -> {
            controller.setCameraSelector(cameraSelector);
            controller.setImageAnalysisAnalyzer(cameraExecutor, new ScanDecodeAnalysis(mContext, scanParam, new ScanCallback() {
                @Override
                public void onCaptured(byte[] result) throws Exception {
                    scanCallback.onCaptured(result);
                    if (timer != null) {
                        timer.restart();
                    }
                }

                @Override
                public void onFailed(int errorCode, String errMsg) throws Exception {

                }
            }));
            //默认analysis和capture都会开，我们不需要capture，所以要设置下只需要analysis
            controller.setEnabledUseCases(CameraController.IMAGE_ANALYSIS);
            controller.enableTorch(scanParam.isTorch());
            controller.bindToLifecycle(lifecycleOwner);
        }, ContextCompat.getMainExecutor(mContext));
    }

    /**
     * switch camera to front or back.
     */
    @SuppressLint("RestrictedApi")
    public void switchCameraLensFacing() {
        if (isFrontCamera() && hasBackCamera()) {
            cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA;
        } else if (hasFrontCamera()) {
            cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA;
        } else {
            throw new UnsupportedOperationException("Have no front or back camera");
        }
        //re-bind use case after switching camera.
        controller.setCameraSelector(cameraSelector);
    }

    public void enableTorch(boolean enable) {
        controller.enableTorch(enable);
    }

    /**
     * disable/enable torch.
     */
    @SuppressLint("RestrictedApi")
    public void switchTorch() {
        //前置直接返回。没用
        if (isFrontCamera()) {
            return;
        }
        if (controller.getTorchState().getValue() == null) {
            return;
        }
        if (controller.getTorchState().getValue() == TorchState.OFF) {
            controller.enableTorch(true);
        } else {
            controller.enableTorch(false);
        }
    }

    /**
     * 判断当前是否是使用前置摄像头
     *
     * @return
     */
    @SuppressLint("RestrictedApi")
    private boolean isFrontCamera() {
        return cameraSelector.getLensFacing() != null
                && cameraSelector.getLensFacing() == CameraSelector.LENS_FACING_FRONT;
    }

    private boolean hasFrontCamera() {
        return controller.hasCamera(CameraSelector.DEFAULT_FRONT_CAMERA);
    }

    private boolean hasBackCamera() {
        return controller.hasCamera(CameraSelector.DEFAULT_BACK_CAMERA);
    }
}
