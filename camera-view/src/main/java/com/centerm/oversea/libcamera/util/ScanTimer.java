package com.centerm.oversea.libcamera.util;

import android.os.CountDownTimer;

/**
 * author：huangmin
 * time：2/5/21
 * describe：
 */
public abstract class ScanTimer extends CountDownTimer {
    /**
     * @param millisInFuture The number of millis in the future from the call
     *                       to {@link #start()} until the countdown is done and {@link #onFinish()}
     *                       is called.
     */
    public ScanTimer(long millisInFuture) {
        super(millisInFuture, millisInFuture);
    }

    @Override
    public void onTick(long millisUntilFinished) {
        //ignore
    }

    public void restart() {
        cancel();
        start();
    }
}
