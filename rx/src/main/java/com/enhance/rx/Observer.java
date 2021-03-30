package com.enhance.rx;

/**
 * author：huangmin
 * time：3/29/21
 * describe：观察者
 */
public interface Observer<T> {
    void onSubscribe();

    void onNext(T t);

    void onError(Throwable throwable);

    void onComplete();

}
