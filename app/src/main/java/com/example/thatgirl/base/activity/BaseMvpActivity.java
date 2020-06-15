package com.example.thatgirl.base.activity;

import com.example.thatgirl.base.presenter.IPresenter;
import com.example.thatgirl.base.view.BaseView;

public abstract class BaseMvpActivity<T extends IPresenter> extends BaseActivity  implements BaseView {
    protected abstract T getPresenter();
    protected T mPresenter;

    @Override
    protected void onViewCreated() {
        if(mPresenter!=null){
            mPresenter.attachView(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mPresenter!=null){
            mPresenter.detachView();
            mPresenter=null;
        }
    }

    @Override
    public void showNormal() {

    }

    @Override
    public void showError() {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void showErrorMsg(String errorMsg) {

    }

    @Override
    public void reload() {

    }
}
