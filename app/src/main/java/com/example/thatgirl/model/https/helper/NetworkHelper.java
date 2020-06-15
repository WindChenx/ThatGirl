package com.example.thatgirl.model.https.helper;

import com.example.thatgirl.entity.Girl;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.Call;
import retrofit2.http.GET;


public interface NetworkHelper {
   Call<Girl> getGirl(int PageNumber);
}
