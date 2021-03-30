package com.enhance.rx;

/**
 * author：huangmin
 * time：3/29/21
 * describe：观察者线程切换操作符
 */
public class ObserverObservable<T> implements ObservableOnSubscribe<T> {
    private ObservableOnSubscribe<T> source;
    private int thread;

    public ObserverObservable(ObservableOnSubscribe<T> source, int thread) {
        this.source = source;
        this.thread = thread;
    }


    @Override
    public void subscribe(Observer<T> emitter) {
        ObserverObserver<T> observerObserver = new ObserverObserver<>(emitter, thread);
        Schedulers.getInstance().submitSubcribeWork(this.source, observerObserver, this.thread);
    }

    class ObserverObserver<K> implements Observer<K> {
        private Observer<K> observer;
        private int thread;

        ObserverObserver(Observer<K> observer, int thread) {
            this.observer = observer;
            this.thread = thread;
        }

        @Override
        public void onSubscribe() {
            Schedulers.getInstance().submitObserverWork(() -> observer.onSubscribe(), thread);

        }

        @Override
        public void onNext(K k) {
            Schedulers.getInstance().submitObserverWork(() -> observer.onNext(k), thread);

        }

        @Override
        public void onError(Throwable throwable) {
            Schedulers.getInstance().submitObserverWork(() -> observer.onError(throwable), thread);
        }

        @Override
        public void onComplete() {
            Schedulers.getInstance().submitObserverWork(() -> observer.onComplete(), thread);
        }
    }
}
