package com.enhance.adapter.rx;

import androidx.annotation.IntDef;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * author：huangmin
 * time：3/29/21
 * describe：乞丐版RX，只有线程切换和map功能
 */
public class Observable<T> {
    private ObservableOnSubscribe<T> source;

    public Observable(ObservableOnSubscribe<T> source) {
        this.source = source;
    }

    /**
     * 创建被观察者
     *
     * @param emitter
     * @param <T>
     * @return
     */
    public static <T> Observable<T> create(ObservableOnSubscribe<T> emitter) {
        return new Observable<>(emitter);
    }

    /**
     * 连接观察者和被观察者
     *
     * @param downsteam
     */
    public void subscribe(Observer<T> downsteam) {
        downsteam.onSubscribe();
        source.subscribe(downsteam);
    }

    public <R> Observable<R> map(Function<T, R> function) {
        MapObservable<T, R> map = new MapObservable<>(source, function);
        return new Observable<>(map);
    }

    public Observable<T> subscribeOn(@Thread int thread) {
        SubscribeObservable<T> subscribe = new SubscribeObservable<>(source, thread);
        return new Observable<>(subscribe);
    }

    public Observable<T> observeOn(@Thread int thread) {
        ObserverObservable<T> observer = new ObserverObservable<>(source, thread);
        return new Observable<>(observer);
    }

    @IntDef({Schedulers.IO, Schedulers.MAIN})
    @Retention(RetentionPolicy.SOURCE)
    @Target(ElementType.PARAMETER)
    @interface Thread {

    }
}
