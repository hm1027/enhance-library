package com.enhance.adapter.rx;

/**
 * author：huangmin
 * time：3/29/21
 * describe：map中转，同时连接真正的观察者和被观察者
 */
public final class MapObservable<T, R> implements ObservableOnSubscribe<R> {
    private ObservableOnSubscribe<T> source;
    private Function<T, R> function;

    MapObservable(ObservableOnSubscribe<T> source, Function<T, R> function) {
        this.source = source;
        this.function = function;
    }

    @Override
    public void subscribe(Observer<R> emitter) {
        MapObserver<T, R> map = new MapObserver<>(emitter, function);
        source.subscribe(map);
    }

    final class MapObserver<E, K> implements Observer<E> {
        private Observer<K> observer;
        private Function<E, K> func;

        MapObserver(Observer<K> observer, Function<E, K> func) {
            this.observer = observer;
            this.func = func;
        }

        @Override
        public void onSubscribe() {
            observer.onSubscribe();
        }

        @Override
        public void onNext(E e) {
            observer.onNext(func.apply(e));
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
