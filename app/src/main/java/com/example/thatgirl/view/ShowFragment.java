package com.example.thatgirl.view;

import android.content.Context;
import android.os.Bundle;
import android.sax.EndElementListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.thatgirl.R;
import com.example.thatgirl.adapter.GirlAdapter;
import com.example.thatgirl.entity.Girl;
import com.example.thatgirl.model.https.api.ApiService;
import com.example.thatgirl.model.https.api.RetrofitFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowFragment  extends Fragment {
    private GirlAdapter girlAdapter;
    private ArrayList<Girl.DataBean> girlList=new ArrayList<>();
    Context context=getContext();
    RecyclerView recyclerView;
    private List<Integer> mHeight;
    private SwipeRefreshLayout swipeRefreshLayout;
    private int pageNumber = 1;
    boolean enable=true;
    private RecyclerViewLoadMoreListener mRecyclerViewLoadMoreListener;
    private boolean refresh=false;
    private boolean loadmore=false;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.show_area,container,false);
         recyclerView=view.findViewById(R.id.recycle_view);
         swipeRefreshLayout=view.findViewById(R.id.swip_refresh);
        StaggeredGridLayoutManager layoutManager=new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        girlAdapter=new GirlAdapter(context,girlList);
        recyclerView.setAdapter(girlAdapter);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                
                reloading();
            }
        });
      recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
          @Override
          public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
              super.onScrollStateChanged(recyclerView, newState);
              if (newState == RecyclerView.SCROLL_STATE_IDLE && isBottom(recyclerView)) {

                      loadMore();

              }



          }
      });

        return view;

    }

    private boolean isBottom(RecyclerView recyclerView) {
        if (recyclerView == null)
            return false;

        if (recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset() >= recyclerView.computeVerticalScrollRange())
            return true;

        return false;
    }

    public void setLoadMoreEnable(boolean enable) {
        this.enable = enable;
    }
    public interface RecyclerViewLoadMoreListener {
        public void onLoadMore();
    }

    private void reloading() {
pageNumber=1;
girlList.clear();

        initData();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
        loadMore();

    }

    private void loadMore() {
        loadmore=true;
        pageNumber+=1;
        initData();

    }

    private void initData() {
        ApiService apiService= RetrofitFactory.createRequest();
        Call<ResponseBody> call = null;
        if(loadmore){
            call= apiService.getGirl(pageNumber);
            loadmore=false;
        }else {
            call= apiService.getGirl(1);
        }
       
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.body()!=null){
                    String result;
                    try {
                        result = response.body().string();
                        Log.d("Tag",result);
                        parseJson(result);
                        girlAdapter.notifyDataSetChanged();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }else {
                    Log.d("Tag","联网成功，但是数据错误");
                }

                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void parseJson(String result) {
        try {
            JSONObject jsonObject=new JSONObject(result);
            JSONArray list=jsonObject.optJSONArray("data");


            for(int i=0;i<list.length();i++ ){
                JSONObject  jsonObject1= (JSONObject) list.get(i);

                if(jsonObject1!=null){
                    Girl.DataBean dataBean=new Girl.DataBean();
                    dataBean.setDesc(jsonObject1.getString("desc"));
                    dataBean.setUrl(jsonObject1.getString("url"));
                    Log.d("Tag",jsonObject1.getString("url"));
                    girlList.add(dataBean);
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

//        this.getActivity().runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                recyclerView.setAdapter(new GirlAdapter(context,girlList));
//
//            }
//        });


    }






}
