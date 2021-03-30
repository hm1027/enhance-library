package com.enhance.adapter.rx;

/**
 * author：huangmin
 * time：3/29/21
 * describe：真正的被观察者接口
 */
public interface ObservableOnSubscribe<T> {
    void subscribe(Observer<T> emitter);
}
