package com.example.thatgirl.model.https.api;

import com.example.thatgirl.entity.Girl;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiService {
    String Host_URL="https://gank.io";

    /**
     * 获取girl
     * @param pageNumber
     * @return
     */
    @GET("/api/v2/data/category/Girl/type/Girl/page/{pageNum}/count/10")
    Call<ResponseBody> getGirl(@Path("pageNum")int pageNumber);
}
