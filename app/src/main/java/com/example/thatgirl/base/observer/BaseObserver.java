package com.example.thatgirl.base.observer;

import android.os.Handler;

import com.example.thatgirl.base.view.BaseView;


import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.observers.ResourceObserver;

public class BaseObserver<T> extends ResourceObserver<T> {
    private BaseView baseView;

    public BaseObserver(BaseView baseView) {
        this.baseView=baseView;

    }

    public BaseObserver() {

    }

    @Override
    public void onNext(@NonNull T t) {
        new Handler().postDelayed(()->{
            baseView.showNormal();
        },500);

    }

    @Override
    public void onError(@NonNull Throwable e) {

    }

    @Override
    public void onComplete() {

    }
}
