package com.example.thatgirl.contract;

import com.example.thatgirl.base.presenter.IPresenter;
import com.example.thatgirl.base.view.BaseView;
import com.example.thatgirl.entity.Girl;

import java.util.ArrayList;

public interface IShowGrilContract {
    interface View extends BaseView{
        void setGirlsList(ArrayList<Girl> girlsListBeans);
    }
    interface Presenter extends IPresenter<View>{
        void  show(int pageNum);
    }
}
