package com.enhance.adapter.rx;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * author：huangmin
 * time：3/29/21
 * describe：
 */
public class Schedulers {
    public static final int IO = 0;
    public static final int MAIN = 1;
    private static ExecutorService ioThreadPool = Executors.newCachedThreadPool();
    private volatile static Schedulers sInstance;
    private Handler handler = new Handler(Looper.getMainLooper(), msg -> {
        msg.getCallback().run();
        return true;
    });

    public static Schedulers getInstance() {
        if (sInstance == null) {
            synchronized (Schedulers.class) {
                if (sInstance == null) {
                    sInstance = new Schedulers();
                }
            }
        }
        return sInstance;
    }

    public <T> void submitSubcribeWork(ObservableOnSubscribe<T> observable, Observer<T> observer, int thread) {
        switch (thread) {
            case IO:
                ioThreadPool.submit(() -> observable.subscribe(observer));
                break;
            case MAIN:
                if (handler != null) {
                    Message message = Message.obtain(handler, () -> observable.subscribe(observer));

                    handler.sendMessage(message);
                }

                break;

            default:
                break;
        }
    }

    public void submitObserverWork(Runnable function, int thread) {
        switch (thread) {
            case IO:
                ioThreadPool.submit(function);
                break;
            case MAIN:
                if (handler != null) {
                    Message msg = Message.obtain(handler,function);
//                    function.run();
                    handler.sendMessage(msg);
                }
        }
    }
}
