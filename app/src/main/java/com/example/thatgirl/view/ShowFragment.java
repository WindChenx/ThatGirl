package com.example.thatgirl.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.sax.EndElementListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.thatgirl.DetailActivity;
import com.example.thatgirl.GirlApplication;
import com.example.thatgirl.R;
import com.example.thatgirl.adapter.GirlAdapter;
import com.example.thatgirl.contract.OnItemListener;
import com.example.thatgirl.entity.Girl;
import com.example.thatgirl.model.https.api.ApiService;
import com.example.thatgirl.model.https.api.RetrofitFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;


public class ShowFragment  extends Fragment implements OnItemListener {
    private GirlAdapter girlAdapter;
    private ArrayList<Girl> girlList=new ArrayList<>();
    Context context=getContext();
    RecyclerView recyclerView;
    private List<Integer> mHeight;
    private SwipeRefreshLayout swipeRefreshLayout;
    private int pageNumber = 2;
    boolean enable=true;
    private RecyclerViewLoadMoreListener mRecyclerViewLoadMoreListener;
    private boolean refresh=false;
    private boolean loadmore=false;

    public static ShowFragment newInstance() {
        ShowFragment showFragment = new ShowFragment();
        return showFragment;
    }
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.show_area,container,false);
         recyclerView=view.findViewById(R.id.recycle_view);
         swipeRefreshLayout=view.findViewById(R.id.swip_refresh);
        StaggeredGridLayoutManager layoutManager=new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        girlAdapter=new GirlAdapter(context,girlList,this);
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

    @Override
    public void onItemClick(Girl girlItem) {
        Intent intent = new Intent(getContext(), DetailActivity.class);
        intent.putExtra("image_detail_url",girlItem.getUrl());
        startActivity(intent);
    }

    public interface RecyclerViewLoadMoreListener {
        public void onLoadMore();
    }

    private void reloading() {
        pageNumber=2;
        girlList.clear();
        initData(String.valueOf(pageNumber));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData(String.valueOf(2));
//        loadMore();

    }

    private void loadMore() {
        loadmore=true;
        pageNumber+=1;
        initData(String.valueOf(pageNumber));

    }

    /**
     * 解析数据参考
     * https://blog.csdn.net/u011546032/article/details/82561464
     */
    private void initData(String url) {
//        ApiService apiService= RetrofitFactory.createRequest();
        Request request = new Request.Builder().url("https://www.msgao.com/meinv/index_"+url+".html")
                .method("GET",null).build();
        OkHttpClient okHttpClient = new OkHttpClient();
        Call call = okHttpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull okhttp3.Response response) throws IOException {
                if(response.body()!=null){
                    String result;
                    try {
                        result = response.body().string();
                        Document document = Jsoup.parse(result);
                        Elements elements = document.getElementsByClass("warp clearfix mt14");
                        List<Element> images = elements.select("#mainbodypul > div");
                        for (Element element : images) {
//                            Log.d("TAG","-------"+element);

                            Girl dataBean=new Girl();
                            String describe = element.select("img").attr("alt");
                            String url = element.select("img").attr("src");
                            dataBean.setDesc(describe);
                            dataBean.setUrl(url);
                            girlList.add(dataBean);
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                girlAdapter.setDataList(girlList);

                            }
                        });

//                        Log.d("Tag",images);
//                        parseJson(result);
//                        girlAdapter.notifyDataSetChanged();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }else {
                    Log.d("Tag","联网成功，但是数据错误");
                }

                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }
        });
    }






}
