package com.example.thatgirl.model.https.api;

import com.example.thatgirl.entity.Girl;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiService {
    String Host_URL="https://www.msgao.com/meinv/";

    /**
     * 获取girl
     * @param pageNumber
     * @return
     */
    @GET("index_{pageNum}.html")
    Call<ResponseBody> getGirl(@Path("pageNum")int pageNumber);
}
