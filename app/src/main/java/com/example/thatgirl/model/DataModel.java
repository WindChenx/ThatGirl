package com.example.thatgirl.model;

import android.view.View;

import com.example.thatgirl.entity.Girl;
import com.example.thatgirl.model.helper.NetworkHelper;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class DataModel implements NetworkHelper {
    private NetworkHelper networkHelper;

    public DataModel(NetworkHelper networkHelper) {
        this.networkHelper = networkHelper;
    }

    @Override
    public Call<ResponseBody> getGirl(int PageNumber) {
        return networkHelper.getGirl(PageNumber);
    }
}
