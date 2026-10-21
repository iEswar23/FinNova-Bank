package com.eswar.finnova.data.repository;

/** Async result from the data layer. Called back on the main thread. */
public interface DataCallback<T> {
    void onSuccess(T data);

    void onError(Throwable error);
}
