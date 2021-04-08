package com.enhance.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.enhance.log.XLog;
import com.enhance.rtsp.utils.ConnectCheckerRtsp;
import com.enhance.rtspserver.RtspServerDisplay;

public class MainActivity extends AppCompatActivity {
    private final int LOCAL_REQUEST_CODE = 100;
    private RtspServerDisplay mRtspServerDisplay;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        XLog.init(true);
        startRecode();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mRtspServerDisplay != null) {
            if (mRtspServerDisplay.isStreaming()) {
                mRtspServerDisplay.stopStream();
            }
            mRtspServerDisplay = null;
        }
    }

    private void startRecode() {
        //若仍处于弹框处理，不响应再次申请
        //Still in the pop-up box, do not finish

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
        }
        MediaProjectionManager mProjectionManager = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        Intent captureIntent = mProjectionManager.createScreenCaptureIntent();
        startActivityForResult(captureIntent, LOCAL_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOCAL_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Log.d(TAG, "screen record permission success");
                startRtspServerSteam(resultCode, data);
            } else {
                Log.d(TAG, "screen record permission failed");
                finish();
            }
        }
    }

    private void startRtspServerSteam(final int resultCode, final Intent data) {
        new Thread(() -> {
            mRtspServerDisplay = new RtspServerDisplay(MainActivity.this, true, new ConnectCheckerRtsp() {
                @Override
                public void onConnectionSuccessRtsp() {
                    XLog.d("Rtsp client connect success.");
                }

                @Override
                public void onConnectionFailedRtsp(String reason) {
                    Log.d(TAG, "Rtsp client disconnect.");
                }

                @Override
                public void onNewBitrateRtsp(long bitrate) {
                }

                @Override
                public void onDisconnectRtsp() {
                    Log.d(TAG, "Rtsp service disconnect.");
                }

                @Override
                public void onAuthErrorRtsp() {
                    Log.d(TAG, "Rtsp client auth fail.");
                }

                @Override
                public void onAuthSuccessRtsp() {
                    Log.d(TAG, "Rtsp client auth success.");
                }
            }, 1935);
            mRtspServerDisplay.setIntentResult(resultCode, data);

            if (mRtspServerDisplay.prepareAudio() && mRtspServerDisplay.prepareVideo(720, 1280, 10, 720 * 1280, 0, 320)) {
                mRtspServerDisplay.disableAudio();
                mRtspServerDisplay.startStream();
            }

        }).start();
    }
}
