package com.example.thatgirl.view;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.os.Bundle;
import android.util.Log;

import com.example.thatgirl.R;
import com.example.thatgirl.adapter.GirlAdapter;
import com.example.thatgirl.base.activity.BaseActivity;
import com.example.thatgirl.base.view.BaseView;
import com.example.thatgirl.contract.IShowGrilContract;
import com.example.thatgirl.entity.Girl;
import com.example.thatgirl.model.https.api.ApiService;
import com.example.thatgirl.model.https.api.RetrofitFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements BaseView {
    private static final String TAG = "MainActivity";

    private List<Girl.DataBean> girlList=new ArrayList<>();
//    private ShowContentPresenter showContentPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




    }

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void onViewCreated() {

    }

    @Override
    protected void initData() {



    }


    @Override
    protected void onClick() {

    }





}
