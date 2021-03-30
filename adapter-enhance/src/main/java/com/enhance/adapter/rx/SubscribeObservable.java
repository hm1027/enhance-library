package com.enhance.adapter.rx;

/**
 * author：huangmin
 * time：3/29/21
 * describe：被观察者线程切换操作符
 */
public class SubscribeObservable<T> implements ObservableOnSubscribe<T> {
    private ObservableOnSubscribe<T> source;
    private int thread;

    public SubscribeObservable(ObservableOnSubscribe<T> source, int thread) {
        this.source = source;
        this.thread = thread;
    }


    @Override
    public void subscribe(Observer<T> emitter) {
        SubscribeObserver<T> subscribeObserver = new SubscribeObserver<>(emitter);
        Schedulers.getInstance().submitSubcribeWork(this.source, subscribeObserver, this.thread);
    }

    class SubscribeObserver<K> implements Observer<K> {
        private Observer<K> observer;

        SubscribeObserver(Observer<K> observer) {
            this.observer = observer;
        }

        @Override
        public void onSubscribe() {
            observer.onSubscribe();
        }

        @Override
        public void onNext(K k) {
            observer.onNext(k);
        }

        @Override
        public void onError(Throwable throwable) {
            observer.onError(throwable);
        }

        @Override
        public void onComplete() {
            observer.onComplete();
        }
    }
}
