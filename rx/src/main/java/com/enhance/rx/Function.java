package com.enhance.rx;

/**
 * author：huangmin
 * time：3/29/21
 * describe：exchange T->R
 */
public interface Function<T, R> {
    R apply(T t);
}
