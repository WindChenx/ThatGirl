package com.example.thatgirl.model.helper;

import com.example.thatgirl.entity.Girl;
import com.example.thatgirl.model.https.api.ApiService;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;


public class NetworkHelperImp implements NetworkHelper {
    private ApiService mApiService;

    public NetworkHelperImp(ApiService retrofitService){
        mApiService = retrofitService;
    }

    @Override
    public Call<ResponseBody> getGirl(int pageNumber) {
        return mApiService.getGirl(pageNumber);
    }
}
