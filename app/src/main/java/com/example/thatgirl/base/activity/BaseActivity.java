package com.example.thatgirl.base.activity;

import android.os.Bundle;
import android.os.PersistableBundle;

import com.example.thatgirl.base.presenter.IPresenter;
import com.example.thatgirl.base.view.BaseView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity implements BaseView {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
        onViewCreated();
        initData();
        onClick();
    }


    protected abstract int getLayout();
    protected abstract void onViewCreated();
    protected abstract void initData();
    protected abstract void onClick();
//    void showNormal();
//    void showError();
//    void showLoading();
//    void showErrorMsg(String errorMsg);
//    void useThemeMode(int mode);
//    void reload();


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
    public void useThemeMode(int mode) {

    }

    @Override
    public void reload() {

    }
}
