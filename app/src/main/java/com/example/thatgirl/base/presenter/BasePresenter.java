package com.example.thatgirl.base.presenter;

import com.example.thatgirl.base.view.BaseView;
import com.example.thatgirl.model.DataModel;
import com.example.thatgirl.model.helper.NetworkHelperImp;
import com.example.thatgirl.model.https.api.RetrofitFactory;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;

public class BasePresenter<T extends BaseView> implements IPresenter<T> {
    protected T mView;
    protected CompositeDisposable mCompositeDisposable;
    protected DataModel model;

    public BasePresenter() {
       if(model==null){
           model=new DataModel(new NetworkHelperImp(RetrofitFactory.createRequest()));
       }
    }

    @Override
    public void attachView(T view) {
        mView=view;
    }

    @Override
    public void detachView() {
        mView=null;
        if(mCompositeDisposable!=null){
            mCompositeDisposable.clear();
        }
    }

    @Override
    public void addRxSubscribe(Disposable disposable) {
        if(mCompositeDisposable==null){
            mCompositeDisposable=new CompositeDisposable();
        }
        mCompositeDisposable.add(disposable);
    }

    @Override
    public int getThemeMode() {
        return 0;
    }

    @Override
    public void setThemeMode(int mode) {

    }
}
