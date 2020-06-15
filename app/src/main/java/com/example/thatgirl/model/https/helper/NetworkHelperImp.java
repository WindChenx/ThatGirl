package com.example.thatgirl.model.https.helper;

import com.example.thatgirl.entity.Girl;
import com.example.thatgirl.model.https.api.ApiService;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.Call;


public class NetworkHelperImp implements NetworkHelper{
    private ApiService mApiService;

    public NetworkHelperImp(ApiService mApiService) {
        this.mApiService = mApiService;
    }


    @Override
    public Call<Girl> getGirl(int PageNumber) {
        return null;
    }
}
