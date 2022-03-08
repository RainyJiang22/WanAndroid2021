package com.base.wanandroid.utils;


import java.util.concurrent.Executor;

import io.reactivex.CompletableTransformer;
import io.reactivex.ObservableTransformer;
import io.reactivex.SingleTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class RxTransformer {
    /**
     * 异步的转换器
     *
     * @param <T>
     * @return
     */
    public static <T> ObservableTransformer<T, T> async() {
        return upstream -> upstream.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    public static <T> SingleTransformer<T, T> asyncSingle() {
        return upstream -> upstream.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    public static CompletableTransformer asyncCompletable() {
        return upstream -> upstream.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    public static <T> ObservableTransformer<T, T> executor(Executor executor) {
        return upstream -> upstream.subscribeOn(Schedulers.from(executor))
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static <T> SingleTransformer<T, T> executorSingle(Executor executor) {
        return upstream -> upstream.subscribeOn(Schedulers.from(executor))
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static CompletableTransformer executorCompletable(Executor executor) {
        return upstream -> upstream.subscribeOn(Schedulers.from(executor))
                .observeOn(AndroidSchedulers.mainThread());
    }
}
