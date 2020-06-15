package com.example.thatgirl.base.view;

public interface BaseView {
    void showNormal();
    void showError();
    void showLoading();
    void showErrorMsg(String errorMsg);
    void useThemeMode(int mode);
    void reload();

}
