package com.example.thatgirl.model.helper;

import com.example.thatgirl.entity.Girl;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;


public interface NetworkHelper {
    Call<ResponseBody> getGirl(int PageNumber);
}
