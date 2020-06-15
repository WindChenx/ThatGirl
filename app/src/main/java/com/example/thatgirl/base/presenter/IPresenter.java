package com.example.thatgirl.base.presenter;

import com.example.thatgirl.base.view.BaseView;

import io.reactivex.rxjava3.disposables.Disposable;

public interface IPresenter<T extends BaseView> {
    /**
     * 绑定view
     * @param view
     */
    void attachView(T view);

    /**
     * 解绑view
     */
    void detachView();
    void addRxSubscribe(Disposable disposable);
    public int getThemeMode();
    public void  setThemeMode(int mode);

}
